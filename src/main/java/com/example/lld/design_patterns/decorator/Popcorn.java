package com.example.lld.design_patterns.decorator;

public class Popcorn extends TicketAddOn {
    public Popcorn(TicketOrder wrapped) {
        super(wrapped);
    }

    @Override
    public String description() {
        return wrapped.description() + " + popcorn";
    }

    @Override
    public double cost() {
        return wrapped.cost() + 120;
    }
}
