
package com.estatehub.concurrency;

public class InquiryProducer implements Runnable {

    private final InquiryQueue queue;
    private final String[] inquiries;

    public InquiryProducer(InquiryQueue queue, String[] inquiries) {
        this.queue = queue;
        this.inquiries = inquiries;
    }

    @Override
    public void run() {
        try {
            for (String inquiry : inquiries) {
                queue.produce(inquiry);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer was interrupted.");
        }
    }
}
