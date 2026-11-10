package com.example.lld.concurrency.lock_per_entity.simple;

/**
 * Transfer using synchronized blocks instead of ReentrantLock.
 *
 * Rules:
 * 1. Lock both accounts before touching either one.
 * 2. Always lock the account with the smaller id first, to avoid deadlock.
 *
 * synchronized unlocks automatically when the block ends, even if an exception is
 * thrown or we return early, so there is no try/finally to write.
 */
public class Bank {

    public boolean transfer(Account from, Account to, long amount) {
        if (from == to) {
            return false;   // can't transfer to yourself
        }

        // Smaller id first, whichever direction the money moves
        Account first = from.getId() < to.getId() ? from : to;
        Account second = (first == from) ? to : from;

        synchronized (first) {          // take the first key
            synchronized (second) {     // take the second key
                if (from.balance() < amount) {
                    return false;       // not enough money
                }
                from.debit(amount, "to " + to.getId());
                to.credit(amount, "from " + from.getId());
                return true;
            }                           // second key given back here
        }                               // first key given back here
    }
}
