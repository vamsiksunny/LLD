package com.example.lld.concurrency.expiry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Key-value store with TTL, using two kinds of expiry together:
 * - lazy: get() treats an expired entry as missing, so readers never see stale data
 * - active: a scheduled sweep removes expired entries nobody reads, so memory doesn't grow
 *
 * remove(key, entry) only deletes that exact entry, so a sweep can't delete a value
 * that was overwritten with a fresh TTL after the sweep looked at it.
 */
public class TtlKeyValueStore<K, V> implements AutoCloseable {
    private record Entry<V>(V value, long expiresAtNanos) {
        boolean isExpired(long now) {
            return now - expiresAtNanos >= 0;
        }
    }

    private final Map<K, Entry<V>> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public TtlKeyValueStore(Duration cleanupInterval) {
        long ms = cleanupInterval.toMillis();
        cleaner.scheduleAtFixedRate(this::sweep, ms, ms, TimeUnit.MILLISECONDS);
    }

    public void put(K key, V value, Duration ttl) {
        map.put(key, new Entry<>(value, System.nanoTime() + ttl.toNanos()));
    }

    public V get(K key) {
        Entry<V> entry = map.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired(System.nanoTime())) {
            map.remove(key, entry);
            return null;
        }
        return entry.value();
    }

    /** Number of entries physically stored, including expired ones the sweep hasn't removed yet. */
    public int storedSize() {
        return map.size();
    }

    private void sweep() {
        long now = System.nanoTime();
        map.forEach((key, entry) -> {
            if (entry.isExpired(now)) {
                map.remove(key, entry);
            }
        });
    }

    @Override
    public void close() {
        cleaner.shutdownNow();
    }
}
