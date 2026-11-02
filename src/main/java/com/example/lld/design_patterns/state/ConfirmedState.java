package com.example.lld.design_patterns.state;

public class ConfirmedState implements TicketState {
    @Override
    public void pay(Ticket ticket) {
        System.out.println("Already paid, ignoring.");
    }

    @Override
    public void cancel(Ticket ticket) {
        System.out.println("Cancelled, refund initiated.");
        ticket.setState(new CancelledState());
    }

    @Override
    public String name() {
        return "CONFIRMED";
    }
}
