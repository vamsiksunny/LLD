package com.example.lld.concurrency.lock_per_entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Several fields must change together: balance and history.
 *
 * An AtomicLong balance alone isn't enough, because the balance check, the update and the
 * history entry have to happen as one unit. Each account has its own lock, so transfers
 * between unrelated accounts run in parallel instead of all waiting on one global lock.
 */
public class Account {
    private final long id;
    private final ReentrantLock lock = new ReentrantLock();
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

    ReentrantLock lock() {
        return lock;
    }

    /** Caller must hold this account's lock. */
    void debit(long amount, String note) {
        balance -= amount;
        history.add("-" + amount + " " + note);
    }

    /** Caller must hold this account's lock. */
    void credit(long amount, String note) {
        balance += amount;
        history.add("+" + amount + " " + note);
    }

    /** Caller must hold this account's lock. */
    long balanceUnlocked() {
        return balance;
    }

    public long getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public int historySize() {
        lock.lock();
        try {
            return history.size();
        } finally {
            lock.unlock();
        }
    }
}
