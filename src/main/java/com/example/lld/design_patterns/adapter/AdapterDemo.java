package com.example.lld.design_patterns.adapter;

public class AdapterDemo {
    public static void main(String[] args) {
        // Application code depends only on PaymentGateway
        PaymentGateway gateway = new ThirdPartyPaymentAdapter(new ThirdPartyPaymentSdk());

        boolean ok = gateway.pay("order-101", 499.99);
        System.out.println("order-101 paid: " + ok);

        boolean failed = gateway.pay("order-102", 0);
        System.out.println("order-102 paid: " + failed);
    }
}
