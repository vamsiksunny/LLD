package com.example.lld.design_patterns.decorator;

/**
 * Component interface: both the plain ticket and every add-on wrapper implement it.
 */
public interface TicketOrder {
    String description();

    double cost();
}
