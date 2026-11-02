package com.example.lld.concurrency.counter;

import com.example.lld.concurrency.Race;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CounterDemo {
    private static int unsafeCount = 0;   // plain int: count++ is read, add, write (3 steps)

    public static void main(String[] args) {
        int threads = 10;
        int perThread = 10_000;

        Race.run(threads, t -> {
            for (int i = 0; i < perThread; i++) {
                unsafeCount++;
            }
        });
        System.out.println("plain int++   : " + unsafeCount + " (expected " + threads * perThread
                + ", usually lower: updates got lost)");

        TicketIdGenerator ids = new TicketIdGenerator();
        Set<String> seen = ConcurrentHashMap.newKeySet();
        Race.run(threads, t -> {
            for (int i = 0; i < perThread; i++) {
                seen.add(ids.nextTicketId());
            }
        });
        System.out.println("AtomicLong    : issued " + ids.issued() + ", unique ids " + seen.size()
                + " (expected " + threads * perThread + " both)");
    }
}
