package com.example.lld.design_patterns.singleton;

/**
 * Lazy singleton with double-checked locking.
 *
 * - private constructor: nobody else can call new
 * - volatile: other threads never see a half-constructed instance
 * - two null checks: the lock is only taken on the first call, not every call
 */
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;

    private DatabaseConnection() {
        System.out.println("  opening database connection (should happen once)");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public void query(String sql) {
        System.out.println("  running: " + sql);
    }
}
