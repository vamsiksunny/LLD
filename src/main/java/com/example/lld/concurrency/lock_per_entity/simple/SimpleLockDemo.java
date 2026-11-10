package com.example.lld.concurrency.lock_per_entity.simple;

import com.example.lld.concurrency.Race;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleLockDemo {
    public static void main(String[] args) {
        List<Account> accounts = List.of(
                new Account(1, 1_000),
                new Account(2, 1_000),
                new Account(3, 1_000),
                new Account(4, 1_000));
        Bank bank = new Bank();
        AtomicInteger successfulTransfers = new AtomicInteger();

        // 8 threads, each does 2,000 transfers between random accounts
        Race.run(8, threadIndex -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < 2_000; i++) {
                Account from = accounts.get(random.nextInt(accounts.size()));
                Account to = accounts.get(random.nextInt(accounts.size()));
                long amount = random.nextLong(1, 200);

                if (bank.transfer(from, to, amount)) {
                    successfulTransfers.incrementAndGet();
                }
            }
        });

        long total = 0;
        int historyEntries = 0;
        for (Account account : accounts) {
            System.out.println("account " + account.getId() + " balance = " + account.getBalance());
            total += account.getBalance();
            historyEntries += account.historySize() - 1;   // minus the "open" entry
        }

        System.out.println("total = " + total + " (expected 4000)");
        System.out.println("history entries = " + historyEntries
                + ", successful transfers x 2 = " + successfulTransfers.get() * 2 + " (should match)");
    }
}
