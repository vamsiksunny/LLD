package com.example.lld.concurrency.traps;

import com.example.lld.concurrency.Race;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Trap: check-then-act is a race, even on a ConcurrentHashMap.
 *
 * Each call (containsKey, put) is thread-safe on its own, but the PAIR is not atomic.
 * Two threads can both see "not there", and both put. Fix: one atomic call,
 * putIfAbsent or computeIfAbsent.
 */
public class CheckThenActTrap {
    private static final Map<String, String> seatOwner = new ConcurrentHashMap<>();

    static boolean brokenBook(String seat, String user) {
        if (!seatOwner.containsKey(seat)) {   // check
            Race.pause(5);                     // any delay here (GC, context switch) opens the window
            seatOwner.put(seat, user);         // act: overwrites another thread's booking
            return true;
        }
        return false;
    }

    static boolean fixedBook(String seat, String user) {
        return seatOwner.putIfAbsent(seat, user) == null;
    }

    public static void main(String[] args) {
        AtomicInteger broken = new AtomicInteger();
        Race.run(10, i -> {
            if (brokenBook("A1", "user-" + i)) {
                broken.incrementAndGet();
            }
        });
        System.out.println("containsKey + put: users told 'booked' = " + broken.get() + "  <-- should be 1");

        seatOwner.clear();
        AtomicInteger fixed = new AtomicInteger();
        Race.run(10, i -> {
            if (fixedBook("A1", "user-" + i)) {
                fixed.incrementAndGet();
            }
        });
        System.out.println("putIfAbsent:       users told 'booked' = " + fixed.get());
    }
}
