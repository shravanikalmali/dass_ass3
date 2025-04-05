
// File: model/log/DailyLogManager.java
package yada.model.log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages daily logs across multiple dates
 */
public class DailyLogManager {
    private Map<LocalDate, DailyLog> logs;
    
    public DailyLogManager() {
        logs = new HashMap<>();
    }
    
    public DailyLog getOrCreateLog(LocalDate date) {
        if (!logs.containsKey(date)) {
            logs.put(date, new DailyLog(date));
        }
        return logs.get(date);
    }
    
    public List<DailyLog> getAllLogs() {
        return new ArrayList<>(logs.values());
    }
    
    public void addLog(DailyLog log) {
        logs.put(log.getDate(), log);
    }
}
