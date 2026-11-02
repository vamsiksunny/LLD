package com.example.lld.concurrency.traps;

import com.example.lld.concurrency.Race;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Trap: synchronized on the whole service is correct but slow.
 *
 * Version 1 (fine to offer first in an interview): one lock for everything. Every
 * request waits, even for unrelated seats. Version 2: a lock per seat, so requests for
 * different seats run in parallel and only requests for the SAME seat wait.
 */
public class GlobalLockVsPerItemLockTrap {
    private static final long WORK_MS = 20;   // simulated DB write / payment call while holding the lock

    static class GlobalLockService {
        private final Set<String> booked = new HashSet<>();

        synchronized boolean book(String seat) {   // one lock for the whole service
            if (booked.contains(seat)) {
                return false;
            }
            Race.pause(WORK_MS);
            booked.add(seat);
            return true;
        }
    }

    static class PerSeatLockService {
        private final Set<String> booked = ConcurrentHashMap.newKeySet();
        private final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();

        boolean book(String seat) {
            // computeIfAbsent: every thread gets the SAME lock object for a given seat
            ReentrantLock lock = locks.computeIfAbsent(seat, k -> new ReentrantLock());
            lock.lock();
            try {
                if (booked.contains(seat)) {
                    return false;
                }
                Race.pause(WORK_MS);
                booked.add(seat);
                return true;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        int users = 10;   // each books a different seat

        GlobalLockService global = new GlobalLockService();
        long start = System.nanoTime();
        Race.run(users, i -> global.book("S" + i));
        long globalMs = (System.nanoTime() - start) / 1_000_000;

        PerSeatLockService perSeat = new PerSeatLockService();
        start = System.nanoTime();
        Race.run(users, i -> perSeat.book("S" + i));
        long perSeatMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println(users + " users booking different seats, " + WORK_MS + " ms of work each:");
        System.out.println("  one global lock: " + globalMs + " ms (runs one at a time, about " + users * WORK_MS + ")");
        System.out.println("  lock per seat:   " + perSeatMs + " ms (runs in parallel, about " + WORK_MS + ")");
    }
}
