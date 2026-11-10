package com.example.lld.concurrency.lock_per_entity.simple;

import java.util.ArrayList;
import java.util.List;

/**
 * Same idea as lock_per_entity.Account, but the lock is the Account object itself.
 *
 * In Java every object has a built-in lock. synchronized (account) { ... } takes that
 * account's lock, and a synchronized method takes the lock of "this". Both use the same
 * lock, so they protect each other.
 */
public class Account {
    private final long id;
    private long balance;
    private final List<String> history = new ArrayList<>();

    public Account(long id, long openingBalance) {
        this.id = id;
        this.balance = openingBalance;
        history.add("open " + openingBalance);
    }

    public long getId() {
        return id;
    }

    // --- Used by Bank while it holds this account's lock ---

    long balance() {
        return balance;
    }

    void debit(long amount, String note) {
        balance -= amount;
        history.add("-" + amount + " " + note);
    }

    void credit(long amount, String note) {
        balance += amount;
        history.add("+" + amount + " " + note);
    }

    // --- Safe for anyone to call: these take the lock themselves ---

    public synchronized long getBalance() {
        return balance;
    }

    public synchronized int historySize() {
        return history.size();
    }
}
