package com.example.lld.design_patterns.singleton;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * BROKEN on purpose: lazy singleton with no locking.
 *
 * Two threads can both see instance == null at the same moment, and both call new.
 * Fine in single-threaded code, wrong in multi-threaded code. Compare with
 * DatabaseConnection, which fixes this with double-checked locking.
 */
public class UnsafeLazySingleton {
    private static UnsafeLazySingleton instance;

    /** Counts constructor calls, so the demo can show the race. */
    public static final AtomicInteger CREATED = new AtomicInteger();

    private UnsafeLazySingleton() {
        CREATED.incrementAndGet();
        try {
            Thread.sleep(50);   // slow construction makes the race easy to hit
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static UnsafeLazySingleton getInstance() {
        if (instance == null) {          // several threads can pass this check together...
            instance = new UnsafeLazySingleton();   // ...and each creates its own instance
        }
        return instance;
    }
}
