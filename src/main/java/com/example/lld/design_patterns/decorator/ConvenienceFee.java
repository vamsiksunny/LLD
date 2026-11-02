package com.example.lld.design_patterns.decorator;

public class ConvenienceFee extends TicketAddOn {
    private static final double RATE = 0.05;

    public ConvenienceFee(TicketOrder wrapped) {
        super(wrapped);
    }

    @Override
    public String description() {
        return wrapped.description() + " + 5% convenience fee";
    }

    @Override
    public double cost() {
        return wrapped.cost() * (1 + RATE);
    }
}
