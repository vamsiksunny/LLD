package com.example.lld.concurrency.producer_consumer;

import com.example.lld.concurrency.Race;

public class ProducerConsumerDemo {
    public static void main(String[] args) throws InterruptedException {
        // 3 workers, queue holds at most 50 messages at a time
        NotificationSender sender = new NotificationSender(3, 50);

        // 5 producer threads each submit 200 notifications
        Race.run(5, p -> {
            for (int i = 0; i < 200; i++) {
                try {
                    sender.submit("booking confirmed #" + p + "-" + i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        sender.shutdown();
        System.out.println("sent = " + sender.sentCount() + " (expected 1000: nothing lost, nothing sent twice)");
    }
}
