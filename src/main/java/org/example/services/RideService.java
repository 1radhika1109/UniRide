package org.example.services;

import org.example.dao.BookingDAO;
import org.example.dao.RideDAO;
import org.example.exception.SeatUnavailableException;
import org.example.model.Booking;
import org.example.model.Ride;
import org.example.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RideService {

    private final RideDAO rideDao;
    private final BookingDAO bookingDao;

    // Constructor
    public RideService(RideDAO rideDao, BookingDAO bookingDao) {
        this.rideDao = rideDao;
        this.bookingDao = bookingDao;
    }

    // ---------------- OFFER RIDE ----------------
    public Ride offerRide(User driver, String source, String destination, LocalDateTime time, int seats, double fare) {
        if (driver == null) throw new IllegalArgumentException("Driver cannot be null");
        if (!driver.isDriver()) throw new IllegalArgumentException("Only drivers can offer rides!");

        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setSource(source);
        ride.setDestination(destination);
        ride.setRideDateTime(time);
        ride.setAvailableSeats(seats);
        ride.setFare(fare);

        rideDao.saveRide(ride); // Hibernate save
        return ride;
    }

    // ---------------- SEARCH RIDES ----------------
    public List<Ride> searchRides(String sourceQuery, String destinationQuery) {
        if (sourceQuery == null) sourceQuery = "";
        if (destinationQuery == null) destinationQuery = "";
        return rideDao.searchRides(sourceQuery, destinationQuery);
    }

    // ---------------- BOOK SEAT ----------------
    public Booking bookSeat(Ride ride, User passenger) throws SeatUnavailableException {
        if (ride == null || passenger == null)
            throw new IllegalArgumentException("Ride and passenger cannot be null");

        if (ride.getAvailableSeats() <= 0) {
            throw new SeatUnavailableException("No seats available for this ride!");
        }

        // Reduce available seats
        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideDao.updateRide(ride); // persist updated seat count

        // Create and save booking
        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setSeatsBooked(1);
        booking.setStatus("CONFIRMED");

        bookingDao.saveBooking(booking);
        return booking;
    }

    // ---------------- GET RIDE BY ID ----------------
    public Ride getRideById(int rideId) {
        return rideDao.getRideById(rideId);
    }

    // ---------------- GET ALL RIDES ----------------
    public List<Ride> getAllRides() {
        return rideDao.getAllRides();
    }

    // ---------------- GET RIDES BY DRIVER ----------------
    public List<Ride> getAllRidesByDriver(int driverId) {
        return rideDao.getAllRidesByDriver(driverId);
    }

    // ---------------- UPDATE RIDE ----------------
    public void updateRide(Ride ride) {
        if (ride == null) throw new IllegalArgumentException("Ride cannot be null");
        rideDao.updateRide(ride);
    }

    // ---------------- CANCEL RIDE ----------------
    public void cancelRide(Ride ride) {
        if (ride == null) throw new IllegalArgumentException("Ride cannot be null");
        rideDao.deleteRide(ride);
    }
}
