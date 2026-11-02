package com.example.lld.concurrency.producer_consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Producer/consumer: request threads put() messages, a fixed set of worker threads take() them.
 *
 * - take() blocks when the queue is empty, so idle workers use no CPU.
 * - put() blocks when the queue is full (bounded), which slows producers down instead of
 *   letting memory grow without limit. That's back-pressure.
 * - Shutdown uses a "poison pill": one special message per worker that tells it to stop
 *   after everything queued ahead of it has been sent.
 */
public class NotificationSender {
    private static final String POISON_PILL = "__STOP__";

    private final BlockingQueue<String> queue;
    private final List<Thread> workers = new ArrayList<>();
    private final AtomicInteger sent = new AtomicInteger();

    public NotificationSender(int workerCount, int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
        for (int i = 0; i < workerCount; i++) {
            Thread worker = new Thread(this::workLoop, "notifier-" + i);
            workers.add(worker);
            worker.start();
        }
    }

    /** Producer side. Blocks if the queue is full. */
    public void submit(String message) throws InterruptedException {
        queue.put(message);
    }

    private void workLoop() {
        try {
            while (true) {
                String message = queue.take();
                if (POISON_PILL.equals(message)) {
                    return;
                }
                send(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void send(String message) {
        // A real sender would call an email/SMS API here
        sent.incrementAndGet();
    }

    /** Lets workers finish everything already queued, then stops them. */
    public void shutdown() throws InterruptedException {
        for (int i = 0; i < workers.size(); i++) {
            queue.put(POISON_PILL);
        }
        for (Thread worker : workers) {
            worker.join();
        }
    }

    public int sentCount() {
        return sent.get();
    }
}
