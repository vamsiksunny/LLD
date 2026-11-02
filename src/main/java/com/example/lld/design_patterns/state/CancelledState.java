package com.example.lld.design_patterns.state;

public class CancelledState implements TicketState {
    @Override
    public void pay(Ticket ticket) {
        System.out.println("Ticket is cancelled, cannot pay.");
    }

    @Override
    public void cancel(Ticket ticket) {
        System.out.println("Ticket is already cancelled.");
    }

    @Override
    public String name() {
        return "CANCELLED";
    }
}
