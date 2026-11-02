package com.example.lld.concurrency.lock_per_entity;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Transfer between two accounts, holding both accounts' locks.
 *
 * Deadlock trap: thread 1 transfers A -> B and locks A first, thread 2 transfers B -> A
 * and locks B first. Each waits for the other forever. The fix is to always lock in the
 * same global order (here, lower account id first), whichever direction the money moves.
 */
public class Bank {

    public boolean transfer(Account from, Account to, long amount) {
        if (from == to) {
            return false;
        }
        Account first = from.getId() < to.getId() ? from : to;
        Account second = first == from ? to : from;
        ReentrantLock firstLock = first.lock();
        ReentrantLock secondLock = second.lock();

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                // check and act happen under both locks, so no one can change the balance in between
                if (from.balanceUnlocked() < amount) {
                    return false;
                }
                from.debit(amount, "to " + to.getId());
                to.credit(amount, "from " + from.getId());
                return true;
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}
