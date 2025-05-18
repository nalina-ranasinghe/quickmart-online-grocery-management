package com.quickmart.User.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class FileRepository<T> {
    protected String filePath;

    public FileRepository(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
    }

    protected void createFileIfNotExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }
    }

    protected List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    protected void writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Abstract methods to be implemented by child classes
    protected abstract T parseLine(String line);
    protected abstract String formatLine(T item);
} 