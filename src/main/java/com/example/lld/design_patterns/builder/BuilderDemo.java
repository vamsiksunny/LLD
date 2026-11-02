package com.example.lld.design_patterns.builder;

import java.time.LocalDateTime;

public class BuilderDemo {
    public static void main(String[] args) {
        LocalDateTime evening = LocalDateTime.of(2026, 11, 7, 19, 30);

        // Only required fields; optional ones get defaults
        Show basic = Show.builder("Inception", evening).build();
        System.out.println(basic);

        // Set just the optional fields you need, in any order, by name
        Show imax = Show.builder("Oppenheimer", evening.plusHours(3))
                .format("IMAX")
                .subtitles(true)
                .language("Hindi")
                .build();
        System.out.println(imax);

        try {
            Show.builder(" ", evening).build();
        } catch (IllegalStateException e) {
            System.out.println("Build failed: " + e.getMessage());
        }
    }
}
