package com.example.lld.concurrency.lock_per_entity.rollback;

import java.util.ArrayList;
import java.util.List;

/**
 * Account with rules that can make a transfer fail: frozen accounts and a maximum balance.
 * As in the simple package, the Account object itself is the lock.
 */
public class Account {
    private final long id;
    private final long maxBalance;
    private long balance;
    private boolean frozen;
    private final List<String> history = new ArrayList<>();

    public Account(long id, long openingBalance, long maxBalance) {
        this.id = id;
        this.balance = openingBalance;
        this.maxBalance = maxBalance;
        history.add("open " + openingBalance);
    }

    public long getId() {
        return id;
    }

    // --- Used by Bank while it holds this account's lock ---

    long balance() {
        return balance;
    }

    long maxBalance() {
        return maxBalance;
    }

    boolean frozen() {
        return frozen;
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

    public synchronized void freeze() {
        frozen = true;
        history.add("frozen");
    }

    public synchronized long getBalance() {
        return balance;
    }

    public synchronized List<String> getHistory() {
        return List.copyOf(history);   // a copy, so callers can't change our list
    }
}
