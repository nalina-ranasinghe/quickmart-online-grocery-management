package com.quickmart.User.model;

import lombok.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String username;
    private String password;
    private String email;
    private String role;
    private String mobile;

    public User() {}

    public User(String username, String password, String email, String mobile) {
        this.username = username.trim();
        this.password = password.trim();
        this.email = email.trim();
        this.mobile = mobile.trim();
        this.role = "USER";
    }

    // File operations
    private static final String USER_FILE = "users.txt";

    public static void saveUser(User user) throws IOException {
        System.out.println("\n=== Saving User ===");
        System.out.println("Username: [" + user.getUsername() + "]");
        System.out.println("Email: [" + user.getEmail() + "]");
        System.out.println("Password length: " + user.getPassword().length());
        
        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("Creating new users.txt file");
            file.createNewFile();
        }
        
        // Validate user data before saving
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Trim and normalize all fields
        String username = user.getUsername().trim();
        String password = user.getPassword().trim();
        String email = user.getEmail().trim().toLowerCase();
        String mobile = user.getMobile().trim();
        String role = user.getRole().trim();

        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        String userData = String.format("%s,%s,%s,%s,%s", username, password, email, mobile, role);
        System.out.println("Saving user data: [" + userData + "]");

        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(userData);
            System.out.println("User data saved successfully");
            
            // Verify the data was saved correctly
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String lastLine = null;
                String line;
                while ((line = reader.readLine()) != null) {
                    lastLine = line;
                }
                if (lastLine != null && lastLine.equals(userData)) {
                    System.out.println("Verified: Data was saved correctly");
                } else {
                    System.out.println("Warning: Data verification failed");
                }
            }
        }
    }

    public static User findByUsername(String username) throws IOException {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Invalid username provided");
            return null;
        }

        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("User file does not exist");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equals(username.trim())) {
                    User user = new User();
                    user.setUsername(parts[0].trim());
                    user.setPassword(parts[1].trim());
                    user.setEmail(parts[2].trim());
                    user.setMobile(parts[3].trim());
                    user.setRole(parts[4].trim());
                    System.out.println("Found user in file: [" + line + "]");
                    return user;
                }
            }
        }
        System.out.println("User not found in file: [" + username + "]");
        return null;
    }

    public static User findByEmail(String email) throws IOException {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Invalid email provided");
            return null;
        }

        String searchEmail = email.trim().toLowerCase();
        System.out.println("\n=== Email Search ===");
        System.out.println("Searching for email: [" + searchEmail + "]");

        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("User file does not exist");
            return null;
        }

        System.out.println("\nReading users.txt file:");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    System.out.println("Line " + lineNumber + ": [empty line]");
                    continue;
                }
                
                System.out.println("Line " + lineNumber + ": [" + line + "]");
                
                String[] parts = line.split(",");
                System.out.println("Number of parts: " + parts.length);
                
                if (parts.length >= 5) {
                    String storedEmail = parts[2].trim().toLowerCase();
                    System.out.println("Stored email: [" + storedEmail + "]");
                    System.out.println("Search email: [" + searchEmail + "]");
                    System.out.println("Emails match: " + storedEmail.equals(searchEmail));
                    
                    if (storedEmail.equals(searchEmail)) {
                        User user = new User();
                        user.setUsername(parts[0].trim());
                        user.setPassword(parts[1].trim());
                        user.setEmail(parts[2].trim());
                        user.setMobile(parts[3].trim());
                        user.setRole(parts[4].trim());
                        System.out.println("Found user by email in file: [" + line + "]");
                        return user;
                    }
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        }
        System.out.println("\nUser not found with email: [" + searchEmail + "]");
        return null;
    }

    public static List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("No users file exists yet.");
            return users;
        }

        System.out.println("\n=== Reading All Users ===");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                System.out.println("\nLine " + lineNumber + ": [" + line + "]");
                
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    User user = new User();
                    user.setUsername(parts[0].trim());
                    user.setPassword(parts[1].trim());
                    user.setEmail(parts[2].trim());
                    user.setMobile(parts[3].trim());
                    user.setRole(parts[4].trim());
                    users.add(user);
                    System.out.println("Added user: " + user.getUsername() + " (" + user.getEmail() + ")");
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        }
        System.out.println("Total users found: " + users.size());
        return users;
    }

    // Helper method to validate user data
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               email != null && !email.trim().isEmpty();
    }

    @Override
    public String toString() {
        return username + "," + password + "," + email + "," + mobile + "," + role;
    }
} 