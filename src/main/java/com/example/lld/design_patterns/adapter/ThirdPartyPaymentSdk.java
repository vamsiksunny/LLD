package com.example.lld.design_patterns.adapter;

/**
 * Adaptee: an external SDK we can't modify. Its API doesn't match PaymentGateway:
 * different method name, amount in paise (long), and a status string instead of a boolean.
 */
public class ThirdPartyPaymentSdk {
    public String makeTransaction(long amountInPaise, String currency, String referenceId) {
        System.out.println("  [SDK] charging " + amountInPaise + " paise (" + currency + ") for ref " + referenceId);
        return amountInPaise > 0 ? "SUCCESS" : "FAILED";
    }
}
