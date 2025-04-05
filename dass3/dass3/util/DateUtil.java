
// File: util/DateUtil.java
package yada.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date-related operations
 */
public class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}
