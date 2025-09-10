package org.example.services;

import org.example.model.User;
import org.example.exception.InvalidLoginException;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users = new HashMap<>();
    private int userIdCounter = 1;

    // Register new user
    public User registerUser(String name, String email, String password, boolean isDriver, String vehicleInfo) {
        if (users.containsKey(email)) {
            throw new RuntimeException("User already exists with this email!");
        }
        User user = new User(userIdCounter++, name, email, password, isDriver, vehicleInfo);
        users.put(email, user);
        return user;
    }

    // Login user
    public User login(String email, String password) {
        User user = users.get(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidLoginException("Invalid email or password!");
        }
        return user;
    }

    // For testing: get all users
    public Map<String, User> getAllUsers() {
        return users;
    }
}