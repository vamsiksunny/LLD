package com.example.lld.design_patterns.strategy;

/**
 * Strategy interface: one family of interchangeable algorithms (here, ways to pay).
 */
public interface PaymentStrategy {
    void pay(double amount);
}
