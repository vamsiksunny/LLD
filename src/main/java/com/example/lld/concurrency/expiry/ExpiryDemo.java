package com.example.lld.concurrency.expiry;

import com.example.lld.concurrency.Race;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class ExpiryDemo {
    public static void main(String[] args) {
        // Production would use Duration.ofMinutes(10); 300 ms keeps the demo fast
        try (SeatHoldService service = new SeatHoldService(Duration.ofMillis(300))) {
            System.out.println("Seat holds:");
            AtomicInteger holders = new AtomicInteger();
            Race.run(10, i -> {
                if (service.hold("A1", "user-" + i)) {
                    holders.incrementAndGet();
                }
            });
            System.out.println("  users who got the hold on A1 = " + holders.get() + " (expected 1)");

            service.hold("B1", "alice");
            System.out.println("  alice confirms B1 in time: " + service.confirm("B1", "alice"));

            Race.pause(500);   // let A1's hold expire
            System.out.println("  A1 available again after expiry: " + service.isAvailable("A1"));
            System.out.println("  B1 available (booked, not expired): " + service.isAvailable("B1"));

            service.hold("C1", "bob");
            Race.pause(500);
            System.out.println("  bob confirms C1 too late: " + service.confirm("C1", "bob"));
        }

        try (TtlKeyValueStore<String, String> store = new TtlKeyValueStore<>(Duration.ofMillis(200))) {
            System.out.println("TTL store:");
            store.put("session-1", "alice", Duration.ofMillis(100));
            store.put("session-2", "bob", Duration.ofSeconds(10));
            System.out.println("  right away: session-1 = " + store.get("session-1"));

            Race.pause(500);   // past the TTL, and at least one sweep has run
            System.out.println("  after 500 ms: session-1 = " + store.get("session-1")
                    + ", session-2 = " + store.get("session-2")
                    + ", entries stored = " + store.storedSize() + " (expected null, bob, 1)");
        }
    }
}
