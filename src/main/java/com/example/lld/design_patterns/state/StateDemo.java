package com.example.lld.design_patterns.state;

public class StateDemo {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        ticket.pay();     // PENDING -> CONFIRMED
        ticket.pay();     // ignored
        ticket.cancel();  // CONFIRMED -> CANCELLED
        ticket.pay();     // rejected

        System.out.println("Final state: " + ticket.getState().name());
    }
}
