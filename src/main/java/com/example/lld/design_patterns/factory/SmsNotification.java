package com.example.lld.design_patterns.factory;

public class SmsNotification implements Notification {
    @Override
    public void send(String to, String message) {
        System.out.println("SMS to " + to + ": " + message);
    }
}
