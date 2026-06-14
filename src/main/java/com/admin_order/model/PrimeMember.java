package com.admin_order.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prime_members")
public class PrimeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    // SILVER = 5%, GOLD = 10%, PLATINUM = 20%
    private String tier; // "SILVER", "GOLD", "PLATINUM"

    private Integer points;

    private Double discountRate;

    private String status; // "ACTIVE", "SUSPENDED"

    public PrimeMember() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTier() { return tier; }
    public void setTier(String tier) {
        this.tier = tier;
        // Auto-set discount rate based on tier
        if ("PLATINUM".equalsIgnoreCase(tier)) {
            this.discountRate = 20.0;
        } else if ("GOLD".equalsIgnoreCase(tier)) {
            this.discountRate = 10.0;
        } else {
            this.discountRate = 5.0; // SILVER default
        }
    }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Double getDiscountRate() { return discountRate; }
    public void setDiscountRate(Double discountRate) { this.discountRate = discountRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
