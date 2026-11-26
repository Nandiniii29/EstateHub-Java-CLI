
package com.estatehub.main;

import com.estatehub.exceptions.InvalidInputException;
import com.estatehub.exceptions.InvalidPropertyException;
import com.estatehub.exceptions.Member2ValidationException;
import com.estatehub.exceptions.PropertyNotFoundException;
import com.estatehub.model.*;
import com.estatehub.repository.*;
import com.estatehub.service.*;
import com.estatehub.util.EMICalculator;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    private static PropertyRepository repository =
            new MySQLPropertyRepository();

    private static PropertyService propertyService =
            new PropertyService(repository);

    private static AdminPropertyService adminPropertyService =
            new AdminPropertyService(repository);

    private static AdminDashboardService dashboardService =
            new AdminDashboardService(repository);

    private static PropertySearchService searchService =
            new PropertySearchService(propertyService);

    private static UserService userService = new UserService();

    private static CustomerPropertyService customerPropertyService =
            new CustomerPropertyService();

    private static InquiryService inquiryService =
            new InquiryService(new MySQLInquiryRepository());

    private static TourRequestService tourRequestService =
            new TourRequestService();

    private static AdminReportService reportService =
            new AdminReportService(inquiryService);

    private static CSVExportService csvExportService =
            new CSVExportService(inquiryService);
    private static CSVImportService csvImportService =
        new CSVImportService(propertyService);
    private static AdminActivityLogService activityLogService =
            new AdminActivityLogService();


    public static void main(String[] args) {

        int choice = -1;

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
            System.out.println("11. User Management");
            System.out.println("12. Admin Management");
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

                case 11:
                    userManagementMenu();
                    break;

                case 12:
                    adminMenu();
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

            User currentUser = userService.getCurrentUser();

            if (currentUser != null) {
                try {
                    customerPropertyService.recordViewed(
                            currentUser.getUserId(),
                            String.valueOf(property.getPropertyId())
                    );

                } catch (Member2ValidationException e) {
                    System.out.println(
                            "Could not update recently viewed: "
                                    + e.getMessage()
                    );

                } catch (IllegalStateException e) {
                    System.out.println(
                            "Could not save viewing history: "
                                    + e.getMessage()
                    );
                }
            }

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
        System.out.println("4. Filter by Bedrooms");
        System.out.println("5. Filter by Bathrooms");
        System.out.println("6. Filter by Furnishing Status");
        System.out.println("7. Filter by Listing Status");

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

            case 4:
                System.out.print("Enter number of bedrooms: ");
                int bedrooms = readInt();

                if (bedrooms < 0) {
                    System.out.println("Bedrooms cannot be negative.");
                    break;
                }

                displayProperties(searchService.filterByBedrooms(bedrooms));
                break;

            case 5:
                System.out.print("Enter number of bathrooms: ");
                int bathrooms = readInt();

                if (bathrooms < 0) {
                    System.out.println("Bathrooms cannot be negative.");
                    break;
                }

                displayProperties(searchService.filterByBathrooms(bathrooms));
                break;

            case 6:
                FurnishingStatus furnishing =
                        readEnum(FurnishingStatus.class,
                                "Enter furnishing status (FURNISHED/SEMI_FURNISHED/UNFURNISHED): ");

                displayProperties(
                        searchService.filterByFurnishingStatus(furnishing));
                break;

            case 7:
                ListingStatus status =
                        readEnum(ListingStatus.class,
                                "Enter listing status (PENDING/APPROVED/REJECTED/SOLD/RENTED): ");

                displayProperties(
                        searchService.filterByListingStatus(status));
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


    // ADMIN MENU

    private static void adminMenu() {

        User loggedInUser = userService.getCurrentUser();

        if (loggedInUser == null) {
            System.out.println("Please login before accessing Admin Management.");
            return;
        }

        if (loggedInUser.getRole() != User.Role.ADMIN) {
            System.out.println("Access denied. Admin privileges are required.");
            return;
        }

        int choice;

        do {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. View Pending Properties");
            System.out.println("2. Approve Property");
            System.out.println("3. Reject Property");
            System.out.println("4. View Admin Dashboard");
            System.out.println("5. View All Customer Inquiries");
            System.out.println("6. View Inquiry Reports");
            System.out.println("7. Export Inquiries to CSV");
System.out.println("8. Import Properties from CSV");
System.out.println("9. View Admin Activity Log");
System.out.println("0. Back to Main Menu");

            System.out.print("Enter choice: ");

            choice = readInt();

            try {
                switch (choice) {

                    case 1:
                        System.out.println("\n--- PENDING PROPERTIES ---");

                        List<Property> pendingProperties =
                                adminPropertyService.getPendingProperties();

                        if (pendingProperties.isEmpty()) {
                            System.out.println("No pending properties found.");
                        } else {
                            for (Property property : pendingProperties) {
                                System.out.println(property);
                            }
                        }
                        break;

                    case 2:
                        System.out.print("Enter Property ID to approve: ");
                        int approveId = readInt();

                        adminPropertyService.approveProperty(approveId);

                        activityLogService.logActivity(
                                loggedInUser,
                                approveId,
                                "APPROVE_PROPERTY",
                                "Admin approved property ID " + approveId
                        );

                        System.out.println("Property approved successfully!");
                        break;

                    case 3:
                        System.out.print("Enter Property ID to reject: ");
                        int rejectId = readInt();

                        adminPropertyService.rejectProperty(rejectId);

                        activityLogService.logActivity(
                                loggedInUser,
                                rejectId,
                                "REJECT_PROPERTY",
                                "Admin rejected property ID " + rejectId
                        );

                        System.out.println("Property rejected successfully!");
                        break;

                    case 4:
                        System.out.println("\n========== ADMIN DASHBOARD ==========");

                        System.out.println(
                                "Total Properties: "
                                        + dashboardService.getTotalProperties()
                        );

                        Map<ListingStatus, Integer> stats =
                                dashboardService.getPropertyStatistics();

                        for (ListingStatus status : ListingStatus.values()) {
                            System.out.println(
                                    status + " Properties: " + stats.get(status)
                            );
                        }

                        System.out.println("\n--- Properties by Purpose ---");
                        dashboardService.getPropertiesByPurpose().forEach(
                                (purpose, count) ->
                                        System.out.println(purpose + ": " + count)
                        );

                        System.out.println("\n--- Properties by City ---");
                        dashboardService.getPropertiesByCity().forEach(
                                (city, count) ->
                                        System.out.println(city + ": " + count)
                        );

                        System.out.println("\n--- Properties by Type ---");
                        dashboardService.getPropertiesByType().forEach(
                                (type, count) ->
                                        System.out.println(type + ": " + count)
                        );

                        break;

                    case 5:
                        System.out.println("\n========== CUSTOMER INQUIRIES ==========");

                        List<Inquiry> allInquiries =
                                inquiryService.getAllInquiries();

                        if (allInquiries.isEmpty()) {
                            System.out.println("No customer inquiries found.");
                        } else {
                            for (Inquiry inquiry : allInquiries) {
                                System.out.println(inquiry);
                            }
                        }
                        break;

                    case 6:
                        System.out.println("\n========== INQUIRY REPORTS ==========");

                        System.out.println(
                                "Total Inquiries: "
                                        + reportService.getTotalInquiries()
                        );

                        System.out.println(
                                "Pending Inquiries: "
                                        + reportService.getPendingInquiries()
                        );

                        System.out.println("\n--- Inquiries by Type ---");
                        reportService.getInquiriesByType().forEach(
                                (type, count) ->
                                        System.out.println(type + ": " + count)
                        );

                        System.out.println("\n--- Inquiries by Property ---");
                        reportService.getInquiriesByProperty().forEach(
                                (propertyId, count) ->
                                        System.out.println(
                                                "Property " + propertyId + ": " + count
                                        )
                        );

                        System.out.println("\n--- Inquiries by Status ---");
                        reportService.getInquiriesByStatus().forEach(
                                (status, count) ->
                                        System.out.println(status + ": " + count)
                        );
                        break;

                    case 7:
                        System.out.println("\n========== CSV EXPORT ==========");

                        try {
                            String filePath = csvExportService.exportInquiries();

                            System.out.println("Inquiries exported successfully!");
                            System.out.println("File saved at: " + filePath);

                        } catch (java.io.IOException e) {
                            System.out.println("CSV export failed: " + e.getMessage());
                        }

                        break;

                                        case 8:
                        System.out.println("\n========== CSV IMPORT ==========");

                        System.out.print("Enter CSV file path: ");
                        String importFilePath = sc.nextLine().trim();

                        if (importFilePath.isEmpty()) {
                            System.out.println("File path cannot be empty.");
                        } else {
                            csvImportService.importProperties(importFilePath);
                        }

                        break;

                    case 9:
                        System.out.println("\n========== ADMIN ACTIVITY LOG ==========");

                        List<AdminActivityLog> activities =
                                activityLogService.getAllActivities();

                        if (activities.isEmpty()) {
                            System.out.println("No admin activities found.");
                        } else {
                            for (AdminActivityLog activity : activities) {
                                System.out.println("----------------------------------");
                                System.out.println(activity);
                            }
                            System.out.println("----------------------------------");
                        }
                        break;

                    case 0:
                        System.out.println("Returning to main menu...");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (InvalidInputException e) {
                System.out.println("Admin operation failed: " + e.getMessage());

            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 0);
    }


    // USER MANAGEMENT MENU

    private static void userManagementMenu() {

        int choice;

        do {
            System.out.println("\n--- USER MANAGEMENT ---");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. Update Profile");
            System.out.println("4. Change Password");
            System.out.println("5. Submit Property Inquiry");
            System.out.println("6. View Inquiry History");
            System.out.println("7. Cancel Inquiry");
            System.out.println("8. Request Property Tour");
            System.out.println("9. View Tour Requests");
            System.out.println("10. Logout");
            System.out.println("11. Customer Property Features");
            System.out.println("12. Register Initial Admin");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                try {
                    switch (choice) {

                        case 1: {
                            System.out.print("Enter name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter email: ");
                            String email = sc.nextLine();

                            System.out.print("Enter phone: ");
                            String phone = sc.nextLine();

                            System.out.print("Enter password: ");
                            String password = sc.nextLine();

                            userService.register(name, email, phone, password);
                            System.out.println("Registration successful!");
                            break;
                        }

                        case 2: {
                            System.out.print("Enter email: ");
                            String email = sc.nextLine();

                            System.out.print("Enter password: ");
                            String password = sc.nextLine();

                            userService.login(email, password);
                            System.out.println("Login successful!");
                            break;
                        }

                        case 3: {
                            System.out.print("Enter new name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter new phone: ");
                            String phone = sc.nextLine();

                            userService.updateProfile(name, phone);
                            System.out.println("Profile updated!");
                            break;
                        }

                        case 4: {
                            System.out.print("Enter current password: ");
                            String oldPassword = sc.nextLine();

                            System.out.print("Enter new password: ");
                            String newPassword = sc.nextLine();

                            userService.changePassword(oldPassword, newPassword);
                            System.out.println("Password changed!");
                            break;
                        }

                        case 5: {
                            User user = userService.getCurrentUser();

                            if (user == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            System.out.print("Enter Property ID: ");
                            String propertyId = sc.nextLine();

                            System.out.print("Enter Inquiry Type: ");
                            String type = sc.nextLine();

                            System.out.print("Enter message: ");
                            String message = sc.nextLine();

                            Inquiry inquiry = inquiryService.submit(
                                    user.getUserId(), propertyId, type, message
                            );

                            System.out.println("Inquiry submitted!");
                            System.out.println(inquiry);
                            break;
                        }

                        case 6: {
                            User user = userService.getCurrentUser();

                            if (user == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            List<Inquiry> history =
                                    inquiryService.getHistory(user.getUserId());

                            if (history.isEmpty()) {
                                System.out.println("No inquiries found.");
                            } else {
                                for (Inquiry item : history) {
                                    System.out.println(item);
                                    System.out.println("--------------------");
                                }
                            }
                            break;
                        }

                        case 7: {
                            User user = userService.getCurrentUser();

                            if (user == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            System.out.print("Enter Inquiry ID: ");
                            String inquiryId = sc.nextLine();

                            boolean cancelled = inquiryService.cancel(
                                    user.getUserId(), inquiryId
                            );

                            System.out.println(cancelled
                                    ? "Inquiry cancelled!"
                                    : "Inquiry could not be cancelled.");
                            break;
                        }

                        case 8: {
                            User user = userService.getCurrentUser();

                            if (user == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            System.out.print("Enter Property ID: ");
                            String propertyId = sc.nextLine();

                            System.out.print("Enter preferred date (YYYY-MM-DD): ");
                            LocalDate date = LocalDate.parse(sc.nextLine());

                            System.out.print("Enter additional details: ");
                            String details = sc.nextLine();

                            TourRequest tour = tourRequestService.requestTour(
                                    user.getUserId(), propertyId, date, details
                            );

                            System.out.println("Tour request submitted!");
                            System.out.println("Request ID: " + tour.getRequestId());
                            break;
                        }

                        case 9: {
                            User user = userService.getCurrentUser();

                            if (user == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            List<TourRequest> tours =
                                    tourRequestService.getRequestsForUser(
                                            user.getUserId()
                                    );

                            if (tours.isEmpty()) {
                                System.out.println("No tour requests found.");
                            } else {
                                for (TourRequest tour : tours) {
                                    System.out.println("Request ID: " + tour.getRequestId());
                                    System.out.println("Property ID: " + tour.getPropertyId());
                                    System.out.println("Preferred Date: " + tour.getPreferredDate());
                                    System.out.println("Details: " + tour.getDetails());
                                    System.out.println("--------------------");
                                }
                            }
                            break;
                        }

                        case 10:
                            userService.logout();
                            System.out.println("Logged out successfully.");
                            break;

                        case 11: {
                            User user = userService.getCurrentUser();

                            if (user == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            customerPropertyMenu();
                            break;
                        }

                        case 12: {
                            System.out.println("\n--- INITIAL ADMIN REGISTRATION ---");

                            System.out.print("Enter admin setup key: ");
                            String setupKey = sc.nextLine();

                            System.out.print("Enter name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter email: ");
                            String email = sc.nextLine();

                            System.out.print("Enter phone: ");
                            String phone = sc.nextLine();

                            System.out.print("Enter password: ");
                            String password = sc.nextLine();

                            User admin = userService.registerAdmin(
                                    name, email, phone, password, setupKey
                            );

                            System.out.println("Admin account created successfully!");
                            System.out.println("Admin ID: " + admin.getUserId());
                            break;
                        }

                        case 0:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }

                } catch (Member2ValidationException e) {
                    System.out.println("Operation failed: " + e.getMessage());

                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date. Use YYYY-MM-DD.");

                } catch (IllegalStateException e) {
                    System.out.println("Database operation failed: " + e.getMessage());
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                choice = -1;
            }

        } while (choice != 0);
    }


    // CUSTOMER PROPERTY FEATURES MENU

    private static void customerPropertyMenu() {

        int choice = -1;

        do {
            System.out.println("\n--- CUSTOMER PROPERTY FEATURES ---");
            System.out.println("1. Add Property to Favorites");
            System.out.println("2. Remove Property from Favorites");
            System.out.println("3. View Favorite Properties");
            System.out.println("4. View Recently Viewed Properties");
            System.out.println("5. Add Property to Comparison");
            System.out.println("6. Remove Property from Comparison");
            System.out.println("7. View Comparison Properties");
            System.out.println("0. Back to User Management");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                User user = userService.getCurrentUser();

                if (user == null) {
                    System.out.println("Please login first.");
                    return;
                }

                String userId = user.getUserId();

                switch (choice) {

                    case 1: {
                        System.out.print("Enter Property ID: ");
                        String propertyId = sc.nextLine();

                        boolean added = customerPropertyService.addFavorite(
                                userId, propertyId
                        );

                        System.out.println(added
                                ? "Property added to favorites!"
                                : "Property is already in favorites.");
                        break;
                    }

                    case 2: {
                        System.out.print("Enter Property ID: ");
                        String propertyId = sc.nextLine();

                        boolean removed = customerPropertyService.removeFavorite(
                                userId, propertyId
                        );

                        System.out.println(removed
                                ? "Property removed from favorites."
                                : "Property was not in favorites.");
                        break;
                    }

                    case 3:
                        System.out.println("\n--- FAVORITE PROPERTIES ---");

                        displayCustomerProperties(
                                customerPropertyService.getFavorites(userId)
                        );
                        break;

                    case 4:
                        System.out.println("\n--- RECENTLY VIEWED PROPERTIES ---");

                        displayCustomerProperties(
                                customerPropertyService.getRecentlyViewed(userId)
                        );
                        break;

                    case 5: {
                        System.out.print("Enter Property ID: ");
                        String propertyId = sc.nextLine();

                        boolean added = customerPropertyService.addToComparison(
                                userId, propertyId
                        );

                        System.out.println(added
                                ? "Property added to comparison!"
                                : "Property is already in comparison.");
                        break;
                    }

                    case 6: {
                        System.out.print("Enter Property ID: ");
                        String propertyId = sc.nextLine();

                        boolean removed =
                                customerPropertyService.removeFromComparison(
                                        userId, propertyId
                                );

                        System.out.println(removed
                                ? "Property removed from comparison."
                                : "Property was not in comparison.");
                        break;
                    }

                    case 7:
                        System.out.println("\n--- COMPARISON PROPERTIES ---");

                        displayCustomerProperties(
                                customerPropertyService.getComparison(userId)
                        );
                        break;

                    case 0:
                        System.out.println("Returning to User Management...");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid menu number.");
                choice = -1;

            } catch (Member2ValidationException e) {
                System.out.println("Operation failed: " + e.getMessage());

            } catch (IllegalStateException e) {
                System.out.println(
                        "Database operation failed: " + e.getMessage()
                );
            }

        } while (choice != 0);
    }


    // DISPLAY CUSTOMER PROPERTY LISTS

    private static void displayCustomerProperties(List<String> propertyIds) {

        if (propertyIds == null || propertyIds.isEmpty()) {
            System.out.println("No properties found.");
            return;
        }

        for (String propertyId : propertyIds) {
            try {
                Property property = searchService.searchById(
                        Integer.parseInt(propertyId)
                );

                System.out.println("----------------------------------");
                System.out.println(property);

            } catch (Exception e) {
                System.out.println(
                        "Could not display property ID " + propertyId
                                + ": " + e.getMessage()
                );
            }
        }

        System.out.println("----------------------------------");
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
