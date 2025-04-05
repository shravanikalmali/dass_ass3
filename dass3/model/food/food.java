
// File: model/food/Food.java
package yada.model.food;

import java.util.List;

/**
 * Interface representing a food item (basic or composite)
 */
public interface Food {
    String getName();
    List<String> getKeywords();
    int getCaloriesPerServing();
    boolean matchesKeyword(String keyword);
    boolean matchesAllKeywords(List<String> keywords);
    boolean matchesAnyKeyword(List<String> keywords);
    String toFileString();
}
