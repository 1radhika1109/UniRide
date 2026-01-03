package org.example.util;

import org.example.model.User;
import org.example.model.Ride;
import org.example.model.Booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    // Export bookings to CSV
    public static void exportBookings(List<Booking> bookings, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("BookingID,PassengerName,RideID,Source,Destination,SeatsBooked,Fare,Status\n");
            for (Booking b : bookings) {
                writer.write(String.format("%d,%s,%d,%s,%s,%d,%.2f,%s\n",
                        b.getBookingId(),
                        b.getPassenger().getName(),
                        b.getRide().getRideId(),
                        b.getRide().getSource(),
                        b.getRide().getDestination(),
                        b.getSeatsBooked(),
                        b.getRide().getFare(),
                        b.getStatus()
                ));
            }
            System.out.println("✅ Bookings exported to " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Error exporting bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Export rides to CSV
    public static void exportRides(List<Ride> rides, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("RideID,DriverName,Source,Destination,RideDate,AvailableSeats,Fare\n");
            for (Ride r : rides) {
                writer.write(String.format("%d,%s,%s,%s,%s,%d,%.2f\n",
                        r.getRideId(),
                        r.getDriver().getName(),
                        r.getSource(),
                        r.getDestination(),
                        r.getRideDateTime().toString(),
                        r.getAvailableSeats(),
                        r.getFare()
                ));
            }
            System.out.println("✅ Rides exported to " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Error exporting rides: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Import users from CSV
    public static List<User> importUsers(String fileName) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    User user = new User();
                    user.setName(data[0].trim());
                    user.setEmail(data[1].trim());
                    user.setPassword(data[2].trim());
                    user.setRole(data.length > 3 ? data[3].trim() : "PASSENGER");
                    users.add(user);
                }
            }
            System.out.println("✅ Users imported from " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Error importing users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
}
