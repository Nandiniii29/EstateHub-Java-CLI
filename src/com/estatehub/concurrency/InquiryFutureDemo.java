
package com.estatehub.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InquiryFutureDemo {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            // Submit Callable tasks and store their Future objects.
            Future<String> result1 = executor.submit(new InquiryCallable(101));
            Future<String> result2 = executor.submit(new InquiryCallable(102));
            Future<String> result3 = executor.submit(new InquiryCallable(103));

            System.out.println("Waiting for inquiry results...\n");

            // Retrieve returned results.
            System.out.println(result1.get());
            System.out.println(result2.get());
            System.out.println(result3.get());

        } catch (Exception e) {
            System.out.println("Error processing inquiries: " + e.getMessage());

        } finally {
            executor.shutdown();
        }

        System.out.println("\nAll Callable tasks completed.");
    }
}
