package com.example.lld.design_patterns.decorator;

public class DecoratorDemo {
    public static void main(String[] args) {
        TicketOrder plain = new BasicTicket();
        print(plain);

        // Wrap layer by layer; each wrapper adds behaviour without subclassing BasicTicket
        TicketOrder loaded = new ConvenienceFee(new Popcorn(new Popcorn(new ReclinerUpgrade(new BasicTicket()))));
        print(loaded);
    }

    private static void print(TicketOrder order) {
        System.out.printf("%s = %.2f%n", order.description(), order.cost());
    }
}
