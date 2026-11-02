package com.example.lld.concurrency.pool;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A pool of free items: one lock-free queue of free spots per vehicle type.
 *
 * poll() removes and returns the head atomically, so two threads can never get the
 * same spot, and nobody has to scan for a free one. The queue's size is the live
 * free capacity. Use a BlockingQueue instead if callers should wait for a spot.
 */
public class ParkingSpotPool {
    private final Map<VehicleType, Queue<String>> freeSpots = new EnumMap<>(VehicleType.class);

    public ParkingSpotPool(Map<VehicleType, Integer> capacity) {
        // The EnumMap itself is built once here and never modified, so it's safe to share.
        for (VehicleType type : VehicleType.values()) {
            Queue<String> queue = new ConcurrentLinkedQueue<>();
            for (int i = 1; i <= capacity.getOrDefault(type, 0); i++) {
                queue.add(type + "-" + i);
            }
            freeSpots.put(type, queue);
        }
    }

    /** Returns a spot, or empty if this type is full. */
    public Optional<String> park(VehicleType type) {
        return Optional.ofNullable(freeSpots.get(type).poll());
    }

    public void leave(VehicleType type, String spotId) {
        freeSpots.get(type).offer(spotId);
    }

    /** Note: size() on ConcurrentLinkedQueue is O(n); fine for a demo or small lots. */
    public int freeCount(VehicleType type) {
        return freeSpots.get(type).size();
    }
}
