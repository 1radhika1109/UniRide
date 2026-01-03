package org.example.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String role; // NEW: role (like DRIVER or PASSENGER)

    @Column(name = "is_driver")
    private boolean isDriver;

    private String vehicleInfo;

    // One-to-many: User → Rides
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ride> ridesOffered;

    // One-to-many: User → Bookings
    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public User() {}

    public User(String name, String email, String password, String role, boolean isDriver, String vehicleInfo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isDriver = isDriver;
        this.vehicleInfo = vehicleInfo;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public boolean isDriver() { return isDriver; }
    public String getVehicleInfo() { return vehicleInfo; }
    public List<Ride> getRidesOffered() { return ridesOffered; }
    public List<Booking> getBookings() { return bookings; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setDriver(boolean driver) { isDriver = driver; }
    public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }
    public void setRidesOffered(List<Ride> ridesOffered) { this.ridesOffered = ridesOffered; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", isDriver=" + isDriver +
                ", vehicleInfo='" + vehicleInfo + '\'' +
                '}';
    }
}
