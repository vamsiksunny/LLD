package com.example.lld.design_patterns.factory;

public class PushNotification implements Notification {
    @Override
    public void send(String to, String message) {
        System.out.println("PUSH to device " + to + ": " + message);
    }
}
