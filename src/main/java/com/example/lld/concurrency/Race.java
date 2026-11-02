package com.example.lld.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.IntConsumer;

/**
 * Demo helper: starts N threads, holds them at a gate, then releases them all at once
 * so they really do hit the shared data at the same moment. Waits for all to finish.
 */
public final class Race {
    private Race() {
    }

    /** Runs task(threadIndex) on {@code threads} threads released together. */
    public static void run(int threads, IntConsumer task) {
        CountDownLatch gate = new CountDownLatch(1);
        List<Thread> started = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            int index = i;
            Thread t = new Thread(() -> {
                try {
                    gate.await();
                    task.accept(index);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            started.add(t);
            t.start();
        }
        gate.countDown();
        for (Thread t : started) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /** Sleeps briefly. Used inside deliberately broken code to widen a race window. */
    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
