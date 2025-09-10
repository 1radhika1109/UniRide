package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            // Validate menu input (only 1,2,3 allowed)
            int choice;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("⚠️ Invalid input! Please enter 1, 2, or 3.");
                sc.nextLine(); // clear buffer
                continue;
            }

            switch (choice) {
                case 1: // Register
                    sc.nextLine(); // clear buffer
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    System.out.print("Are you a driver? (true/false): ");
                    String driverInput = sc.next().toLowerCase();
                    boolean isDriver = driverInput.equals("true") || driverInput.equals("t") || driverInput.equals("yes") || driverInput.equals("y");

                    System.out.println("✅ Registered successfully!");
                    System.out.println("Name: " + name + ", Email: " + email + ", Driver: " + isDriver);
                    break;

                case 2: // Login
                    sc.nextLine(); // clear buffer
                    System.out.print("Enter email: ");
                    String loginEmail = sc.nextLine();

                    System.out.print("Enter password: ");
                    String loginPassword = sc.nextLine();

                    System.out.println("✅ Login successful for " + loginEmail);
                    break;

                case 3: // Exit
                    System.out.println("👋 Exiting... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("⚠️ Invalid choice! Please enter 1, 2, or 3.");
            }
        }
    }
}
