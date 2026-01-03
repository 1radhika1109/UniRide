package org.example;

import org.example.dao.BookingDAO;
import org.example.dao.RideDAO;
import org.example.dao.UserDAO;
import org.example.exception.InvalidLoginException;
import org.example.exception.SeatUnavailableException;
import org.example.model.Ride;
import org.example.model.User;
import org.example.services.RideService;
import org.example.services.UserService;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ⚠️ Hibernate SessionFactory
        SessionFactory factory = HibernateUtil.getSessionFactory();



        // Inject DAOs
        UserDAO userDao = new UserDAO(factory);
        RideDAO rideDao = new RideDAO(factory);
        BookingDAO bookingDao = new BookingDAO(factory);

        // Services
        UserService userService = new UserService(userDao, rideDao, bookingDao);

        RideService rideService = new RideService(rideDao, bookingDao);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (true) {
            System.out.println("\n===== UniRide Console App =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View All Users");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1 -> registerUser(sc, userService);
                case 2 -> loginUser(sc, userService, rideService, formatter);
                case 3 -> viewAllUsers(userService);
                case 4 -> { exitApp(sc, factory); return; }
                default -> System.out.println("⚠️ Invalid choice! Enter 1-4.");
            }
        }
    }

    private static void registerUser(Scanner sc, UserService userService) {
        sc.nextLine(); // clear buffer
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.print("Are you a driver? (yes/no): ");
        String input = sc.nextLine().trim().toLowerCase();

        boolean isDriver = input.equals("yes") || input.equals("y") || input.equals("true") || input.equals("1");
        String vehicleInfo = null;
        String role = isDriver ? "DRIVER" : "PASSENGER";

        if (isDriver) {
            System.out.print("Enter vehicle info: ");
            vehicleInfo = sc.nextLine();
        }

        User newUser = userService.registerUser(name, email, password, role, vehicleInfo);
        if (newUser != null) {
            System.out.println("✅ Registered! User ID: " + newUser.getId() +
                    " | Role: " + role);
        } else {
            System.out.println("❌ Registration failed.");
        }
    }

    private static void loginUser(Scanner sc, UserService userService, RideService rideService, DateTimeFormatter formatter) {
        sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try {
            User loggedInUser = userService.login(email, password);
            System.out.println("✅ Login successful! Welcome " + loggedInUser.getName() +
                    " (" + (loggedInUser.isDriver() ? "Driver" : "Passenger") + ")");

            if (loggedInUser.isDriver()) {
                driverMenu(sc, loggedInUser, rideService, formatter);
            } else {
                passengerMenu(sc, loggedInUser, rideService);
            }

        } catch (InvalidLoginException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void viewAllUsers(UserService userService) {
        System.out.println("📋 Registered Users:");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User u : users) {
                System.out.println("- " + u.getId() + ": " + u.getName() +
                        " | " + u.getEmail() +
                        " | Role: " + (u.isDriver() ? "Driver" : "Passenger") +
                        (u.isDriver() ? " | Vehicle: " + u.getVehicleInfo() : ""));
            }
        }
    }

    private static void exitApp(Scanner sc, SessionFactory factory) {
        System.out.println("👋 Exiting... Goodbye!");
        sc.close();
        factory.close();
    }

    private static int readInt(Scanner sc) {
        if (sc.hasNextInt()) {
            int val = sc.nextInt();
            sc.nextLine(); // clear buffer
            return val;
        } else {
            sc.nextLine();
            return -1;
        }
    }

    // ----------------- DRIVER MENU -----------------
    private static void driverMenu(Scanner sc, User driver, RideService rideService, DateTimeFormatter formatter) {
        while (true) {
            System.out.println("\n--- Driver Menu ---");
            System.out.println("1. Offer a Ride");
            System.out.println("2. View My Offered Rides");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1 -> offerRide(sc, driver, rideService, formatter);
                case 2 -> viewDriverRides(driver, rideService);
                case 3 -> { System.out.println("🔙 Logging out..."); return; }
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void offerRide(Scanner sc, User driver, RideService rideService, DateTimeFormatter formatter) {
        System.out.print("Enter pickup point: ");
        String source = sc.nextLine();
        System.out.print("Enter destination point: ");
        String dest = sc.nextLine();
        System.out.print("Enter date/time (yyyy-MM-dd HH:mm): ");
        String timeStr = sc.nextLine();

        LocalDateTime time;
        try {
            time = LocalDateTime.parse(timeStr, formatter);
        } catch (Exception e) {
            System.out.println("⚠️ Invalid date/time format!");
            return;
        }

        System.out.print("Enter available seats: ");
        int seats = readInt(sc);

        System.out.print("Enter fare: ");
        double fare;
        try {
            fare = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid fare!");
            return;
        }

        rideService.offerRide(driver, source, dest, time, seats, fare);
        System.out.println("✅ Ride offered successfully!");
    }

    private static void viewDriverRides(User driver, RideService rideService) {
        List<Ride> myRides = rideService.getAllRidesByDriver(driver.getId());
        System.out.println("📋 Your Offered Rides:");
        if (myRides.isEmpty()) System.out.println("No rides offered yet.");
        else myRides.forEach(System.out::println);
    }

    // ----------------- PASSENGER MENU -----------------
    private static void passengerMenu(Scanner sc, User passenger, RideService rideService) {
        while (true) {
            System.out.println("\n--- Passenger Menu ---");
            System.out.println("1. Search Rides");
            System.out.println("2. Book a Ride");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1 -> searchRides(sc, rideService);
                case 2 -> bookRide(sc, rideService, passenger);
                case 3 -> { System.out.println("🔙 Logging out..."); return; }
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void searchRides(Scanner sc, RideService rideService) {
        System.out.print("Enter pickup point: ");
        String source = sc.nextLine();
        System.out.print("Enter destination point: ");
        String dest = sc.nextLine();

        List<Ride> found = rideService.searchRides(source, dest);
        System.out.println("📋 Available Rides:");
        if (found.isEmpty()) System.out.println("No rides found.");
        else found.forEach(System.out::println);
    }

    private static void bookRide(Scanner sc, RideService rideService, User passenger) {
        System.out.print("Enter ride ID to book: ");
        int rideId = readInt(sc);

        Ride ride = rideService.getRideById(rideId);
        if (ride == null) {
            System.out.println("⚠️ Ride not found!");
            return;
        }

        try {
            rideService.bookSeat(ride, passenger);
            System.out.println("✅ Seat booked successfully!");
        } catch (SeatUnavailableException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}
