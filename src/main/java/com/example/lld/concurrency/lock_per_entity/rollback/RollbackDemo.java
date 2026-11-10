package com.example.lld.concurrency.lock_per_entity.rollback;

import com.example.lld.concurrency.Race;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RollbackDemo {
    public static void main(String[] args) {
        Bank bank = new Bank();

        System.out.println("=== 1. Validate first: failed transfers change nothing ===");
        Account alice = new Account(1, 1_000, 5_000);
        Account bob = new Account(2, 1_000, 1_200);     // bob can hold at most 1200
        Account carol = new Account(3, 1_000, 5_000);
        carol.freeze();

        show(bank.transfer(alice, bob, 100), alice, bob);     // SUCCESS
        show(bank.transfer(alice, bob, 5_000), alice, bob);   // INSUFFICIENT_FUNDS
        show(bank.transfer(alice, bob, 500), alice, bob);     // RECEIVER_LIMIT_EXCEEDED (1100 + 500 > 1200)
        show(bank.transfer(alice, carol, 100), alice, carol); // RECEIVER_FROZEN
        show(bank.transfer(alice, alice, 100), alice, alice); // SAME_ACCOUNT
        System.out.println("alice history: " + alice.getHistory());

        System.out.println();
        System.out.println("=== 2. Compensate: outside call fails, money is refunded ===");
        ExternalWallet wallet = new ExternalWallet();
        Account dave = new Account(4, 1_000, 5_000);

        System.out.println("wallet up:   " + bank.sendToWallet(dave, wallet, 300)
                + ", dave = " + dave.getBalance());
        wallet.setDown(true);
        System.out.println("wallet down: " + bank.sendToWallet(dave, wallet, 300)
                + ", dave = " + dave.getBalance() + " (refunded, still 700)");
        System.out.println("dave history: " + dave.getHistory());

        System.out.println();
        System.out.println("=== 3. Under load: rules + concurrency, money is never lost ===");
        List<Account> accounts = List.of(
                new Account(10, 1_000, 1_500),
                new Account(11, 1_000, 1_500),
                new Account(12, 1_000, 1_500),
                new Account(13, 1_000, 1_500));
        accounts.get(3).freeze();   // one frozen account: every transfer to or from it must fail

        Race.run(8, t -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < 2_000; i++) {
                Account from = accounts.get(random.nextInt(accounts.size()));
                Account to = accounts.get(random.nextInt(accounts.size()));
                bank.transfer(from, to, random.nextLong(1, 300));
            }
        });

        long total = 0;
        for (Account account : accounts) {
            long balance = account.getBalance();
            total += balance;
            System.out.println("account " + account.getId() + " = " + balance
                    + (balance < 0 || balance > 1_500 ? "  <-- RULE BROKEN" : ""));
        }
        System.out.println("total = " + total + " (expected 4000)");
        System.out.println("frozen account 13 = " + accounts.get(3).getBalance() + " (expected 1000, untouched)");
    }

    private static void show(TransferResult result, Account a, Account b) {
        System.out.printf("%-24s account %d = %d, account %d = %d%n",
                result, a.getId(), a.getBalance(), b.getId(), b.getBalance());
    }
}
