package com.example.lld.design_patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject: keeps a list of subscribers and notifies all of them on every event.
 * It doesn't know or care what each subscriber does with the notification.
 */
public class MovieReleasePublisher {
    private final List<Subscriber> subscribers = new ArrayList<>();

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void releaseMovie(String movieName) {
        System.out.println("Releasing: " + movieName);
        for (Subscriber subscriber : subscribers) {
            subscriber.onMovieReleased(movieName);
        }
    }
}
