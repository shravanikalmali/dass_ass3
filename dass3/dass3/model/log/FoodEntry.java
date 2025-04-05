
// File: model/log/FoodEntry.java
package yada.model.log;

import yada.model.food.Food;

/**
 * Represents a food entry in the daily log
 */
public class FoodEntry {
    private Food food;
    private int servings;
    
    public FoodEntry(Food food, int servings) {
        this.food = food;
        this.servings = servings;
    }
    
    public Food getFood() {
        return food;
    }
    
    public int getServings() {
        return servings;
    }
    
    public int getTotalCalories() {
        return food.getCaloriesPerServing() * servings;
    }
    
    public String toFileString() {
        return food.getName() + ":" + servings;
    }
}
