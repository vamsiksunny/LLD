package com.example.lld.design_patterns.decorator;

public class ReclinerUpgrade extends TicketAddOn {
    public ReclinerUpgrade(TicketOrder wrapped) {
        super(wrapped);
    }

    @Override
    public String description() {
        return wrapped.description() + " + recliner";
    }

    @Override
    public double cost() {
        return wrapped.cost() + 150;
    }
}
