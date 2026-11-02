package com.example.lld.concurrency.claim_once;

import com.example.lld.concurrency.Race;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClaimOnceDemo {
    public static void main(String[] args) {
        // 1. CAS per seat: 20 users race for 5 seats
        List<Seat> seats = List.of(
                new Seat("A1"),
                new Seat("A2"),
                new Seat("A3"),
                new Seat("A4"),
                new Seat("A5"));
        ShowSeating seating = new ShowSeating(seats);
        Set<String> booked = ConcurrentHashMap.newKeySet();
        AtomicInteger soldOut = new AtomicInteger();

        Race.run(20, i -> seating.bookAnySeat().ifPresentOrElse(
                seat -> {
                    if (!booked.add(seat.getId())) {
                        throw new IllegalStateException("double booked " + seat.getId());
                    }
                },
                soldOut::incrementAndGet));

        System.out.println("AtomicBoolean CAS: seats booked = " + booked.size()
                + ", users told sold out = " + soldOut.get() + "  (expected 5 and 15, no double booking)");

        // 2. putIfAbsent: 10 users race for the same seat
        AtomicInteger winners = new AtomicInteger();
        Race.run(10, i -> {
            if (seating.bookSeatFor("VIP-1", "user-" + i)) {
                winners.incrementAndGet();
            }
        });
        System.out.println("putIfAbsent: winners = " + winners.get()
                + " (expected 1), owner = " + seating.ownerOf("VIP-1"));
    }
}
