package com.example.lld.design_patterns.strategy;

public class NetBankingPayment implements PaymentStrategy {
    private final String bankName;

    public NetBankingPayment(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using net banking via " + bankName);
    }
}
