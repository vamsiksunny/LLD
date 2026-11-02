package com.example.lld.design_patterns.chain_of_responsibility;

/**
 * Base handler: each handler either rejects the request or passes it to the next one.
 * The sender only talks to the first handler and doesn't know how long the chain is.
 */
public abstract class BookingHandler {
    private BookingHandler next;

    /** Returns the handler passed in, so calls can be chained: a.setNext(b).setNext(c). */
    public BookingHandler setNext(BookingHandler next) {
        this.next = next;
        return next;
    }

    public boolean handle(BookingRequest request) {
        if (!check(request)) {
            return false;
        }
        return next == null || next.handle(request);
    }

    protected abstract boolean check(BookingRequest request);
}
