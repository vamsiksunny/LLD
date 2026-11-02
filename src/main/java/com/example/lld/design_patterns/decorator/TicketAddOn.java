package com.example.lld.design_patterns.decorator;

/**
 * Base decorator: wraps another TicketOrder and, by default, just delegates to it.
 * Subclasses add their own description and cost on top of the wrapped order.
 */
public abstract class TicketAddOn implements TicketOrder {
    protected final TicketOrder wrapped;

    protected TicketAddOn(TicketOrder wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String description() {
        return wrapped.description();
    }

    @Override
    public double cost() {
        return wrapped.cost();
    }
}
