package com.example.lld.design_patterns.singleton;

/**
 * Eager singleton: the instance is created when the class is loaded.
 *
 * + simplest version, thread-safe for free (class loading is thread-safe)
 * - created even if nobody ever calls getInstance(), so avoid it when construction is expensive
 */
public class EagerSingleton {
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() {
        System.out.println("  EagerSingleton created");
    }

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}
