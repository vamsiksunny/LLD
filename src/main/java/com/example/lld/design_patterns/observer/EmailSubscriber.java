package com.example.lld.design_patterns.observer;

public class EmailSubscriber implements Subscriber {
    private final String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void onMovieReleased(String movieName) {
        System.out.println("  email to " + email + ": " + movieName + " is now in theatres");
    }
}
