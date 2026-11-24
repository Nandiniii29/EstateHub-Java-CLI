
package com.estatehub.concurrency;

public class InquiryExecutorDemo {

    public static void main(String[] args) {

        // Create an executor with 2 worker threads.
        InquiryExecutor executor = new InquiryExecutor(2);

        String[] inquiries = {
            "Property 101 - Visit request",
            "Property 102 - Price details",
            "Property 103 - Availability",
            "Property 104 - Location details",
            "Property 105 - Ownership details"
        };

        System.out.println("Submitting inquiries...\n");

        // Submit inquiries to the executor.
        executor.processInquiries(inquiries);

        // Shut down after submitting all tasks.
        executor.shutdown();

        System.out.println("\nAll executor tasks completed.");
    }
}
