
// File: model/food/BasicFood.java
package yada.model.food;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a basic food item
 */
public class BasicFood implements Food {
    private String name;
    private List<String> keywords;
    private int caloriesPerServing;
    
    public BasicFood(String name, List<String> keywords, int caloriesPerServing) {
        this.name = name;
        this.keywords = new ArrayList<>(keywords);
        this.caloriesPerServing = caloriesPerServing;
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
        return caloriesPerServing;
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
    
    @Override
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("B|").append(name).append("|");
        
        for (int i = 0; i < keywords.size(); i++) {
            sb.append(keywords.get(i));
            if (i < keywords.size() - 1) {
                sb.append(",");
            }
        }
        
        sb.append("|").append(caloriesPerServing);
        return sb.toString();
    }
}
