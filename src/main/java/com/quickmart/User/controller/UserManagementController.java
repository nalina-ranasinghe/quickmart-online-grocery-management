package com.quickmart.User.controller;

import com.quickmart.User.model.User;
import com.quickmart.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserManagementController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{username}")
    @ResponseBody
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        // Get existing user to preserve password
        User existingUser = userService.getUserByUsername(username);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Preserve the password from existing user
        updatedUser.setPassword(existingUser.getPassword());
        
        // If role is not provided, preserve existing role
        if (updatedUser.getRole() == null || updatedUser.getRole().trim().isEmpty()) {
            updatedUser.setRole(existingUser.getRole());
        }

        if (userService.updateUser(updatedUser)) {
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.badRequest().body("Failed to update user");
    }

    @DeleteMapping("/{username}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if (userService.deleteUser(username)) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.badRequest().body("Failed to delete user");
    }

    @GetMapping("/management")
    public String userManagementPage() {
        return "user-management";
    }
} 