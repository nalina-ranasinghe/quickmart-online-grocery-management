package com.quickmart.User.service;

import com.quickmart.User.model.User;
import com.quickmart.User.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public boolean registerUser(User user) {
        return userRepository.saveUser(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public boolean deleteUser(String username) {
        return userRepository.deleteUser(username);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User authenticateUser(String usernameOrEmail, String password) {
        try {
            System.out.println("\n=== Authentication Attempt ===");
            System.out.println("Attempting to authenticate with: [" + usernameOrEmail + "]");
            System.out.println("Password length: " + password.length());
            
            // Debug: Print raw file contents
            debugFileContents();
            
            // Check if users.txt exists
            File userFile = new File("users.txt");
            if (!userFile.exists()) {
                System.out.println("Error: users.txt file does not exist");
                return null;
            }
            
            // List all users for debugging
            System.out.println("\nCurrent users in system:");
            listAllUsers();
            
            // Try to find user by username first
            System.out.println("\nTrying to find user by username...");
            User user = User.findByUsername(usernameOrEmail);
            
            // If not found by username, try to find by email
            if (user == null) {
                System.out.println("\nUser not found by username, trying email...");
                user = User.findByEmail(usernameOrEmail);
            }
            
            if (user == null) {
                System.out.println("\nAuthentication failed: User not found");
                return null;
            }
            
            // Debug password comparison
            System.out.println("\nFound user. Comparing passwords:");
            System.out.println("Stored password length: " + user.getPassword().length());
            System.out.println("Entered password length: " + password.length());
            System.out.println("Stored password: [" + user.getPassword() + "]");
            System.out.println("Entered password: [" + password + "]");
            
            // Compare passwords
            boolean passwordMatch = user.getPassword().equals(password);
            System.out.println("Password match result: " + passwordMatch);
            
            if (passwordMatch) {
                System.out.println("\nAuthentication successful for: " + usernameOrEmail);
                return user;
            } else {
                System.out.println("\nAuthentication failed: Password mismatch");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Debug method to list all users
    public void listAllUsers() {
        try {
            File userFile = new File("users.txt");
            if (!userFile.exists()) {
                System.out.println("No users file exists yet.");
                return;
            }

            System.out.println("\n=== Current Users in System ===");
            for (User user : User.getAllUsers()) {
                System.out.println("Username: " + user.getUsername() + 
                                 ", Password: [" + user.getPassword() + "]" +
                                 ", Email: " + user.getEmail());
            }
            System.out.println("=== End of User List ===\n");
        } catch (IOException e) {
            System.err.println("Error listing users: " + e.getMessage());
        }
    }

    // Debug method to print raw file contents
    private void debugFileContents() {
        try {
            File userFile = new File("users.txt");
            if (!userFile.exists()) {
                System.out.println("No users.txt file exists");
                return;
            }

            System.out.println("\n=== Raw File Contents ===");
            List<String> lines = Files.readAllLines(Paths.get("users.txt"));
            for (int i = 0; i < lines.size(); i++) {
                System.out.println("Line " + (i + 1) + ": [" + lines.get(i) + "]");
            }
            System.out.println("=== End of File Contents ===\n");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
} 