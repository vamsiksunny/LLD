package com.example.lld.concurrency.shared_map;

import com.example.lld.concurrency.Race;

public class SharedMapDemo {
    public static void main(String[] args) {
        SplitwiseBalances balances = new SplitwiseBalances();

        // 10 threads each record 1,000 expenses where alice pays 100 paise for bob
        Race.run(10, t -> {
            for (int i = 0; i < 1_000; i++) {
                balances.addExpense("alice", "bob", 100);
            }
        });
        System.out.println("alice = " + balances.balanceOf("alice") + ", bob = " + balances.balanceOf("bob")
                + ", total = " + balances.total() + "  (expected 1000000, -1000000, 0)");

        RequestCounter counter = new RequestCounter();
        // 8 threads, each sends 500 requests for user-1 and 500 for user-2
        Race.run(8, t -> {
            for (int i = 0; i < 500; i++) {
                counter.record("user-1");
                counter.record("user-2");
            }
        });
        System.out.println("user-1 = " + counter.countFor("user-1") + ", user-2 = " + counter.countFor("user-2")
                + "  (expected 4000 each)");
        System.out.println("reset user-1, it had " + counter.reset("user-1") + ", now " + counter.countFor("user-1"));
    }
}
