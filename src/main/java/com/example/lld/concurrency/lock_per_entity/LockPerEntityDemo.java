package com.example.lld.concurrency.lock_per_entity;

import com.example.lld.concurrency.Race;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class LockPerEntityDemo {
    public static void main(String[] args) {
        List<Account> accounts = List.of(
                new Account(1, 1_000), new Account(2, 1_000), new Account(3, 1_000), new Account(4, 1_000));
        Bank bank = new Bank();
        AtomicInteger succeeded = new AtomicInteger();

        // 8 threads each do 2,000 random transfers in both directions (would deadlock without lock ordering)
        Race.run(8, t -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < 2_000; i++) {
                Account from = accounts.get(random.nextInt(accounts.size()));
                Account to = accounts.get(random.nextInt(accounts.size()));
                if (bank.transfer(from, to, random.nextLong(1, 200))) {
                    succeeded.incrementAndGet();
                }
            }
        });

        long total = 0;
        int historyEntries = 0;
        for (Account account : accounts) {
            long balance = account.getBalance();
            if (balance < 0) {
                throw new IllegalStateException("account " + account.getId() + " went negative");
            }
            total += balance;
            historyEntries += account.historySize() - 1;   // minus the "open" entry
            System.out.println("account " + account.getId() + " balance = " + balance);
        }
        System.out.println("total = " + total + " (expected 4000: money is never created or lost)");
        System.out.println("history entries = " + historyEntries + ", successful transfers x2 = "
                + succeeded.get() * 2 + " (should match: balance and history changed together)");
    }
}
