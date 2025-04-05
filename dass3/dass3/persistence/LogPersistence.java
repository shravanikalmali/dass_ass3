
// File: persistence/LogPersistence.java
package yada.persistence;

import yada.model.food.Food;
import yada.model.food.FoodDatabase;
import yada.model.log.DailyLog;
import yada.model.log.DailyLogManager;
import yada.model.log.FoodEntry;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Handles persistence for daily logs
 */
public class LogPersistence {
    private String filename;
    private FoodDatabase foodDatabase;
    
    public LogPersistence(String filename) {
        this.filename = filename;
    }
    
    public DailyLogManager loadLogs() {
        DailyLogManager logManager = new DailyLogManager();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue;
                }
                
                try {
                    LocalDate date = LocalDate.parse(parts[0]);
                    DailyLog log = new DailyLog(date);
                    
                    // Parse entries if any
                    if (parts.length > 1 && !parts[1].isEmpty()) {
                        // We'll populate entries when we have access to the food database
                        // This happens when the controller initializes
                    }
                    
                    logManager.addLog(log);
                } catch (Exception e) {
                    System.err.println("Error parsing log entry: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            // No logs yet, that's fine
        } catch (IOException e) {
            System.err.println("Error reading logs: " + e.getMessage());
        }
        
        return logManager;
    }
    
    public void populateLogEntries(DailyLogManager logManager, FoodDatabase foodDatabase) {
        this.foodDatabase = foodDatabase;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue;
                }
                
                try {
                    LocalDate date = LocalDate.parse(parts[0]);
                    DailyLog log = logManager.getOrCreateLog(date);
                    
                    // Parse entries if any
                    if (parts.length > 1 && !parts[1].isEmpty()) {
                        String[] entries = parts[1].split(",");
                        for (String entry : entries) {
                            String[] entryParts = entry.split(":");
                            if (entryParts.length == 2) {
                                String foodName = entryParts[0];
                                int servings = Integer.parseInt(entryParts[1]);
                                
                                Food food = foodDatabase.getFood(foodName);
                                if (food != null) {
                                    log.addEntry(new FoodEntry(food, servings));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing log entry: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            // No logs yet, that's fine
        } catch (IOException e) {
            System.err.println("Error reading logs: " + e.getMessage());
        }
    }
    
    public void saveLogs(DailyLogManager logManager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (DailyLog log : logManager.getAllLogs()) {
                writer.write(log.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving logs: " + e.getMessage());
        }
    }
}
