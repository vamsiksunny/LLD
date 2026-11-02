package com.example.lld.design_patterns.observer;

public class AppSubscriber implements Subscriber {
    private final String userId;

    public AppSubscriber(String userId) {
        this.userId = userId;
    }

    @Override
    public void onMovieReleased(String movieName) {
        System.out.println("  in-app alert for " + userId + ": book " + movieName + " now");
    }
}
