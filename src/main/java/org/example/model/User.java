package org.example.model;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private boolean isDriver;
    private String vehicleInfo;

    // Constructor
    public User(int userId, String name, String email, String password, boolean isDriver, String vehicleInfo) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isDriver = isDriver;
        this.vehicleInfo = vehicleInfo;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isDriver() { return isDriver; }
    public String getVehicleInfo() { return vehicleInfo; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isDriver=" + isDriver +
                ", vehicleInfo='" + vehicleInfo + '\'' +
                '}';
    }
}
