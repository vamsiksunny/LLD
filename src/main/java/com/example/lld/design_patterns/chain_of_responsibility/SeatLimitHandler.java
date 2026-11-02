package com.example.lld.design_patterns.chain_of_responsibility;

public class SeatLimitHandler extends BookingHandler {
    private static final int MAX_SEATS = 10;

    @Override
    protected boolean check(BookingRequest request) {
        int count = request.seats().size();
        if (count == 0 || count > MAX_SEATS) {
            System.out.println("  rejected: seat count must be 1-" + MAX_SEATS + ", got " + count);
            return false;
        }
        System.out.println("  seat count ok");
        return true;
    }
}
