package com.quickmart.product.service;

import com.quickmart.product.model.CheckoutDetails;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private static final String CHECKOUT_DIR = "checkout_records";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private CheckoutDetails latestOrder;

    public void saveCheckoutDetails(CheckoutDetails details) throws IOException {
        // Create directory if it doesn't exist
        Path directory = Paths.get(CHECKOUT_DIR);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Create filename with timestamp
        String filename = String.format("%s/checkout_%s.txt",
                CHECKOUT_DIR,
                details.getCheckoutTime().format(DATE_FORMATTER));

        // Save details to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(details.toString());
        }

        // Store the latest order
        this.latestOrder = details;
    }

    public CheckoutDetails getLatestOrder() {
        return latestOrder;
    }
} 