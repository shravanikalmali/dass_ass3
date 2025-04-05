
// File: persistence/ProfilePersistence.java
package yada.persistence;

import yada.model.DietProfile;

import java.io.*;

/**
 * Handles persistence for user profile
 */
public class ProfilePersistence {
    private String filename;
    
    public ProfilePersistence(String filename) {
        this.filename = filename;
    }
    
    public DietProfile loadProfile() {
        DietProfile profile = new DietProfile();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("=");
                if (parts.length != 2) {
                    continue;
                }
                
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                switch (key) {
                    case "gender":
                        profile.setGender(value);
                        break;
                    case "height":
                        profile.setHeight(Double.parseDouble(value));
                        break;
                    case "age":
                        profile.setAge(Integer.parseInt(value));
                        break;
                    case "weight":
                        profile.setWeight(Double.parseDouble(value));
                        break;
                    case "activityLevel":
                        profile.setActivityLevel(Integer.parseInt(value));
                        break;
                    case "calculationMethod":
                        profile.setCalculationMethod(Integer.parseInt(value));
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            // No profile yet, use defaults
        } catch (IOException e) {
            System.err.println("Error reading profile: " + e.getMessage());
        }
        
        return profile;
    }
    
    public void saveProfile(DietProfile profile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("gender=" + profile.getGender());
            writer.newLine();
            writer.write("height=" + profile.getHeight());
            writer.newLine();
            writer.write("age=" + profile.getAge());
            writer.newLine();
            writer.write("weight=" + profile.getWeight());
            writer.newLine();
            writer.write("activityLevel=" + profile.getActivityLevel());
            writer.newLine();
            writer.write("calculationMethod=" + profile.getCalculationMethod());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving profile: " + e.getMessage());
        }
    }
}