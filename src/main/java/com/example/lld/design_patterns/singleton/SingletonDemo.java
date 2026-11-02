package com.example.lld.design_patterns.singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class SingletonDemo {
    private static final int THREADS = 10;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Eager:");
        System.out.println("  same instance: " + (EagerSingleton.getInstance() == EagerSingleton.getInstance()));

        System.out.println("Unsafe lazy (no locking), called from " + THREADS + " threads:");
        int unsafe = distinctInstances(UnsafeLazySingleton::getInstance);
        System.out.println("  distinct instances: " + unsafe
                + ", constructor ran " + UnsafeLazySingleton.CREATED.get() + " times  <-- broken, should be 1");

        System.out.println("Double-checked locking, called from " + THREADS + " threads:");
        System.out.println("  distinct instances: " + distinctInstances(DatabaseConnection::getInstance));
        DatabaseConnection.getInstance().query("SELECT * FROM shows");

        System.out.println("Holder idiom:");
        System.out.println("  same instance: " + (AppConfig.getInstance() == AppConfig.getInstance()));
        System.out.println("  max seats: " + AppConfig.getInstance().getMaxSeatsPerBooking());

        System.out.println("Enum singleton:");
        System.out.println("  ids: " + IdGenerator.INSTANCE.nextId() + ", " + IdGenerator.INSTANCE.nextId());
    }

    /** Calls getInstance from many threads released at the same moment, and counts distinct results. */
    private static int distinctInstances(Supplier<Object> getInstance) throws InterruptedException {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        CountDownLatch start = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            Thread t = new Thread(() -> {
                try {
                    start.await();
                    seen.add(getInstance.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads.add(t);
            t.start();
        }
        start.countDown();   // release all threads together
        for (Thread t : threads) {
            t.join();
        }
        return seen.size();
    }
}
