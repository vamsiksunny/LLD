package com.example.lld.design_patterns.strategy;

import lombok.Setter;

/**
 * Context: holds a reference to a strategy and delegates the work to it.
 * It never needs to know which concrete strategy it is using.
 */
@Setter
public class PaymentProcessor {
    private PaymentStrategy strategy;

    public PaymentProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void checkout(double amount) {
        strategy.pay(amount);
    }
}
