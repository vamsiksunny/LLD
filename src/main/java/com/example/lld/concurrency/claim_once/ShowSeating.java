package com.example.lld.concurrency.claim_once;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Two ways to claim exactly once:
 * 1. Per-item flag: Seat.tryBook() (AtomicBoolean CAS). The loser tries the next seat.
 * 2. Ownership map: putIfAbsent(seatId, userId) returns null only for the thread that won,
 *    and also records who owns the seat.
 */
public class ShowSeating {
    private final List<Seat> seats;
    private final Map<String, String> owners = new ConcurrentHashMap<>();

    public ShowSeating(List<Seat> seats) {
        this.seats = seats;
    }

    /** Takes the first free seat. If another thread wins a seat's CAS, move on to the next one. */
    public Optional<Seat> bookAnySeat() {
        for (Seat seat : seats) {
            if (seat.tryBook()) {
                return Optional.of(seat);
            }
        }
        return Optional.empty();   // sold out
    }

    /** Books a specific seat for a user. putIfAbsent is atomic: only one caller sees null. */
    public boolean bookSeatFor(String seatId, String userId) {
        return owners.putIfAbsent(seatId, userId) == null;
    }

    public String ownerOf(String seatId) {
        return owners.get(seatId);
    }
}
