package org.example.model;

import java.time.LocalDateTime;

public class Ride {
    private int rideId;
    private User driver;
    private String source;
    private String destination;
    private LocalDateTime time;
    private int seatsAvailable;

    public Ride(int rideId, User driver, String source, String destination, LocalDateTime time, int seatsAvailable) {
        this.rideId = rideId;
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.seatsAvailable = seatsAvailable;
    }

    public int getRideId() { return rideId; }
    public User getDriver() { return driver; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public LocalDateTime getTime() { return time; }
    public int getSeatsAvailable() { return seatsAvailable; }

    public void bookSeat() {
        if (seatsAvailable > 0) {
            seatsAvailable--;
        }
    }

    @Override
    public String toString() {
        return "Ride{" +
                "rideId=" + rideId +
                ", driver=" + driver.getName() +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", time=" + time +
                ", seatsAvailable=" + seatsAvailable +
                '}';
    }
}
