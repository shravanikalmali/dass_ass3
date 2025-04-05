
// File: model/food/FoodDatabase.java
package yada.model.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintains a database of all available foods
 */
public class FoodDatabase {
    private Map<String, Food> foods;
    
    public FoodDatabase() {
        foods = new HashMap<>();
    }
    
    public void addFood(Food food) {
        foods.put(food.getName(), food);
    }
    
    public Food getFood(String name) {
        return foods.get(name);
    }
    
    public void removeFood(String name) {
        foods.remove(name);
    }
    
    public List<Food> getAllFoods() {
        return new ArrayList<>(foods.values());
    }
    
    public List<Food> searchFoods(List<String> keywords, boolean matchAll) {
        List<Food> results = new ArrayList<>();
        
        for (Food food : foods.values()) {
            boolean matches;
            if (matchAll) {
                matches = food.matchesAllKeywords(keywords);
            } else {
                matches = food.matchesAnyKeyword(keywords);
            }
            
            if (matches) {
                results.add(food);
            }
        }
        
        return results;
    }
    
    public List<String> getFoodNames() {
        return new ArrayList<>(foods.keySet());
    }
}
