package com.example.lld.design_patterns.state;

public class PendingState implements TicketState {
    @Override
    public void pay(Ticket ticket) {
        System.out.println("Payment received.");
        ticket.setState(new ConfirmedState());
    }

    @Override
    public void cancel(Ticket ticket) {
        System.out.println("Seat hold released.");
        ticket.setState(new CancelledState());
    }

    @Override
    public String name() {
        return "PENDING";
    }
}
