
// File: model/log/DailyLog.java
package yada.model.log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a daily food consumption log
 */
public class DailyLog {
    private LocalDate date;
    private List<FoodEntry> entries;
    
    public DailyLog(LocalDate date) {
        this.date = date;
        this.entries = new ArrayList<>();
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void addEntry(FoodEntry entry) {
        entries.add(entry);
    }
    
    public void removeEntry(FoodEntry entry) {
        entries.remove(entry);
    }
    
    public List<FoodEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    
    public int getTotalCalories() {
        int total = 0;
        for (FoodEntry entry : entries) {
            total += entry.getTotalCalories();
        }
        return total;
    }
    
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(date).append("|");
        
        for (int i = 0; i < entries.size(); i++) {
            sb.append(entries.get(i).toFileString());
            if (i < entries.size() - 1) {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }
}
