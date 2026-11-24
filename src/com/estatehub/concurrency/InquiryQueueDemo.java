
package com.estatehub.concurrency;

public class InquiryQueueDemo {

    public static void main(String[] args) {

        // Create a queue with maximum capacity of 2.
        InquiryQueue queue = new InquiryQueue(2);

        String[] customerInquiries = {
            "Inquiry 1: Property 101 - Request for visit",
            "Inquiry 2: Property 102 - Price details",
            "Inquiry 3: Property 103 - Availability",
            "Inquiry 4: Property 104 - Location details"
        };

        // Create producer and consumer threads.
        Thread producer = new Thread(
            new InquiryProducer(queue, customerInquiries)
        );

        Thread consumer = new Thread(
            new InquiryConsumer(queue)
        );

        // Start both threads.
        consumer.start();
        producer.start();

        try {
            // Wait until producer finishes adding inquiries.
            producer.join();

            // Close queue after production is complete.
            queue.close();

            // Wait until consumer processes remaining inquiries.
            consumer.join();

            System.out.println("\nAll inquiries processed successfully.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            queue.close();
            System.out.println("Main thread was interrupted.");
        }
    }
}
