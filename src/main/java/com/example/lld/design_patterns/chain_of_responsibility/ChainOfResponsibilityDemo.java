package com.example.lld.design_patterns.chain_of_responsibility;

import java.util.List;

public class ChainOfResponsibilityDemo {
    public static void main(String[] args) {
        BookingHandler chain = new AuthHandler();
        chain.setNext(new SeatLimitHandler())
             .setNext(new BalanceHandler());

        List<BookingRequest> requests = List.of(
                new BookingRequest("u1", true, List.of("A1", "A2"), 1000, 500),
                new BookingRequest("u2", false, List.of("B1"), 1000, 250),
                new BookingRequest("u3", true, List.of("C1", "C2", "C3"), 100, 750)
        );

        for (BookingRequest request : requests) {
            System.out.println("Request from " + request.userId() + ":");
            boolean approved = chain.handle(request);
            System.out.println(approved ? "  -> BOOKED" : "  -> FAILED");
        }
    }
}
