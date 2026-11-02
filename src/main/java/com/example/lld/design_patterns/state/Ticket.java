package com.example.lld.design_patterns.state;

import lombok.Getter;

/**
 * Context: forwards every action to its current state object.
 * There is no if/else or switch on status anywhere in this class.
 */
public class Ticket {
    @Getter
    private TicketState state = new PendingState();

    void setState(TicketState state) {
        System.out.println("  state: " + this.state.name() + " -> " + state.name());
        this.state = state;
    }

    public void pay() {
        state.pay(this);
    }

    public void cancel() {
        state.cancel(this);
    }
}
