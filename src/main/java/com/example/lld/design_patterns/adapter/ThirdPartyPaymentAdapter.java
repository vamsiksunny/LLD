package com.example.lld.design_patterns.adapter;

/**
 * Adapter: implements our interface and translates each call into the SDK's API.
 * All the conversion logic lives here, so the rest of the app never sees the SDK.
 */
public class ThirdPartyPaymentAdapter implements PaymentGateway {
    private final ThirdPartyPaymentSdk sdk;

    public ThirdPartyPaymentAdapter(ThirdPartyPaymentSdk sdk) {
        this.sdk = sdk;
    }

    @Override
    public boolean pay(String orderId, double amountInRupees) {
        long paise = Math.round(amountInRupees * 100);
        String status = sdk.makeTransaction(paise, "INR", orderId);
        return "SUCCESS".equals(status);
    }
}
