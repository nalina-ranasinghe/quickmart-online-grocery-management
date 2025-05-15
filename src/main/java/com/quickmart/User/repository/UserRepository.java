package com.quickmart.User.repository;

import com.quickmart.User.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends FileRepository<User> {
    private static final String USER_FILE = "users.txt";

    public UserRepository() {
        super(USER_FILE);
    }

    @Override
    protected User parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 5) {
            User user = new User();
            user.setUsername(parts[0].trim());
            user.setPassword(parts[1].trim());
            user.setEmail(parts[2].trim());
            user.setMobile(parts[3].trim());
            user.setRole(parts[4].trim());
            return user;
        }
        return null;
    }

    @Override
    protected String formatLine(User user) {
        return user.toString();
    }

    // Create operation
    public boolean saveUser(User user) {
        List<String> lines = readAllLines();
        // Check if user already exists
        for (String line : lines) {
            User existingUser = parseLine(line);
            if (existingUser != null && existingUser.getUsername().equals(user.getUsername())) {
                return false; // User already exists
            }
        }
        lines.add(formatLine(user));
        writeAllLines(lines);
        return true;
    }

    // Read operation
    public User findByUsername(String username) {
        List<String> lines = readAllLines();
        for (String line : lines) {
            User user = parseLine(line);
            if (user != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Update operation
    public boolean updateUser(User updatedUser) {
        List<String> lines = readAllLines();
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            User user = parseLine(line);
            if (user != null && user.getUsername().equals(updatedUser.getUsername())) {
                updatedLines.add(formatLine(updatedUser));
                found = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (found) {
            writeAllLines(updatedLines);
            return true;
        }
        return false;
    }

    // Delete operation
    public boolean deleteUser(String username) {
        List<String> lines = readAllLines();
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            User user = parseLine(line);
            if (user != null && !user.getUsername().equals(username)) {
                updatedLines.add(line);
            } else if (user != null) {
                found = true;
            }
        }

        if (found) {
            writeAllLines(updatedLines);
            return true;
        }
        return false;
    }

    // List all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        List<String> lines = readAllLines();
        for (String line : lines) {
            User user = parseLine(line);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }
} 