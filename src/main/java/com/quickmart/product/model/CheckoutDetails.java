package com.quickmart.product.model;

import java.time.LocalDateTime;

public class CheckoutDetails {
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private String cardNumber;
    private String expiryDate;
    private String cardName;
    private double totalAmount;
    private LocalDateTime checkoutTime;

    // Default constructor for JSON deserialization
    public CheckoutDetails() {
        this.checkoutTime = LocalDateTime.now();
    }

    // Constructor
    public CheckoutDetails(String fullName, String phone, String address, String city, 
                         String zipCode, String cardNumber, String expiryDate, 
                         String cardName, double totalAmount) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.cardNumber = maskCardNumber(cardNumber); // Mask card number for security
        this.expiryDate = expiryDate;
        this.cardName = cardName;
        this.totalAmount = totalAmount;
        this.checkoutTime = LocalDateTime.now();
    }

    // Private method to mask card number
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = maskCardNumber(cardNumber); }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCheckoutTime() { return checkoutTime; }
    public void setCheckoutTime(LocalDateTime checkoutTime) { this.checkoutTime = checkoutTime; }

    @Override
    public String toString() {
        return String.format("""
            Checkout Details:
            ----------------
            Customer: %s
            Phone: %s
            Address: %s
            City: %s
            ZIP: %s
            Card: %s
            Expiry: %s
            Amount: $%.2f
            Time: %s
            """, 
            fullName, phone, address, city, zipCode, 
            cardNumber, expiryDate, totalAmount, checkoutTime);
    }
} 