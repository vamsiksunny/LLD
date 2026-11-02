package com.example.lld.design_patterns.adapter;

/**
 * Target interface: what our application code expects to call.
 */
public interface PaymentGateway {
    boolean pay(String orderId, double amountInRupees);
}
