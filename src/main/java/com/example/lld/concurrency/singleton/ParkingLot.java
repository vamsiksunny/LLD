package com.example.lld.concurrency.singleton;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe singleton with double-checked locking.
 *
 * Why volatile is required: "instance = new ParkingLot()" is really three steps:
 * allocate memory, run the constructor, assign the reference. Without volatile, the CPU
 * or JIT may reorder them so the reference is assigned before the constructor finishes.
 * Another thread then passes the first null check and uses a half-built object.
 * volatile forbids that reordering.
 */
public class ParkingLot {
    private static volatile ParkingLot instance;

    static final AtomicInteger CONSTRUCTED = new AtomicInteger();   // for the demo only

    private final int capacity;

    private ParkingLot() {
        CONSTRUCTED.incrementAndGet();
        this.capacity = 100;
    }

    public static ParkingLot getInstance() {
        ParkingLot local = instance;          // one volatile read on the fast path
        if (local == null) {
            synchronized (ParkingLot.class) {
                local = instance;
                if (local == null) {          // second check: another thread may have won while we waited
                    instance = local = new ParkingLot();
                }
            }
        }
        return local;
    }

    public int getCapacity() {
        return capacity;
    }
}
