
package com.estatehub.util;

public class PropertyValidator {

    public static boolean isValidPrice(double price) {
        return Double.isFinite(price) && price > 0;
    }

    public static boolean isValidArea(double area) {
        return Double.isFinite(area) && area > 0;
    }

    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    public static boolean isValidRoomCount(int count) {
        return count >= 0;
    }

    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    public static boolean isValidPropertyId(int id) {
        return id > 0;
    }
}
