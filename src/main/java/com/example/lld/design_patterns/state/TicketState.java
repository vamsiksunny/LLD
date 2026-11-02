package com.example.lld.design_patterns.state;

/**
 * State interface: each state decides how the ticket reacts to an action
 * and which state comes next.
 */
public interface TicketState {
    void pay(Ticket ticket);

    void cancel(Ticket ticket);

    String name();
}
