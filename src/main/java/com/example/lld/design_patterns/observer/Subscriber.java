package com.example.lld.design_patterns.observer;

/**
 * Observer interface: anything that wants to hear about new movie releases.
 */
public interface Subscriber {
    void onMovieReleased(String movieName);
}
