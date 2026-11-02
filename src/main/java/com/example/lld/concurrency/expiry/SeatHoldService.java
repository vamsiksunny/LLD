package com.example.lld.concurrency.expiry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Seat hold that expires: a user holds a seat while paying; if they don't confirm
 * within the hold time (10 min in production), the seat is released automatically.
 *
 * Each seat has ONE entry in ONE map, and every state change is a single atomic
 * conditional operation on it:
 *   free  -> held    putIfAbsent(seat, hold)
 *   held  -> booked  replace(seat, hold, booked)   (confirm)
 *   held  -> free    remove(seat, hold)            (expiry)
 *
 * The expiry task and confirm() may run at the same moment. Both are conditional on the
 * exact same Hold object, so only one can succeed. Using two maps (holds + booked) would
 * leave a gap between "remove hold" and "add booking" where the seat looks free.
 */
public class SeatHoldService implements AutoCloseable {
    private sealed interface SeatState permits Hold, Booked {
    }

    /** holdNumber makes each hold unique, so an old expiry task can't remove a newer hold. */
    private record Hold(String userId, long holdNumber) implements SeatState {
    }

    private record Booked(String userId) implements SeatState {
    }

    private final Map<String, SeatState> seats = new ConcurrentHashMap<>();
    private final AtomicLong holdNumbers = new AtomicLong();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Duration holdTime;

    public SeatHoldService(Duration holdTime) {
        this.holdTime = holdTime;
    }

    public boolean hold(String seatId, String userId) {
        Hold hold = new Hold(userId, holdNumbers.incrementAndGet());
        if (seats.putIfAbsent(seatId, hold) != null) {
            return false;   // already held or booked
        }
        scheduler.schedule(() -> {
            if (seats.remove(seatId, hold)) {   // only if it's still this exact hold
                System.out.println("  hold on " + seatId + " by " + userId + " expired");
            }
        }, holdTime.toMillis(), TimeUnit.MILLISECONDS);
        return true;
    }

    public boolean confirm(String seatId, String userId) {
        if (seats.get(seatId) instanceof Hold hold && hold.userId().equals(userId)) {
            // fails if the hold expired between get() and replace()
            return seats.replace(seatId, hold, new Booked(userId));
        }
        return false;   // never held, held by someone else, already booked, or expired
    }

    public boolean isAvailable(String seatId) {
        return !seats.containsKey(seatId);
    }

    @Override
    public void close() {
        scheduler.shutdownNow();
    }
}
