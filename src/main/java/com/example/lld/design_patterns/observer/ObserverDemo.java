package com.example.lld.design_patterns.observer;

public class ObserverDemo {
    public static void main(String[] args) {
        MovieReleasePublisher publisher = new MovieReleasePublisher();

        Subscriber email = new EmailSubscriber("vamsik@example.com");
        Subscriber app = new AppSubscriber("user-42");
        publisher.subscribe(email);
        publisher.subscribe(app);

        publisher.releaseMovie("Interstellar re-release");

        publisher.unsubscribe(email);
        publisher.releaseMovie("Dune: Part Three");   // only the app subscriber hears this
    }
}
