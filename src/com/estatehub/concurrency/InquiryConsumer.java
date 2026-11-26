
package com.estatehub.concurrency;

public class InquiryConsumer implements Runnable {

    private final InquiryQueue queue;

    public InquiryConsumer(InquiryQueue queue) {
    if (queue == null) {
        throw new IllegalArgumentException("Queue cannot be null.");
    }

    this.queue = queue;
}

    @Override
    public void run() {
        try {
            while (true) {
                String inquiry = queue.consume();

                // null means the queue is closed and empty.
                if (inquiry == null) {
                    break;
                }

                System.out.println("Processing inquiry: " + inquiry);
                Thread.sleep(1000);
            }

            System.out.println("Consumer finished processing inquiries.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer was interrupted.");
        }
    }
}
