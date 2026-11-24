
package com.estatehub.main;

import com.estatehub.service.UserService;
import com.estatehub.service.InquiryService;
import com.estatehub.service.TourRequestService;

import com.estatehub.model.User;
import com.estatehub.model.Inquiry;
import com.estatehub.model.TourRequest;

import com.estatehub.exceptions.Member2ValidationException;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        UserService userService = new UserService();
        InquiryService inquiryService = new InquiryService();
        TourRequestService tourRequestService = new TourRequestService();

        int choice;

        System.out.println("==================================================");
        System.out.println("             ESTATEHUB SYSTEM");
        System.out.println("==================================================");

        do {
            System.out.println("\n============== MAIN MENU ==============");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. Update Profile");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");
            System.out.println("6. Submit Inquiry");
            System.out.println("7. View Inquiry History");
            System.out.println("8. Cancel Inquiry");
            System.out.println("9. Request Property Tour");
            System.out.println("10. View Tour Requests");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                try {
                    switch (choice) {

                        // 1. USER REGISTRATION
                        case 1:
                            System.out.println("\n--- USER REGISTRATION ---");

                            System.out.print("Enter name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter email: ");
                            String email = sc.nextLine();

                            System.out.print("Enter phone: ");
                            String phone = sc.nextLine();

                            System.out.print("Enter password: ");
                            String password = sc.nextLine();

                            userService.register(
                                name, email, phone, password
                            );

                            System.out.println("Registration successful!");
                            break;

                        // 2. USER LOGIN
                        case 2:
                            System.out.println("\n--- USER LOGIN ---");

                            System.out.print("Enter email: ");
                            String loginEmail = sc.nextLine();

                            System.out.print("Enter password: ");
                            String loginPassword = sc.nextLine();

                            userService.login(loginEmail, loginPassword);

                            System.out.println("Login successful!");
                            break;

                        // 3. UPDATE PROFILE
                        case 3:
                            System.out.println("\n--- UPDATE PROFILE ---");

                            System.out.print("Enter new name: ");
                            String newName = sc.nextLine();

                            System.out.print("Enter new phone: ");
                            String newPhone = sc.nextLine();

                            userService.updateProfile(
                                newName, newPhone
                            );

                            System.out.println("Profile updated!");
                            break;

                        // 4. CHANGE PASSWORD
                        case 4:
                            System.out.println("\n--- CHANGE PASSWORD ---");

                            System.out.print("Enter current password: ");
                            String oldPassword = sc.nextLine();

                            System.out.print("Enter new password: ");
                            String newPassword = sc.nextLine();

                            userService.changePassword(
                                oldPassword, newPassword
                            );

                            System.out.println("Password changed!");
                            break;

                        // 5. LOGOUT
                        case 5:
                            userService.logout();
                            System.out.println("Logged out successfully.");
                            break;

                        // 6. SUBMIT INQUIRY
                        case 6:
                            System.out.println("\n--- SUBMIT INQUIRY ---");

                            if (userService.getCurrentUser() == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            System.out.print("Enter Property ID: ");
                            String propertyId = sc.nextLine();

                            System.out.print("Enter Inquiry Type: ");
                            String type = sc.nextLine();

                            System.out.print("Enter Message: ");
                            String message = sc.nextLine();

                            Inquiry inquiry = inquiryService.submit(
                                userService.getCurrentUser().getUserId(),
                                propertyId,
                                type,
                                message
                            );

                            System.out.println("Inquiry submitted!");
                            System.out.println(
                                "Inquiry ID: " + inquiry.getInquiryId()
                            );
                            break;

                        // 7. VIEW INQUIRY HISTORY
                        case 7:
                            System.out.println("\n--- INQUIRY HISTORY ---");

                            if (userService.getCurrentUser() == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            List<Inquiry> history =
                                inquiryService.getHistory(
                                    userService.getCurrentUser().getUserId()
                                );

                            if (history.isEmpty()) {
                                System.out.println("No inquiries found.");
                            } else {
                                for (Inquiry item : history) {
                                    System.out.println(
                                        "Inquiry ID: " + item.getInquiryId()
                                    );
                                    System.out.println(
                                        "Property ID: " + item.getPropertyId()
                                    );
                                    System.out.println(
                                        "Inquiry Type: " + item.getInquiryType()
                                    );
                                    System.out.println(
                                        "Message: " + item.getMessage()
                                    );
                                    System.out.println(
                                        "Status: " + item.getStatus()
                                    );
                                    System.out.println(
                                        "Created: " + item.getCreatedAt()
                                    );
                                    System.out.println("--------------------");
                                }
                            }
                            break;

                        // 8. CANCEL INQUIRY
                        case 8:
                            System.out.println("\n--- CANCEL INQUIRY ---");

                            if (userService.getCurrentUser() == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            System.out.print("Enter Inquiry ID: ");
                            String inquiryId = sc.nextLine();

                            boolean cancelled = inquiryService.cancel(
                                userService.getCurrentUser().getUserId(),
                                inquiryId
                            );

                            if (cancelled) {
                                System.out.println("Inquiry cancelled!");
                            } else {
                                System.out.println(
                                    "Inquiry could not be cancelled."
                                );
                            }
                            break;

                        // 9. REQUEST PROPERTY TOUR
                        case 9:
                            System.out.println("\n--- REQUEST PROPERTY TOUR ---");

                            if (userService.getCurrentUser() == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            System.out.print("Enter Property ID: ");
                            String tourPropertyId = sc.nextLine();

                            System.out.print(
                                "Enter preferred date (YYYY-MM-DD): "
                            );
                            String dateInput = sc.nextLine();

                            LocalDate tourDate = LocalDate.parse(dateInput);

                            System.out.print("Enter additional details: ");
                            String details = sc.nextLine();

                            TourRequest tour = tourRequestService.requestTour(
                                userService.getCurrentUser().getUserId(),
                                tourPropertyId,
                                tourDate,
                                details
                            );

                            System.out.println("Tour request submitted!");
                            System.out.println(
                                "Request ID: " + tour.getRequestId()
                            );
                            break;

                        // 10. VIEW TOUR REQUESTS
                        case 10:
                            System.out.println("\n--- TOUR REQUEST HISTORY ---");

                            if (userService.getCurrentUser() == null) {
                                System.out.println("Please login first.");
                                break;
                            }

                            List<TourRequest> tours =
                                tourRequestService.getRequestsForUser(
                                    userService.getCurrentUser().getUserId()
                                );

                            if (tours.isEmpty()) {
                                System.out.println("No tour requests found.");
                            } else {
                                for (TourRequest request : tours) {
                                    System.out.println(
                                        "Request ID: " + request.getRequestId()
                                    );
                                    System.out.println(
                                        "Property ID: " + request.getPropertyId()
                                    );
                                    System.out.println(
                                        "Preferred Date: " + request.getPreferredDate()
                                    );
                                    System.out.println(
                                        "Details: " + request.getDetails()
                                    );
                                    System.out.println("--------------------");
                                }
                            }
                            break;

                        // 0. EXIT
                        case 0:
                            System.out.println(
                                "Thank you for using EstateHub!"
                            );
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }

                } catch (Member2ValidationException e) {
                    System.out.println(
                        "Operation failed: " + e.getMessage()
                    );
                } catch (DateTimeParseException e) {
                    System.out.println(
                        "Invalid date! Please use YYYY-MM-DD."
                    );
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid menu number.");
                choice = -1;
            }

        } while (choice != 0);

        sc.close();
    }
}
