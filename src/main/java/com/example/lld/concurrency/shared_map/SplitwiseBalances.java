package com.example.lld.concurrency.shared_map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared map with atomic per-key updates.
 *
 * merge(key, delta, Long::sum) reads, adds and writes back as one atomic step for that key.
 * The broken version is get() then put(): two threads read the same old value and one
 * update is lost.
 *
 * Balances are stored in paise (long) to avoid floating-point rounding with money.
 * Positive = the user is owed money, negative = the user owes money.
 */
public class SplitwiseBalances {
    private final Map<String, Long> balances = new ConcurrentHashMap<>();

    /** payer paid amount on behalf of debtor. */
    public void addExpense(String payer, String debtor, long amountInPaise) {
        // Two separate atomic updates. Each key is always correct, but a reader between
        // the two lines could briefly see one side updated and not the other. If you
        // need both to change together, that's the "several fields" case: use a lock.
        balances.merge(payer, amountInPaise, Long::sum);
        balances.merge(debtor, -amountInPaise, Long::sum);
    }

    public long balanceOf(String user) {
        return balances.getOrDefault(user, 0L);
    }

    public long total() {
        return balances.values().stream().mapToLong(Long::longValue).sum();
    }
}
