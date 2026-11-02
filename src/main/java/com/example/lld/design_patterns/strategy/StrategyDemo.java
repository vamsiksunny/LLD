package com.example.lld.design_patterns.strategy;

public class StrategyDemo {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor(new CreditCardPayment("4111111111111234"));
        processor.checkout(500.0);

        // Swap the algorithm at runtime without changing PaymentProcessor
        processor.setStrategy(new UpiPayment("vamsik@upi"));
        processor.checkout(250.0);

        processor.setStrategy(new NetBankingPayment("HDFC"));
        processor.checkout(1000.0);

        // Strategy is a functional interface, so a lambda works too
        processor.setStrategy(amount -> System.out.println("Paid " + amount + " in cash"));
        processor.checkout(100.0);
    }
}
