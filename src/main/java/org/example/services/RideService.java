package org.example.services;

import org.example.model.Ride;
import org.example.model.User;
import org.example.exception.SeatUnavailableException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RideService {
    private List<Ride> rides = new ArrayList<>();
    private int rideIdCounter = 1;

    // Driver offers a ride
    public Ride offerRide(User driver, String source, String destination, LocalDateTime time, int seats) {
        if (!driver.isDriver()) {
            throw new RuntimeException("Only drivers can offer rides!");
        }
        Ride ride = new Ride(rideIdCounter++, driver, source, destination, time, seats);
        rides.add(ride);
        return ride;
    }

    // Search rides by source & destination
    public List<Ride> searchRides(String source, String destination) {
        return rides.stream()
                .filter(r -> r.getSource().equalsIgnoreCase(source)
                        && r.getDestination().equalsIgnoreCase(destination)
                        && r.getSeatsAvailable() > 0)
                .collect(Collectors.toList());
    }

    // Book a seat in a ride
    public void bookSeat(Ride ride) {
        if (ride.getSeatsAvailable() <= 0) {
            throw new SeatUnavailableException("No seats available in this ride!");
        }
        ride.bookSeat();
    }

    // Show all rides (for debugging)
    public List<Ride> getAllRides() {
        return rides;
    }
}
