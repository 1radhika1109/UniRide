package org.example.services;

import org.example.dao.UserDAO;
import org.example.dao.RideDAO;
import org.example.dao.BookingDAO;
import org.example.exception.InvalidLoginException;
import org.example.model.Ride;
import org.example.model.User;
import org.example.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDao;
    private final RideDAO rideDao;
    private final BookingDAO bookingDao;

    // Constructor Injection
    public UserService(UserDAO userDao, RideDAO rideDao, BookingDAO bookingDao) {
        this.userDao = userDao;
        this.rideDao = rideDao;
        this.bookingDao = bookingDao;
    }

    // Register user
    public User registerUser(String name, String email, String password, String role, String vehicleInfo) {
        List<User> existingUsers = userDao.getAllUsers();
        for (User u : existingUsers) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new RuntimeException("User already exists with this email!");
            }
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setVehicleInfo(vehicleInfo);
        user.setDriver(role.equalsIgnoreCase("DRIVER"));

        userDao.saveUser(user);
        return user;
    }

    // Login user
    public User login(String email, String password) throws InvalidLoginException {
        User user = userDao.login(email, password);
        if (user == null) {
            throw new InvalidLoginException("Invalid email or password!");
        }
        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    // Get all rides offered by a driver
    public List<Ride> getRidesByDriver(int driverId) {
        return rideDao.getAllRidesByDriver(driverId);
    }

    // Get all rides booked by a passenger
    public List<Ride> getBookedRides(int passengerId) {
        List<Booking> bookings = bookingDao.getBookingsByPassenger(passengerId);
        return bookings.stream()
                .map(Booking::getRide)
                .collect(Collectors.toList());
    }
}
