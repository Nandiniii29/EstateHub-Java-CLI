
package com.estatehub.concurrency;

import java.util.concurrent.Callable;

public class InquiryCallable implements Callable<String> {

    private final int inquiryId;

    public InquiryCallable(int inquiryId) {
    if (inquiryId <= 0) {
        throw new IllegalArgumentException(
            "Inquiry ID must be greater than zero."
        );
    }

    this.inquiryId = inquiryId;
}

    @Override
    public String call() throws Exception {

        // Simulate inquiry processing.
        Thread.sleep(1000);

        return "Inquiry " + inquiryId + " processed successfully.";
    }
}
