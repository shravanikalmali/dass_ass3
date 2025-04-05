
// File: model/DietProfile.java
package yada.model;

/**
 * Represents a user's diet profile with calculation methods for target calories
 */
public class DietProfile {
    private String gender;
    private double height; // in cm
    private int age;
    private double weight; // in kg
    private int activityLevel; // 1-5
    private int calculationMethod; // 1 = Harris-Benedict, 2 = Mifflin-St Jeor
    
    public DietProfile() {
        this.gender = "M";
        this.height = 170.0;
        this.age = 30;
        this.weight = 70.0;
        this.activityLevel = 2;
        this.calculationMethod = 1;
    }
    
    public DietProfile(String gender, double height, int age, double weight, int activityLevel, int calculationMethod) {
        this.gender = gender;
        this.height = height;
        this.age = age;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.calculationMethod = calculationMethod;
    }
    
    public int calculateTargetCalories() {
        double bmr;
        
        if (calculationMethod == 1) {
            // Harris-Benedict Equation
            if (gender.equalsIgnoreCase("M")) {
                bmr = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
            } else {
                bmr = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
            }
        } else {
            // Mifflin-St Jeor Equation
            if (gender.equalsIgnoreCase("M")) {
                bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
            } else {
                bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
            }
        }
        
        // Apply activity factor
        double activityFactor;
        switch (activityLevel) {
            case 1: activityFactor = 1.2; break;   // Sedentary
            case 2: activityFactor = 1.375; break; // Lightly active
            case 3: activityFactor = 1.55; break;  // Moderately active
            case 4: activityFactor = 1.725; break; // Very active
            case 5: activityFactor = 1.9; break;   // Extremely active
            default: activityFactor = 1.2; break;
        }
        
        return (int)(bmr * activityFactor);
    }

    // Getters and setters
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public int getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(int calculationMethod) {
        this.calculationMethod = calculationMethod;
    }
}
