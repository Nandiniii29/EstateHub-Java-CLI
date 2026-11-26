
package com.estatehub.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InquiryExecutor {

    private final ExecutorService executor;

    public InquiryExecutor(int numberOfThreads) {
    if (numberOfThreads <= 0) {
        throw new IllegalArgumentException(
            "Number of threads must be greater than zero."
        );
    }

    executor = Executors.newFixedThreadPool(numberOfThreads);
}

    // Submit multiple inquiries for processing.
    public void processInquiries(String[] inquiries) {

        for (String inquiry : inquiries) {

            executor.execute(new Runnable() {

                @Override
                public void run() {
                    System.out.println(
                        Thread.currentThread().getName()
                        + " is processing: " + inquiry
                    );
                }
            });
        }
    }

    // Stop the executor safely.
    public void shutdown() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
