package com.example.lld.design_patterns.chain_of_responsibility;

import java.util.List;

public record BookingRequest(String userId, boolean loggedIn, List<String> seats, double walletBalance, double amount) {
}
