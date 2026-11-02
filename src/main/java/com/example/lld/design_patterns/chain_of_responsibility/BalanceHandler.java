package com.example.lld.design_patterns.chain_of_responsibility;

public class BalanceHandler extends BookingHandler {
    @Override
    protected boolean check(BookingRequest request) {
        if (request.walletBalance() < request.amount()) {
            System.out.println("  rejected: insufficient balance");
            return false;
        }
        System.out.println("  balance ok");
        return true;
    }
}
