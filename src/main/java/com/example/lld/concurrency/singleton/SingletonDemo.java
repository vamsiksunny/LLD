package com.example.lld.concurrency.singleton;

import com.example.lld.concurrency.Race;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonDemo {
    public static void main(String[] args) {
        Set<ParkingLot> lots = ConcurrentHashMap.newKeySet();
        Race.run(50, i -> lots.add(ParkingLot.getInstance()));
        System.out.println("Double-checked locking: distinct = " + lots.size()
                + ", constructor calls = " + ParkingLot.CONSTRUCTED.get() + " (expected 1, 1)");

        Set<BookingManager> managers = ConcurrentHashMap.newKeySet();
        Race.run(50, i -> managers.add(BookingManager.getInstance()));
        System.out.println("Holder idiom:           distinct = " + managers.size()
                + ", constructor calls = " + BookingManager.CONSTRUCTED.get() + " (expected 1, 1)");
    }
}
