
package com.estatehub.concurrency;

public class MultithreadingDemo {

    public static void main(String[] args) {

        BackgroundTask task1 =
                new BackgroundTask("Property Report Generation");

        BackgroundTask task2 =
                new BackgroundTask("Property Data Backup");

        BackgroundTask task3 =
                new BackgroundTask("Inquiry Processing");

        Thread thread1 = new Thread(task1, "Thread-1");
        Thread thread2 = new Thread(task2, "Thread-2");
        Thread thread3 = new Thread(task3, "Thread-3");

        System.out.println("Starting background tasks...");

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
    System.out.println("Main thread interrupted.");
    Thread.currentThread().interrupt();
    return;
}

        System.out.println("All background tasks completed.");
    }
}
