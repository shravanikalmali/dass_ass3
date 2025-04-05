
// File: model/food/CompositeFood.java
package yada.model.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a composite food item made up of other foods
 */
public class CompositeFood implements Food {
    private String name;
    private List<String> keywords;
    private Map<Food, Integer> components; // Map of component foods and their serving counts
    
    public CompositeFood(String name, List<String> keywords) {
        this.name = name;
        this.keywords = new ArrayList<>(keywords);
        this.components = new HashMap<>();
    }
    
    public void addComponent(Food component, int servings) {
        components.put(component, servings);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public List<String> getKeywords() {
        return new ArrayList<>(keywords);
    }
    
    @Override
    public int getCaloriesPerServing() {
        int totalCalories = 0;
        for (Map.Entry<Food, Integer> entry : components.entrySet()) {
            Food component = entry.getKey();
            int servings = entry.getValue();
            totalCalories += component.getCaloriesPerServing() * servings;
        }
        return totalCalories;
    }
    
    @Override
    public boolean matchesKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        for (String k : keywords) {
            if (k.toLowerCase().contains(lowerKeyword)) {
                return true;
            }
        }
        return name.toLowerCase().contains(lowerKeyword);
    }
    
    
    // File: model/food/CompositeFood.java (continued)
    @Override
    public boolean matchesAllKeywords(List<String> keywords) {
        for (String keyword : keywords) {
            if (!matchesKeyword(keyword)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean matchesAnyKeyword(List<String> keywords) {
        for (String keyword : keywords) {
            if (matchesKeyword(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    public Map<Food, Integer> getComponents() {
        return new HashMap<>(components);
    }
    
    @Override
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("C|").append(name).append("|");
        
        for (int i = 0; i < keywords.size(); i++) {
            sb.append(keywords.get(i));
            if (i < keywords.size() - 1) {
                sb.append(",");
            }
        }
        
        sb.append("|");
        
        int count = 0;
        for (Map.Entry<Food, Integer> entry : components.entrySet()) {
            sb.append(entry.getKey().getName()).append(":").append(entry.getValue());
            count++;
            if (count < components.size()) {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }
}
