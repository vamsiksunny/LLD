package com.example.lld.concurrency.singleton;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Same guarantee with less code: the holder class idiom.
 * Holder isn't loaded until getInstance() first touches it, and the JVM runs a
 * class's static initializer exactly once, thread-safely. No volatile, no synchronized.
 *
 * Third option: "enum BookingManager { INSTANCE; }", which is also safe against
 * reflection and serialization.
 */
public class BookingManager {
    static final AtomicInteger CONSTRUCTED = new AtomicInteger();   // for the demo only

    private BookingManager() {
        CONSTRUCTED.incrementAndGet();
    }

    private static class Holder {
        private static final BookingManager INSTANCE = new BookingManager();
    }

    public static BookingManager getInstance() {
        return Holder.INSTANCE;
    }
}
