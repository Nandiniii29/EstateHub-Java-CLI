package com.estatehub.main;

import com.estatehub.service.UserService;
import com.estatehub.exceptions.Member2ValidationException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        int choice;

        System.out.println("==================================================");
        System.out.println("       ESTATEHUB - USER MANAGEMENT SYSTEM");
        System.out.println("==================================================");

        do {
            System.out.println("\n========== USER MANAGEMENT MENU ==========");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. Update Profile");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                try {
                    switch (choice) {

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

                            userService.register(name, email, phone, password);

                            System.out.println("\nRegistration successful!");
                            System.out.println("Welcome to EstateHub!");
                            break;

                        case 2:
                            System.out.println("\n--- USER LOGIN ---");

                            System.out.print("Enter email: ");
                            String loginEmail = sc.nextLine();

                            System.out.print("Enter password: ");
                            String loginPassword = sc.nextLine();

                            userService.login(loginEmail, loginPassword);

                            System.out.println("\nLogin successful!");
                            System.out.println("You are now logged in.");
                            break;

                        case 3:
                            System.out.println("\n--- UPDATE PROFILE ---");

                            System.out.print("Enter new name: ");
                            String newName = sc.nextLine();

                            System.out.print("Enter new phone: ");
                            String newPhone = sc.nextLine();

                            userService.updateProfile(newName, newPhone);

                            System.out.println("Profile updated successfully!");
                            break;

                        case 4:
                            System.out.println("\n--- CHANGE PASSWORD ---");

                            System.out.print("Enter current password: ");
                            String oldPassword = sc.nextLine();

                            System.out.print("Enter new password: ");
                            String newPassword = sc.nextLine();

                            userService.changePassword(oldPassword, newPassword);

                            System.out.println("Password changed successfully!");
                            break;

                        case 5:
                            userService.logout();
                            System.out.println("You have been logged out.");
                            break;

                        case 0:
                            System.out.println("\nThank you for using EstateHub!");
                            break;

                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }

                } catch (Member2ValidationException e) {
                    System.out.println("\nOperation failed: " + e.getMessage());
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                choice = -1;
            }

        } while (choice != 0);

        sc.close();
    }
}