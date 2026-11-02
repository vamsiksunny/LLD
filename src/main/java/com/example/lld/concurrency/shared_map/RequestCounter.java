package com.example.lld.concurrency.shared_map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Counter per user.
 *
 * computeIfAbsent creates the user's counter exactly once, even if many threads ask at
 * the same moment. LongAdder then handles high-contention increments faster than
 * AtomicLong, because it spreads updates across cells and sums them on read.
 */
public class RequestCounter {
    private final Map<String, LongAdder> counts = new ConcurrentHashMap<>();

    public void record(String userId) {
        counts.computeIfAbsent(userId, k -> new LongAdder()).increment();
    }

    public long countFor(String userId) {
        LongAdder adder = counts.get(userId);
        return adder == null ? 0 : adder.sum();
    }

    /**
     * compute: replace the value with a function of the old value, atomically per key.
     * Here it resets a user's counter and returns the count it had.
     *
     * Caveat: a record() that already fetched the old LongAdder can still increment it
     * just after the swap, and that one hit is lost. If resets must be exact, store an
     * immutable Long and update it with merge(userId, 1L, Long::sum) instead.
     */
    public long reset(String userId) {
        long[] previous = new long[1];
        counts.compute(userId, (k, adder) -> {
            previous[0] = adder == null ? 0 : adder.sum();
            return new LongAdder();
        });
        return previous[0];
    }
}
