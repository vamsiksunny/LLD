package com.example.lld.design_patterns.factory;

/**
 * Product interface: callers only ever see this type, never the concrete classes.
 */
public interface Notification {
    void send(String to, String message);
}
