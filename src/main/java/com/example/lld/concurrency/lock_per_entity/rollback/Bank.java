package com.example.lld.concurrency.lock_per_entity.rollback;

/**
 * Two ways to never leave a transfer half done.
 *
 * 1. transfer(): VALIDATE FIRST, THEN CHANGE.
 *    Every rule that could fail is checked before any data changes. Once we start
 *    writing, nothing left can fail, so there is never anything to roll back.
 *
 * 2. sendToWallet(): COMPENSATE.
 *    An outside call can fail in ways we can't check in advance. We take the money
 *    first, call the outside service, and if it fails we refund with a NEW history entry.
 *    We never erase history: the record shows both the debit and the refund.
 */
public class Bank {

    // ---------- 1. Validate first, then change ----------

    public TransferResult transfer(Account from, Account to, long amount) {
        if (from == to) {
            return TransferResult.SAME_ACCOUNT;
        }
        if (amount <= 0) {
            return TransferResult.INVALID_AMOUNT;
        }

        Account first = from.getId() < to.getId() ? from : to;
        Account second = (first == from) ? to : from;

        synchronized (first) {
            synchronized (second) {
                // Phase 1: validate. Nothing has changed yet, so failing here is free.
                if (from.frozen()) {
                    return TransferResult.SENDER_FROZEN;
                }
                if (to.frozen()) {
                    return TransferResult.RECEIVER_FROZEN;
                }
                if (from.balance() < amount) {
                    return TransferResult.INSUFFICIENT_FUNDS;
                }
                if (to.balance() + amount > to.maxBalance()) {
                    return TransferResult.RECEIVER_LIMIT_EXCEEDED;
                }

                // Phase 2: change. Only simple arithmetic, nothing here can fail.
                from.debit(amount, "to " + to.getId());
                to.credit(amount, "from " + from.getId());
                return TransferResult.SUCCESS;
            }
        }
    }

    // ---------- 2. Compensate when an outside step fails ----------

    public TransferResult sendToWallet(Account from, ExternalWallet wallet, long amount) {
        if (amount <= 0) {
            return TransferResult.INVALID_AMOUNT;
        }

        // Step 1: validate and take the money, under the account's lock.
        synchronized (from) {
            if (from.frozen()) {
                return TransferResult.SENDER_FROZEN;
            }
            if (from.balance() < amount) {
                return TransferResult.INSUFFICIENT_FUNDS;
            }
            from.debit(amount, "to wallet");
        }

        // Step 2: call the outside service WITHOUT holding the lock.
        // Outside calls can be slow; holding the lock would block every other
        // transfer on this account while we wait.
        try {
            wallet.deposit(from.getId(), amount);
            return TransferResult.SUCCESS;
        } catch (RuntimeException e) {
            // Step 3: the outside step failed. Undo step 1 with a compensating entry.
            // Taking the lock again is safe: the money was already removed, so no other
            // transfer could have spent it in the meantime.
            synchronized (from) {
                from.credit(amount, "refund: wallet transfer failed (" + e.getMessage() + ")");
            }
            return TransferResult.FAILED_AND_REFUNDED;
        }
    }
}
