package com.example.lld.design_patterns.factory;

public class FactoryDemo {
    public static void main(String[] args) {
        for (NotificationType type : NotificationType.values()) {
            Notification notification = NotificationFactory.create(type);
            notification.send("vamsik", "Your booking is confirmed");
        }
    }
}
