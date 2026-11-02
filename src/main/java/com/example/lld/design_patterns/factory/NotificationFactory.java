package com.example.lld.design_patterns.factory;

/**
 * Factory: the only place that knows which concrete class to instantiate.
 * Adding a new channel means touching this class, not every caller.
 */
public class NotificationFactory {
    private NotificationFactory() {
    }

    public static Notification create(NotificationType type) {
        return switch (type) {
            case EMAIL -> new EmailNotification();
            case SMS -> new SmsNotification();
            case PUSH -> new PushNotification();
        };
    }
}
