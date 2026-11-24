package com.estatehub.concurrency;

public class SynchronizationDemo {

    public static void main(String[] args) {

        TourCapacityManager manager = new TourCapacityManager();

        Thread t1 = new Thread(() -> manager.bookSlot("Customer 1"));
        Thread t2 = new Thread(() -> manager.bookSlot("Customer 2"));
        Thread t3 = new Thread(() -> manager.bookSlot("Customer 3"));
        Thread t4 = new Thread(() -> manager.bookSlot("Customer 4"));
        Thread t5 = new Thread(() -> manager.bookSlot("Customer 5"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}