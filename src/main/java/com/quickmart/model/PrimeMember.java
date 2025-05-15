package com.quickmart.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PrimeMember {
    private String id;  // Changed from Long to String to match PM001 format

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^0\\d{9}$", message = "Contact number must be exactly 10 digits and start with 0")
    private String contactNumber;

    @Min(value = 3000, message = "Points must be at least 3000 for prime members")
    private int points;
    private String tier;
    private double discount;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void updateTierBasedOnPoints() {
        if (points >= 10000) {
            tier = "PLATINUM";
            discount = 0.10;
        } else if (points >= 5000) {
            tier = "GOLD";
            discount = 0.05;
        } else if (points >= 1000) {
            tier = "SILVER";
            discount = 0.03;
        } else {
            tier = "STANDARD";
            discount = 0.00;
        }
    }

    public void addPointsFromPurchase(double purchaseAmount) {
        // Add 50 points for every 1000 LKR spent
        int pointsToAdd = (int) (purchaseAmount / 1000 * 50);
        points += pointsToAdd;
        updateTierBasedOnPoints();
    }
}