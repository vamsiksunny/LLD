package com.example.lld.concurrency.claim_once;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Claim an item exactly once with compare-and-set.
 * compareAndSet(false, true) checks and flips the flag in one atomic step,
 * so if 100 threads call tryBook() at once, exactly one gets true.
 */
public class Seat {
    private final String id;
    private final AtomicBoolean booked = new AtomicBoolean(false);

    public Seat(String id) {
        this.id = id;
    }

    public boolean tryBook() {
        return booked.compareAndSet(false, true);
    }

    public void release() {
        booked.set(false);
    }

    public String getId() {
        return id;
    }
}
