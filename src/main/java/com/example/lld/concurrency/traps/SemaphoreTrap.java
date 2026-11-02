package com.example.lld.concurrency.traps;

import com.example.lld.concurrency.Race;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * Trap: a Semaphore limits HOW MANY threads get in, not WHICH item each one takes.
 *
 * Semaphore(3) for 3 parking spots lets exactly 3 cars in. But if each car then scans
 * for "the first free spot" with check-then-act, two cars can still pick the same spot.
 * Fix: hand out the items themselves atomically, e.g. queue.poll() (see concurrency.pool).
 * Then you often don't need the semaphore at all: an empty queue already means "full".
 */
public class SemaphoreTrap {
    static final int SPOTS = 3;

    static class BrokenLot {
        private final Semaphore permits = new Semaphore(SPOTS);
        private final boolean[] occupied = new boolean[SPOTS];

        int park() throws InterruptedException {
            permits.acquire();
            for (int i = 0; i < SPOTS; i++) {
                if (!occupied[i]) {        // check
                    Race.pause(5);
                    occupied[i] = true;    // act: another car may have taken it meanwhile
                    return i;
                }
            }
            permits.release();
            return -1;
        }
    }

    static class FixedLot {
        private final Queue<Integer> free = new ConcurrentLinkedQueue<>();

        FixedLot() {
            for (int i = 0; i < SPOTS; i++) {
                free.add(i);
            }
        }

        int park() {
            Integer spot = free.poll();   // atomic: each spot goes to exactly one car
            return spot == null ? -1 : spot;
        }
    }

    public static void main(String[] args) {
        BrokenLot broken = new BrokenLot();
        Set<Integer> brokenSpots = ConcurrentHashMap.newKeySet();
        Race.run(SPOTS, i -> {
            try {
                brokenSpots.add(broken.park());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        System.out.println("Semaphore + scan: " + SPOTS + " cars got in, but distinct spots = "
                + brokenSpots.size() + "  <-- should be " + SPOTS + " (cars share a spot)");

        FixedLot fixed = new FixedLot();
        Set<Integer> fixedSpots = ConcurrentHashMap.newKeySet();
        Race.run(SPOTS, i -> fixedSpots.add(fixed.park()));
        System.out.println("queue.poll():     distinct spots = " + fixedSpots.size());
    }
}
