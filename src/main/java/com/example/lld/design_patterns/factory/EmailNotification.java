package com.example.lld.design_patterns.factory;

public class EmailNotification implements Notification {
    @Override
    public void send(String to, String message) {
        System.out.println("EMAIL to " + to + ": " + message);
    }
}
