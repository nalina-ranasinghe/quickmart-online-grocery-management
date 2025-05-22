package com.quickmart.product;

import com.quickmart.product.model.CheckoutDetails;
import com.quickmart.product.service.CheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);
    private final CartQueue cartQueue;
    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CartQueue cartQueue, CheckoutService checkoutService) {
        this.cartQueue = cartQueue;
        this.checkoutService = checkoutService;
    }

    @GetMapping
    public String showCheckout(Model model) {
        model.addAttribute("cartItems", cartQueue.getAllItems());
        model.addAttribute("totalItems", cartQueue.getTotalItems());
        model.addAttribute("totalPrice", cartQueue.getTotalPrice());
        return "checkout";
    }

    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<String> processCheckout(@RequestBody CheckoutDetails details) {
        try {
            logger.info("Processing checkout for customer: {}", details.getFullName());
            
            // Validate required fields
            if (details.getFullName() == null || details.getFullName().trim().isEmpty() ||
                details.getPhone() == null || details.getPhone().trim().isEmpty() ||
                details.getAddress() == null || details.getAddress().trim().isEmpty() ||
                details.getCity() == null || details.getCity().trim().isEmpty() ||
                details.getZipCode() == null || details.getZipCode().trim().isEmpty() ||
                details.getCardNumber() == null || details.getCardNumber().trim().isEmpty() ||
                details.getExpiryDate() == null || details.getExpiryDate().trim().isEmpty() ||
                details.getCardName() == null || details.getCardName().trim().isEmpty()) {
                
                logger.error("Missing required fields in checkout details");
                return ResponseEntity.badRequest().body("Missing required fields");
            }
            
            // Save checkout details
            checkoutService.saveCheckoutDetails(details);
            
            // Clear the cart after successful checkout
            cartQueue.clear();
            
            logger.info("Checkout processed successfully for customer: {}", details.getFullName());
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            logger.error("Error processing checkout: ", e);
            return ResponseEntity.internalServerError().body("Error processing checkout: " + e.getMessage());
        }
    }

    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(Model model) {
        logger.info("Showing order confirmation page");
        // Get the latest checkout details from the service
        CheckoutDetails latestOrder = checkoutService.getLatestOrder();
        if (latestOrder != null) {
            model.addAttribute("totalAmount", latestOrder.getTotalAmount());
            model.addAttribute("address", String.format("%s, %s, %s", 
                latestOrder.getAddress(), 
                latestOrder.getCity(), 
                latestOrder.getZipCode()));
            logger.info("Added order details to model: totalAmount={}, address={}", 
                latestOrder.getTotalAmount(), 
                latestOrder.getAddress());
        } else {
            logger.warn("No latest order found");
        }
        return "order-confirmation";
    }
} 