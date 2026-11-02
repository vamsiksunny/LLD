package com.example.lld.concurrency.counter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A counter: incrementAndGet() is one atomic read-add-write, so every caller gets a
 * different ID. No lock needed.
 */
public class TicketIdGenerator {
    private final AtomicLong next = new AtomicLong();

    public String nextTicketId() {
        return "TKT-" + next.incrementAndGet();
    }

    public long issued() {
        return next.get();
    }
}
