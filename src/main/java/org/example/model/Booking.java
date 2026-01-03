package org.example.model;

import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User passenger;

    private int seatsBooked;
    private String status;

    public Booking() {} // default constructor for Hibernate

    public Booking(Ride ride, User passenger, int seatsBooked, String status) {
        this.ride = ride;
        this.passenger = passenger;
        this.seatsBooked = seatsBooked;
        this.status = status;
    }

    // ===== Getters =====
    public int getBookingId() { return bookingId; }
    public Ride getRide() { return ride; }
    public User getPassenger() { return passenger; }
    public int getSeatsBooked() { return seatsBooked; }
    public String getStatus() { return status; }

    // ===== Setters =====
    public void setRide(Ride ride) { this.ride = ride; }
    public void setPassenger(User passenger) { this.passenger = passenger; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", ride=" + (ride != null ? ride.getRideId() : "null") +
                ", passenger=" + (passenger != null ? passenger.getName() : "null") +
                ", seatsBooked=" + seatsBooked +
                ", status='" + status + '\'' +
                '}';
    }
}
