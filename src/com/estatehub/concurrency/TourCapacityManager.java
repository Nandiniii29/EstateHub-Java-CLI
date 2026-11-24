package com.estatehub.concurrency;

public class TourCapacityManager {

    private int availableSlots = 3;

    public synchronized void bookSlot(String customerName) {

        if (availableSlots > 0) {
            System.out.println(customerName + " is booking a slot.");

            availableSlots--;

            System.out.println("Booking successful for " + customerName);
            System.out.println("Remaining slots: " + availableSlots);

        } else {
            System.out.println("Sorry " + customerName + ", no slots available.");
        }
    }
}