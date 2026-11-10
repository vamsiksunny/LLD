package com.example.lld.concurrency.lock_per_entity.rollback;

/**
 * Pretend outside service (a UPI wallet, another bank). We can't check in advance
 * whether it will succeed: it may be down, time out or reject the request.
 * That's the case where validate-first isn't enough and we need a compensating refund.
 */
public class ExternalWallet {
    private volatile boolean down;
    private long received;

    public void setDown(boolean down) {
        this.down = down;
    }

    public synchronized void deposit(long fromAccountId, long amount) {
        if (down) {
            throw new IllegalStateException("wallet service unavailable");
        }
        received += amount;
    }

    public synchronized long getReceived() {
        return received;
    }
}
