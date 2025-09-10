package org.example.model;

public class Booking {
    private int bookingId;
    private Ride ride;
    private User passenger;
    private String status; // "CONFIRMED" or "CANCELLED"

    public Booking(int bookingId, Ride ride, User passenger, String status) {
        this.bookingId = bookingId;
        this.ride = ride;
        this.passenger = passenger;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public Ride getRide() { return ride; }
    public User getPassenger() { return passenger; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", ride=" + ride.getRideId() +
                ", passenger=" + passenger.getName() +
                ", status='" + status + '\'' +
                '}';
    }
}
