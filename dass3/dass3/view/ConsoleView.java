
// File: view/ConsoleView.java
package yada.view;

import java.util.Scanner;

/**
 * Handles user interface for console interaction
 */
public class ConsoleView {
    private Scanner scanner;
    
    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void displayWelcomeMessage() {
        System.out.println("***********************************************");
        System.out.println("*                                             *");
        System.out.println("*    Welcome to YADA (Yet Another Diet App)   *");
        System.out.println("*                                             *");
        System.out.println("***********************************************");
    }
    
    public void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Manage Foods");
        System.out.println("2. Manage Daily Log");
        System.out.println("3. Manage Profile");
        System.out.println("4. Show Daily Summary");
        System.out.println("5. Undo Last Action");
        System.out.println("0. Save and Exit");
        System.out.print("Enter choice: ");
    }
    
    public void displayFoodMenu() {
        System.out.println("\n--- Food Menu ---");
        System.out.println("1. Add Basic Food");
        System.out.println("2. Create Composite Food");
        System.out.println("3. Search Foods");
        System.out.println("4. Save Food Database");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter choice: ");
    }
    
    public void displayDailyLogMenu() {
        System.out.println("\n--- Daily Log Menu ---");
        System.out.println("1. Add Food to Log");
        System.out.println("2. Remove Food from Log");
        System.out.println("3. Change Date");
        System.out.println("4. Save Log Data");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter choice: ");
    }
    
    public void displayProfileMenu() {
        System.out.println("\n--- Profile Menu ---");
        System.out.println("1. Update Profile Information");
        System.out.println("2. Change Daily Info (Weight/Activity)");
        System.out.println("3. Change Calorie Calculation Method");
        System.out.println("4. View Profile");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter choice: ");
    }
    
    public void displayMessage(String message) {
        System.out.println(message);
    }
    
    public int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public double getDoubleInput() {
        try {
            String input = scanner.nextLine();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
    
    public String getStringInput() {
        return scanner.nextLine();
    }
}
