package com.example.lld.design_patterns.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Enum singleton: the approach Effective Java recommends.
 * The JVM guarantees exactly one instance, and it can't be broken by
 * reflection or serialization (the other two versions can be).
 */
public enum IdGenerator {
    INSTANCE;

    private final AtomicLong counter = new AtomicLong();

    public long nextId() {
        return counter.incrementAndGet();
    }
}
