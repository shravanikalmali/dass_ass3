
// File: persistence/DatabasePersistence.java
package yada.persistence;

import yada.model.food.BasicFood;
import yada.model.food.CompositeFood;
import yada.model.food.Food;
import yada.model.food.FoodDatabase;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles persistence for the food database
 */
public class DatabasePersistence {
    private String filename;
    
    public DatabasePersistence(String filename) {
        this.filename = filename;
    }
    
    public FoodDatabase loadDatabase() {
        FoodDatabase database = new FoodDatabase();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\|");
                if (parts.length < 3) {
                    continue;
                }
                
                String type = parts[0];
                String name = parts[1];
                List<String> keywords = Arrays.asList(parts[2].split(","));
                
                if (type.equals("B") && parts.length >= 4) {
                    // Basic food
                    int calories = Integer.parseInt(parts[3]);
                    BasicFood food = new BasicFood(name, keywords, calories);
                    database.addFood(food);
                } else if (type.equals("C") && parts.length >= 4) {
                    // Composite food - need to handle this in a second pass
                    // since we need all basic foods loaded first
                    CompositeFood food = new CompositeFood(name, keywords);
                    
                    // For now, just add the empty composite food
                    database.addFood(food);
                }
            }
            
            // Second pass for composite foods to link components
            reader.close();
            BufferedReader secondReader = new BufferedReader(new FileReader(filename));
            while ((line = secondReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    continue;
                }
                
                String type = parts[0];
                String name = parts[1];
                
                if (type.equals("C")) {
                    String[] componentParts = parts[3].split(",");
                    CompositeFood compositeFood = (CompositeFood) database.getFood(name);
                    
                    if (compositeFood != null) {
                        for (String componentPart : componentParts) {
                            String[] compDetail = componentPart.split(":");
                            if (compDetail.length == 2) {
                                String foodName = compDetail[0];
                                int servings = Integer.parseInt(compDetail[1]);
                                
                                Food component = database.getFood(foodName);
                                if (component != null) {
                                    compositeFood.addComponent(component, servings);
                                }
                            }
                        }
                    }
                }
            }
            secondReader.close();
            
        } catch (FileNotFoundException e) {
            // Create initial database with sample foods
            createInitialDatabase(database);
        } catch (IOException e) {
            System.err.println("Error reading database: " + e.getMessage());
        }
        
        return database;
    }
    
    private void createInitialDatabase(FoodDatabase database) {
        // Sample basic foods
        database.addFood(new BasicFood("Apple", Arrays.asList("fruit", "sweet"), 95));
        database.addFood(new BasicFood("Banana", Arrays.asList("fruit", "sweet"), 105));
        database.addFood(new BasicFood("Chicken Breast", Arrays.asList("meat", "protein"), 165));
        database.addFood(new BasicFood("White Rice", Arrays.asList("grain", "carb"), 205));
        database.addFood(new BasicFood("Black Beans", Arrays.asList("legume", "protein"), 227));
        database.addFood(new BasicFood("Olive Oil", Arrays.asList("oil", "fat"), 119));
        database.addFood(new BasicFood("Cheddar Cheese", Arrays.asList("dairy", "protein"), 113));
        database.addFood(new BasicFood("Whole Wheat Bread", Arrays.asList("grain", "carb"), 69));
        database.addFood(new BasicFood("Peanut Butter", Arrays.asList("spread", "protein"), 188));
        database.addFood(new BasicFood("Spinach", Arrays.asList("vegetable", "leafy"), 7));
        database.addFood(new BasicFood("Egg", Arrays.asList("protein", "breakfast"), 78));
        database.addFood(new BasicFood("Milk", Arrays.asList("dairy", "drink"), 103));
        
        // Sample composite foods
        CompositeFood pbSandwich = new CompositeFood("PB Sandwich", Arrays.asList("sandwich", "lunch"));
        pbSandwich.addComponent(database.getFood("Whole Wheat Bread"), 2);
        pbSandwich.addComponent(database.getFood("Peanut Butter"), 2);
        database.addFood(pbSandwich);
        
        CompositeFood omelette = new CompositeFood("Spinach Omelette", Arrays.asList("breakfast", "protein"));
        omelette.addComponent(database.getFood("Egg"), 3);
        omelette.addComponent(database.getFood("Spinach"), 2);
        omelette.addComponent(database.getFood("Cheddar Cheese"), 1);
        database.addFood(omelette);
        
        // Save the initial database
        saveDatabase(database);
    }
    
    public void saveDatabase(FoodDatabase database) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Food food : database.getAllFoods()) {
                writer.write(food.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }
}
