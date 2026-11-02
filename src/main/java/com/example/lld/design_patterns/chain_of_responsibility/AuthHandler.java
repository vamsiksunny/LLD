package com.example.lld.design_patterns.chain_of_responsibility;

public class AuthHandler extends BookingHandler {
    @Override
    protected boolean check(BookingRequest request) {
        if (!request.loggedIn()) {
            System.out.println("  rejected: user " + request.userId() + " is not logged in");
            return false;
        }
        System.out.println("  auth ok");
        return true;
    }
}
