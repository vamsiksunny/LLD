package com.example.lld.concurrency.traps;

import com.example.lld.concurrency.Race;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Trap: volatile gives visibility, not atomicity.
 *
 * volatile guarantees every thread sees the latest written value of isBooked.
 * It does NOT make "read it, then write it" one step, so two threads can both read
 * false before either writes true. Fix: AtomicBoolean.compareAndSet(false, true).
 */
public class VolatileIsNotAtomicTrap {
    static class BrokenSeat {
        private volatile boolean isBooked = false;

        boolean tryBook() {
            if (!isBooked) {
                Race.pause(5);
                isBooked = true;
                return true;
            }
            return false;
        }
    }

    static class FixedSeat {
        private final AtomicBoolean booked = new AtomicBoolean(false);

        boolean tryBook() {
            return booked.compareAndSet(false, true);
        }
    }

    public static void main(String[] args) {
        BrokenSeat broken = new BrokenSeat();
        AtomicInteger brokenWins = new AtomicInteger();
        Race.run(10, i -> {
            if (broken.tryBook()) {
                brokenWins.incrementAndGet();
            }
        });
        System.out.println("volatile boolean: winners = " + brokenWins.get() + "  <-- should be 1");

        FixedSeat fixed = new FixedSeat();
        AtomicInteger fixedWins = new AtomicInteger();
        Race.run(10, i -> {
            if (fixed.tryBook()) {
                fixedWins.incrementAndGet();
            }
        });
        System.out.println("AtomicBoolean CAS: winners = " + fixedWins.get());
    }
}
