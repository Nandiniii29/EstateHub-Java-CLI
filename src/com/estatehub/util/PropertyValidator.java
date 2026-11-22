package com.estatehub.util;

public class PropertyValidator {

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidArea(double area) {
        return area > 0;
    }

    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    public static boolean isValidRoomCount(int count) {
        return count >= 0;
    }
}