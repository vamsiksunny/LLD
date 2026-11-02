package com.example.lld.concurrency.traps;

/**
 * Trap: double-checked locking needs volatile.
 *
 * This class is BROKEN on purpose. "instance = new ...()" is not one step: the JVM may
 * publish the reference before the constructor has finished writing the fields.
 * A second thread passes the unsynchronized first check (instance != null), skips
 * the lock, and reads config before it's set: it can see null or 0.
 *
 * There's no runnable demo because this almost never shows up on x86 laptops (strong
 * memory model) but can on ARM servers and under aggressive JIT. That's exactly why it's
 * dangerous: it passes tests and fails in production.
 *
 * Fix: declare the field volatile (see concurrency.singleton.ParkingLot), or use the
 * holder idiom / an enum (see concurrency.singleton.BookingManager).
 */
public class DoubleCheckedLockingWithoutVolatileTrap {
    private static DoubleCheckedLockingWithoutVolatileTrap instance;   // missing volatile

    private final String config;

    private DoubleCheckedLockingWithoutVolatileTrap() {
        this.config = "loaded";
    }

    public static DoubleCheckedLockingWithoutVolatileTrap getInstance() {
        if (instance == null) {                     // unsynchronized read: may see a half-built object
            synchronized (DoubleCheckedLockingWithoutVolatileTrap.class) {
                if (instance == null) {
                    instance = new DoubleCheckedLockingWithoutVolatileTrap();
                }
            }
        }
        return instance;
    }

    public String getConfig() {
        return config;
    }
}
