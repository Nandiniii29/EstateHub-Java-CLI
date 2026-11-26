
package com.estatehub.concurrency;

import java.util.LinkedList;
import java.util.Queue;

public class InquiryQueue {

    private final Queue<String> inquiries = new LinkedList<>();
    private final int capacity;
    private boolean closed = false;

    public InquiryQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.capacity = capacity;
    }

    // Producer adds an inquiry to the queue.
    public synchronized void produce(String inquiry) throws InterruptedException {

    if (inquiry == null || inquiry.trim().isEmpty()) {
        throw new IllegalArgumentException("Inquiry cannot be empty.");
    }

    while (inquiries.size() == capacity && !closed) {
            wait();
        }

        if (closed) {
            throw new IllegalStateException("Queue is closed.");
        }

        inquiries.add(inquiry);
        System.out.println("Produced: " + inquiry);

        notifyAll();
    }

    // Consumer takes an inquiry from the queue.
    public synchronized String consume() throws InterruptedException {

        while (inquiries.isEmpty() && !closed) {
            wait();
        }

        if (inquiries.isEmpty()) {
            return null;
        }

        String inquiry = inquiries.remove();

        System.out.println("Consumed: " + inquiry);

        notifyAll();

        return inquiry;
    }

    // Stop accepting new inquiries and wake waiting threads.
    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}
