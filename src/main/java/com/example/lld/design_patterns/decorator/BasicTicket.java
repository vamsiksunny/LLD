package com.example.lld.design_patterns.decorator;

public class BasicTicket implements TicketOrder {
    @Override
    public String description() {
        return "Movie ticket";
    }

    @Override
    public double cost() {
        return 200;
    }
}
