package com.example.lld.design_patterns.singleton;

/**
 * Lazy singleton using the holder idiom (Bill Pugh).
 *
 * The JVM only loads Holder the first time getInstance() runs, and class loading
 * is thread-safe, so this is lazy and thread-safe without any synchronized code.
 */
public class AppConfig {
    private final int maxSeatsPerBooking = 10;

    private AppConfig() {
        System.out.println("  loading config (should happen once)");
    }

    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }

    public int getMaxSeatsPerBooking() {
        return maxSeatsPerBooking;
    }
}
