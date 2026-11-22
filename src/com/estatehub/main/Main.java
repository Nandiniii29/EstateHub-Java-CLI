
package com.estatehub.main;

import com.estatehub.model.*;
import com.estatehub.repository.*;
import com.estatehub.service.*;
import com.estatehub.util.EMICalculator;
import com.estatehub.exceptions.InvalidPropertyException;
import com.estatehub.exceptions.PropertyNotFoundException;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    private static PropertyRepository repository =
            new InMemoryPropertyRepository();

    private static PropertyService propertyService =
            new PropertyService(repository);

    private static PropertySearchService searchService =
            new PropertySearchService(propertyService);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n==============================================");
            System.out.println("       ESTATEHUB - PROPERTY MANAGEMENT");
            System.out.println("==============================================");
            System.out.println("1. Add Property");
            System.out.println("2. View All Properties");
            System.out.println("3. Search Property by ID");
            System.out.println("4. Search Property by City");
            System.out.println("5. Search Property by Locality");
            System.out.println("6. Filter Properties");
            System.out.println("7. Sort Properties");
            System.out.println("8. Delete Property");
            System.out.println("9. EMI Calculator");
            System.out.println("10. Update Property");
            System.out.println("0. Exit");
            System.out.println("==============================================");

            System.out.print("Enter your choice: ");
            choice = readInt();

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

                case 10:
                    updateProperty();
                    break;

                case 0:
                    System.out.println("Thank you for using EstateHub!");
                    break;

                default:
                    System.out.println("Invalid menu choice. Try again.");
            }

        } while (choice != 0);
    }

    // ADD PROPERTY

    private static void addProperty() {

        try {
            System.out.println("\n--- ADD NEW PROPERTY ---");

            System.out.print("Enter property ID: ");
            int id = readInt();

            System.out.print("Enter property title: ");
            String title = sc.nextLine();

            PropertyPurpose purpose =
                    readEnum(PropertyPurpose.class, "Purpose (BUY/RENT): ");

            PropertyType type =
                    readEnum(PropertyType.class,
                            "Type (APARTMENT/HOUSE/VILLA/PLOT/COMMERCIAL): ");

            System.out.print("Enter city: ");
            String city = sc.nextLine();

            System.out.print("Enter locality: ");
            String locality = sc.nextLine();

            System.out.print("Enter address: ");
            String address = sc.nextLine();

            System.out.print("Enter price: ");
            double price = readDouble();

            System.out.print("Enter bedrooms: ");
            int bedrooms = readInt();

            System.out.print("Enter bathrooms: ");
            int bathrooms = readInt();

            System.out.print("Enter area in sq. ft.: ");
            double area = readDouble();

            FurnishingStatus furnishing =
                    readEnum(FurnishingStatus.class,
                            "Furnishing (FURNISHED/SEMI_FURNISHED/UNFURNISHED): ");

            System.out.print("Enter description: ");
            String description = sc.nextLine();

            System.out.print("Enter owner ID: ");
            int ownerId = readInt();

            Property property = new Property(
                    id, title, purpose, type, city, locality,
                    address, price, bedrooms, bathrooms, area,
                    furnishing, description, ListingStatus.PENDING,
                    ownerId
            );

            propertyService.addProperty(property);

            System.out.println("Property added successfully!");

        } catch (InvalidPropertyException e) {
            System.out.println("Cannot add property: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    // VIEW AND DISPLAY PROPERTIES

    private static void displayProperties(List<Property> properties) {

        if (properties == null || properties.isEmpty()) {
            System.out.println("No properties found.");
            return;
        }

        System.out.println("\n--- PROPERTY LIST ---");

        for (Property property : properties) {
            System.out.println("----------------------------------");
            System.out.println(property);
        }

        System.out.println("----------------------------------");
        System.out.println("Total properties: " + properties.size());
    }

    // SEARCH BY ID

    private static void searchById() {

        try {
            System.out.print("Enter property ID: ");
            int id = readInt();

            Property property = searchService.searchById(id);

            System.out.println("\nProperty found:");
            System.out.println(property);

        } catch (PropertyNotFoundException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (InvalidPropertyException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // SEARCH BY CITY

    private static void searchByCity() {

        System.out.print("Enter city name: ");
        String city = sc.nextLine();

        displayProperties(searchService.searchByCity(city));
    }

    // SEARCH BY LOCALITY

    private static void searchByLocality() {

        System.out.print("Enter locality: ");
        String locality = sc.nextLine();

        displayProperties(searchService.searchByLocality(locality));
    }

    // FILTER PROPERTIES

    private static void filterProperties() {

        System.out.println("\n--- FILTER PROPERTIES ---");
        System.out.println("1. Filter by Purpose");
        System.out.println("2. Filter by Type");
        System.out.println("3. Filter by Price Range");

        System.out.print("Enter choice: ");
        int choice = readInt();

        switch (choice) {

            case 1:
                PropertyPurpose purpose =
                        readEnum(PropertyPurpose.class,
                                "Enter purpose (BUY/RENT): ");

                displayProperties(searchService.filterByPurpose(purpose));
                break;

            case 2:
                PropertyType type =
                        readEnum(PropertyType.class, "Enter type: ");

                displayProperties(searchService.filterByType(type));
                break;

            case 3:
                System.out.print("Enter minimum price: ");
                double min = readDouble();

                System.out.print("Enter maximum price: ");
                double max = readDouble();

                if (min < 0 || max < min) {
                    System.out.println("Invalid price range.");
                    break;
                }

                displayProperties(searchService.filterByPrice(min, max));
                break;

            default:
                System.out.println("Invalid filter choice.");
        }
    }

    // SORT PROPERTIES

    private static void sortProperties() {

        System.out.println("\n--- SORT PROPERTIES ---");
        System.out.println("1. Sort by Price");
        System.out.println("2. Sort by Area");

        System.out.print("Enter choice: ");
        int choice = readInt();

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

    // DELETE PROPERTY

    private static void deleteProperty() {

        try {
            System.out.print("Enter property ID to delete: ");
            int id = readInt();

            boolean deleted = propertyService.deleteProperty(id);

            if (deleted) {
                System.out.println("Property deleted successfully!");
            }

        } catch (PropertyNotFoundException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (InvalidPropertyException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // UPDATE PROPERTY

    private static void updateProperty() {

        try {
            System.out.println("\n--- UPDATE PROPERTY ---");

            System.out.print("Enter property ID to update: ");
            int id = readInt();

            Property property = propertyService.getPropertyById(id);

            System.out.println("\nCurrent property details:");
            System.out.println(property);

            int choice;

            do {
                System.out.println("\n--- SELECT FIELD TO UPDATE ---");
                System.out.println("1. Title");
                System.out.println("2. Purpose");
                System.out.println("3. Property Type");
                System.out.println("4. City");
                System.out.println("5. Locality");
                System.out.println("6. Address");
                System.out.println("7. Price");
                System.out.println("8. Bedrooms");
                System.out.println("9. Bathrooms");
                System.out.println("10. Area");
                System.out.println("11. Furnishing Status");
                System.out.println("12. Description");
                System.out.println("0. Save and Exit");

                System.out.print("Enter your choice: ");
                choice = readInt();

                switch (choice) {

                    case 1:
                        System.out.print("Enter new title: ");
                        property.setTitle(sc.nextLine());
                        break;

                    case 2:
                        property.setPurpose(
                                readEnum(PropertyPurpose.class,
                                        "Enter new purpose (BUY/RENT): ")
                        );
                        break;

                    case 3:
                        property.setType(
                                readEnum(PropertyType.class,
                                        "Enter new property type: ")
                        );
                        break;

                    case 4:
                        System.out.print("Enter new city: ");
                        property.setCity(sc.nextLine());
                        break;

                    case 5:
                        System.out.print("Enter new locality: ");
                        property.setLocality(sc.nextLine());
                        break;

                    case 6:
                        System.out.print("Enter new address: ");
                        property.setAddress(sc.nextLine());
                        break;

                    case 7:
                        System.out.print("Enter new price: ");
                        property.setPrice(readDouble());
                        break;

                    case 8:
                        System.out.print("Enter new number of bedrooms: ");
                        property.setBedrooms(readInt());
                        break;

                    case 9:
                        System.out.print("Enter new number of bathrooms: ");
                        property.setBathrooms(readInt());
                        break;

                    case 10:
                        System.out.print("Enter new area: ");
                        property.setArea(readDouble());
                        break;

                    case 11:
                        property.setFurnishingStatus(
                                readEnum(FurnishingStatus.class,
                                        "Enter furnishing status: ")
                        );
                        break;

                    case 12:
                        System.out.print("Enter new description: ");
                        property.setDescription(sc.nextLine());
                        break;

                    case 0:
                        propertyService.updateProperty(property);
                        System.out.println("Property updated successfully!");
                        System.out.println("\nUpdated details:");
                        System.out.println(property);
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } while (choice != 0);

        } catch (PropertyNotFoundException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (InvalidPropertyException e) {
            System.out.println("Cannot update property: " + e.getMessage());
        }
    }

    // EMI CALCULATOR

    private static void calculateEMI() {

        try {
            System.out.println("\n--- EMI CALCULATOR ---");

            System.out.print("Enter loan amount: ");
            double loan = readDouble();

            System.out.print("Enter annual interest rate: ");
            double rate = readDouble();

            System.out.print("Enter tenure in years: ");
            int years = readInt();

            double emi = EMICalculator.calculateEMI(
                    loan, rate, years
            );

            System.out.printf("Monthly EMI: Rs. %.2f%n", emi);

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid EMI details: " + e.getMessage());
        }
    }

    // INTEGER INPUT

    private static int readInt() {

        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();

        return value;
    }

    // DECIMAL INPUT

    private static double readDouble() {

        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }

        double value = sc.nextDouble();
        sc.nextLine();

        if (!Double.isFinite(value)) {
            System.out.println("Please enter a finite number.");
            return readDouble();
        }

        return value;
    }

    // ENUM INPUT

    private static <T extends Enum<T>> T readEnum(
            Class<T> enumClass, String message) {

        while (true) {

            System.out.print(message);

            String input = sc.nextLine().trim()
                    .toUpperCase()
                    .replace(' ', '_');

            try {
                return Enum.valueOf(enumClass, input);

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
