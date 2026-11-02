package com.example.lld.concurrency.pool;

import com.example.lld.concurrency.Race;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolDemo {
    public static void main(String[] args) {
        ParkingSpotPool pool = new ParkingSpotPool(Map.of(VehicleType.CAR, 5, VehicleType.BIKE, 3));

        Set<String> taken = ConcurrentHashMap.newKeySet();
        AtomicInteger turnedAway = new AtomicInteger();

        // 12 cars race for 5 car spots
        Race.run(12, i -> pool.park(VehicleType.CAR).ifPresentOrElse(
                spot -> {
                    if (!taken.add(spot)) {
                        throw new IllegalStateException("spot given twice: " + spot);
                    }
                },
                turnedAway::incrementAndGet));

        System.out.println("cars parked = " + taken.size() + ", turned away = " + turnedAway.get()
                + ", free car spots = " + pool.freeCount(VehicleType.CAR) + "  (expected 5, 7, 0)");
        System.out.println("free bike spots untouched = " + pool.freeCount(VehicleType.BIKE));

        String spot = taken.iterator().next();
        pool.leave(VehicleType.CAR, spot);
        System.out.println("after " + spot + " leaves, free car spots = " + pool.freeCount(VehicleType.CAR));
    }
}
