package org.example.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rideId;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    private String source;
    private String destination;

    @Column(name = "ride_time")
    private LocalDateTime rideDateTime;

    @Column(name = "available_seats")
    private int seatsAvailable;

    private double fare;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public Ride() {}

    public Ride(User driver, String source, String destination, LocalDateTime rideDateTime, int seatsAvailable, double fare) {
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.rideDateTime = rideDateTime;
        this.seatsAvailable = seatsAvailable;
        this.fare = fare;
    }

    // ===== Getters =====
    public int getRideId() { return rideId; }
    public User getDriver() { return driver; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public LocalDateTime getRideDateTime() { return rideDateTime; }
    public int getAvailableSeats() { return seatsAvailable; }
    public double getFare() { return fare; }
    public List<Booking> getBookings() { return bookings; }

    // ===== Setters =====
    public void setDriver(User driver) { this.driver = driver; }
    public void setSource(String source) { this.source = source; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setRideDateTime(LocalDateTime rideDateTime) { this.rideDateTime = rideDateTime; }
    public void setAvailableSeats(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }
    public void setFare(double fare) { this.fare = fare; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public void bookSeat() {
        if (seatsAvailable > 0) seatsAvailable--;
        else throw new RuntimeException("No seats available!");
    }

    @Override
    public String toString() {
        return "Ride{" +
                "rideId=" + rideId +
                ", driver=" + (driver != null ? driver.getName() : "null") +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", rideDateTime=" + rideDateTime +
                ", seatsAvailable=" + seatsAvailable +
                ", fare=" + fare +
                '}';
    }
}
