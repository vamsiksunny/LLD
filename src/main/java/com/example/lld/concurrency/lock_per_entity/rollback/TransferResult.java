package com.example.lld.concurrency.lock_per_entity.rollback;

/**
 * Why a transfer did or didn't happen. More useful to callers than a plain boolean.
 */
public enum TransferResult {
    SUCCESS,
    SAME_ACCOUNT,
    INVALID_AMOUNT,
    INSUFFICIENT_FUNDS,
    SENDER_FROZEN,
    RECEIVER_FROZEN,
    RECEIVER_LIMIT_EXCEEDED,
    FAILED_AND_REFUNDED
}
