
package com.estatehub.main;

import com.estatehub.model.*;
import com.estatehub.repository.*;
import com.estatehub.service.*;
import com.estatehub.util.EMICalculator;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final PropertyRepository repository =
            new InMemoryPropertyRepository();

    private static final PropertyService propertyService =
            new PropertyService(repository);

    private static final PropertySearchService searchService =
            new PropertySearchService(propertyService);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("       ESTATEHUB - PROPERTY MANAGER");
            System.out.println("====================================");
            System.out.println("1. Add Property");
            System.out.println("2. View All Properties");
            System.out.println("3. Search Property by ID");
            System.out.println("4. Search Property by City");
            System.out.println("5. Search Property by Locality");
            System.out.println("6. Filter Properties");
            System.out.println("7. Sort Properties");
            System.out.println("8. Delete Property");
            System.out.println("9. EMI Calculator");
            System.out.println("0. Exit");
            System.out.println("====================================");

            choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        addProperty();
                        break;
                    case 2:
                        displayProperties(propertyService.getAllProperties());
                        break;
                    case 3:
                        searchById();
                        break;
                    case 4:
                        searchByCity();
                        break;
                    case 5:
                        searchByLocality();
                        break;
                    case 6:
                        filterProperties();
                        break;
                    case 7:
                        sortProperties();
                        break;
                    case 8:
                        deleteProperty();
                        break;
                    case 9:
                        calculateEMI();
                        break;
                    case 0:
                        System.out.println("Thank you for using EstateHub!");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }

    private static void addProperty() {

        System.out.println("\n--- ADD PROPERTY ---");

        int id = readInt("Property ID: ");

        System.out.print("Title: ");
        String title = sc.nextLine();

        PropertyPurpose purpose =
                readEnum(PropertyPurpose.class, "Purpose (BUY/RENT): ");

        PropertyType type =
                readEnum(PropertyType.class, "Type: ");

        System.out.print("City: ");
        String city = sc.nextLine();

        System.out.print("Locality: ");
        String locality = sc.nextLine();

        System.out.print("Address: ");
        String address = sc.nextLine();

        double price = readDouble("Price: ");
        int bedrooms = readInt("Bedrooms: ");
        int bathrooms = readInt("Bathrooms: ");
        double area = readDouble("Area (sq. ft.): ");

        FurnishingStatus furnishing =
                readEnum(FurnishingStatus.class,
                        "Furnishing (FURNISHED/SEMI_FURNISHED/UNFURNISHED): ");

        System.out.print("Description: ");
        String description = sc.nextLine();

        int ownerId = readInt("Owner ID: ");

        Property property = new Property(
                id, title, purpose, type,
                city, locality, address,
                price, bedrooms, bathrooms, area,
                furnishing, description,
                ListingStatus.PENDING, ownerId
        );

        propertyService.addProperty(property);

        System.out.println("Property added successfully!");
    }

    private static void searchById() {

        int id = readInt("Enter property ID: ");

        Property property = searchService.searchById(id);

        if (property == null) {
            System.out.println("Property not found.");
        } else {
            System.out.println(property);
        }
    }

    private static void searchByCity() {

        System.out.print("Enter city: ");
        String city = sc.nextLine();

        displayProperties(searchService.searchByCity(city));
    }

    private static void searchByLocality() {

        System.out.print("Enter locality: ");
        String locality = sc.nextLine();

        displayProperties(searchService.searchByLocality(locality));
    }

    private static void filterProperties() {

        System.out.println("\n1. Filter by Purpose");
        System.out.println("2. Filter by Property Type");
        System.out.println("3. Filter by Price");

        int choice = readInt("Choose filter: ");

        switch (choice) {

            case 1:
                PropertyPurpose purpose =
                        readEnum(PropertyPurpose.class, "Enter BUY or RENT: ");
                displayProperties(searchService.filterByPurpose(purpose));
                break;

            case 2:
                PropertyType type =
                        readEnum(PropertyType.class, "Enter property type: ");
                displayProperties(searchService.filterByType(type));
                break;

            case 3:
                double min = readDouble("Minimum price: ");
                double max = readDouble("Maximum price: ");

                if (min > max) {
                    System.out.println("Minimum price cannot exceed maximum.");
                    break;
                }

                displayProperties(searchService.filterByPrice(min, max));
                break;

            default:
                System.out.println("Invalid filter choice.");
        }
    }

    private static void sortProperties() {

        System.out.println("\n1. Sort by Price");
        System.out.println("2. Sort by Area");

        int choice = readInt("Choose sorting option: ");

        switch (choice) {

            case 1:
                displayProperties(searchService.sortByPrice());
                break;

            case 2:
                displayProperties(searchService.sortByArea());
                break;

            default:
                System.out.println("Invalid sorting choice.");
        }
    }

    private static void deleteProperty() {

        int id = readInt("Enter property ID to delete: ");

        boolean deleted = propertyService.deleteProperty(id);

        if (deleted) {
            System.out.println("Property deleted successfully.");
        } else {
            System.out.println("Property not found.");
        }
    }

    private static void calculateEMI() {

        double loan = readDouble("Loan amount: ");
        double interest = readDouble("Annual interest rate (%): ");
        int years = readInt("Loan tenure (years): ");

        double emi = EMICalculator.calculateEMI(
                loan, interest, years
        );

        System.out.printf("Monthly EMI: %.2f%n", emi);
    }

    private static void displayProperties(List<Property> properties) {

        if (properties.isEmpty()) {
            System.out.println("No properties found.");
            return;
        }

        for (Property property : properties) {
            System.out.println("\n----------------------------");
            System.out.println(property);
        }

        System.out.println("----------------------------");
        System.out.println("Total properties: " + properties.size());
    }

    private static int readInt(String message) {

        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String message) {

        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(sc.nextLine().trim());

                if (!Double.isFinite(value)) {
                    System.out.println("Enter a finite number.");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static <T extends Enum<T>> T readEnum(
            Class<T> enumClass, String message) {

        while (true) {
            System.out.print(message);

            String input = sc.nextLine()
                    .trim()
                    .toUpperCase()
                    .replace(' ', '_');

            try {
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid option. Available values:");

                for (T value : enumClass.getEnumConstants()) {
                    System.out.println("- " + value);
                }
            }
        }
    }
}
