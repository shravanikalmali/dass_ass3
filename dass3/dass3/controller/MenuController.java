// File: controller/MenuController.java
package yada.controller;

import yada.model.DietProfile;
import yada.model.food.BasicFood;
import yada.model.food.CompositeFood;
import yada.model.food.Food;
import yada.model.food.FoodDatabase;
import yada.model.log.DailyLog;
import yada.model.log.DailyLogManager;
import yada.model.log.FoodEntry;
import yada.persistence.DatabasePersistence;
import yada.persistence.LogPersistence;
import yada.persistence.ProfilePersistence;
import yada.util.Command;
import yada.util.CommandHistory;
import yada.util.DateUtil;
import yada.view.ConsoleView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller handling user interactions with the menu system
 */
public class MenuController {
    private final FoodDatabase foodDatabase;
    private final DailyLogManager logManager;
    private final DietProfile profile;
    private final CommandHistory commandHistory;
    private final ConsoleView view;
    private final DatabasePersistence databasePersistence;
    private final LogPersistence logPersistence;
    private final ProfilePersistence profilePersistence;
    private LocalDate currentDate;

    public MenuController(FoodDatabase foodDatabase, DailyLogManager logManager, DietProfile profile,
                         CommandHistory commandHistory, ConsoleView view,
                         DatabasePersistence databasePersistence, LogPersistence logPersistence,
                         ProfilePersistence profilePersistence) {
        this.foodDatabase = foodDatabase;
        this.logManager = logManager;
        this.profile = profile;
        this.commandHistory = commandHistory;
        this.view = view;
        this.databasePersistence = databasePersistence;
        this.logPersistence = logPersistence;
        this.profilePersistence = profilePersistence;
        this.currentDate = LocalDate.now();
    }

    public void saveAndExit() {
        databasePersistence.saveDatabase(foodDatabase);
        logPersistence.saveLogs(logManager);
        profilePersistence.saveProfile(profile);
        view.displayMessage("All data saved. Goodbye!");
    }

    public void manageFoods() {
        boolean running = true;
        
        while (running) {
            view.displayFoodMenu();
            int choice = view.getIntInput();
            
            switch (choice) {
                case 0:
                    running = false;
                    break;
                case 1:
                    addBasicFood();
                    break;
                case 2:
                    createCompositeFood();
                    break;
                case 3:
                    searchFood();
                    break;
                case 4:
                    saveFoodDatabase();
                    break;
                default:
                    view.displayMessage("Invalid option. Please try again.");
            }
        }
    }
    
    private void addBasicFood() {
        view.displayMessage("\n--- Add Basic Food ---");
        view.displayMessage("Enter food name:");
        String name = view.getStringInput();
        
        view.displayMessage("Enter keywords (comma separated):");
        String keywordsInput = view.getStringInput();
        List<String> keywords = List.of(keywordsInput.split(","));
        
        view.displayMessage("Enter calories per serving:");
        int calories = view.getIntInput();
        
        BasicFood newFood = new BasicFood(name, keywords, calories);
        
        Command addFoodCommand = new Command() {
            @Override
            public void execute() {
                foodDatabase.addFood(newFood);
            }
            
            @Override
            public void undo() {
                foodDatabase.removeFood(newFood.getName());
            }
        };
        
        commandHistory.executeCommand(addFoodCommand);
        view.displayMessage("Food added successfully!");
    }
    
    private void createCompositeFood() {
        view.displayMessage("\n--- Create Composite Food ---");
        view.displayMessage("Enter composite food name:");
        String name = view.getStringInput();
        
        view.displayMessage("Enter keywords (comma separated):");
        String keywordsInput = view.getStringInput();
        List<String> keywords = List.of(keywordsInput.split(","));
        
        List<Food> components = new ArrayList<>();
        List<Integer> servings = new ArrayList<>();
        
        boolean addingComponents = true;
        while (addingComponents) {
            view.displayMessage("\nSearch for a component food:");
            Food food = searchAndSelectFood();
            
            if (food != null) {
                view.displayMessage("Enter number of servings:");
                int numServings = view.getIntInput();
                
                components.add(food);
                servings.add(numServings);
                
                view.displayMessage("Add another component? (y/n)");
                String another = view.getStringInput();
                if (!another.toLowerCase().startsWith("y")) {
                    addingComponents = false;
                }
            } else {
                view.displayMessage("No food selected. Try again? (y/n)");
                String tryAgain = view.getStringInput();
                if (!tryAgain.toLowerCase().startsWith("y")) {
                    addingComponents = false;
                }
            }
        }
        
        if (components.isEmpty()) {
            view.displayMessage("No components added. Composite food creation cancelled.");
            return;
        }
        
        CompositeFood compositeFood = new CompositeFood(name, keywords);
        for (int i = 0; i < components.size(); i++) {
            compositeFood.addComponent(components.get(i), servings.get(i));
        }
        
        Command addCompositeCommand = new Command() {
            @Override
            public void execute() {
                foodDatabase.addFood(compositeFood);
            }
            
            @Override
            public void undo() {
                foodDatabase.removeFood(compositeFood.getName());
            }
        };
        
        commandHistory.executeCommand(addCompositeCommand);
        view.displayMessage("Composite food created successfully!");
    }
    
    private void searchFood() {
        view.displayMessage("\n--- Search Foods ---");
        view.displayMessage("Enter search keywords (comma separated):");
        String keywordsInput = view.getStringInput();
        List<String> keywords = List.of(keywordsInput.split(","));
        
        view.displayMessage("Match all keywords? (y/n)");
        String matchAll = view.getStringInput();
        boolean all = matchAll.toLowerCase().startsWith("y");
        
        List<Food> results = foodDatabase.searchFoods(keywords, all);
        
        if (results.isEmpty()) {
            view.displayMessage("No foods found matching your criteria.");
        } else {
            view.displayMessage("\nSearch Results:");
            for (int i = 0; i < results.size(); i++) {
                Food food = results.get(i);
                view.displayMessage((i + 1) + ". " + food.getName() + " (" + food.getCaloriesPerServing() + " cal/serving)");
            }
        }
    }
    
    private Food searchAndSelectFood() {
        view.displayMessage("Enter search keywords (comma separated):");
        String keywordsInput = view.getStringInput();
        List<String> keywords = List.of(keywordsInput.split(","));
        
        view.displayMessage("Match all keywords? (y/n)");
        String matchAll = view.getStringInput();
        boolean all = matchAll.toLowerCase().startsWith("y");
        
        List<Food> results = foodDatabase.searchFoods(keywords, all);
        
        if (results.isEmpty()) {
            view.displayMessage("No foods found matching your criteria.");
            return null;
        } else {
            view.displayMessage("\nSearch Results:");
            for (int i = 0; i < results.size(); i++) {
                Food food = results.get(i);
                view.displayMessage((i + 1) + ". " + food.getName() + " (" + food.getCaloriesPerServing() + " cal/serving)");
            }
            
            view.displayMessage("\nSelect a food by number (0 to cancel):");
            int selection = view.getIntInput();
            
            if (selection > 0 && selection <= results.size()) {
                return results.get(selection - 1);
            } else {
                return null;
            }
        }
    }
    
    private void saveFoodDatabase() {
        databasePersistence.saveDatabase(foodDatabase);
        view.displayMessage("Food database saved successfully!");
    }

    public void manageDailyLog() {
        boolean running = true;
        
        while (running) {
            DailyLog currentLog = logManager.getOrCreateLog(currentDate);
            
            view.displayMessage("\n--- Daily Log for " + currentDate + " ---");
            view.displayDailyLogMenu();
            int choice = view.getIntInput();
            
            switch (choice) {
                case 0:
                    running = false;
                    break;
                case 1:
                    addFoodToLog();
                    break;
                case 2:
                    removeFoodFromLog();
                    break;
                case 3:
                    changeDateForLog();
                    break;
                case 4:
                    saveLogData();
                    break;
                default:
                    view.displayMessage("Invalid option. Please try again.");
            }
        }
    }
    
    private void addFoodToLog() {
        view.displayMessage("\n--- Add Food to Log ---");
        Food food = searchAndSelectFood();
        
        if (food != null) {
            view.displayMessage("Enter number of servings:");
            int servings = view.getIntInput();
            
            FoodEntry entry = new FoodEntry(food, servings);
            DailyLog log = logManager.getOrCreateLog(currentDate);
            
            Command addEntryCommand = new Command() {
                @Override
                public void execute() {
                    log.addEntry(entry);
                }
                
                @Override
                public void undo() {
                    log.removeEntry(entry);
                }
            };
            
            commandHistory.executeCommand(addEntryCommand);
            view.displayMessage("Food added to log successfully!");
        }
    }
    
    private void removeFoodFromLog() {
        DailyLog log = logManager.getOrCreateLog(currentDate);
        List<FoodEntry> entries = log.getEntries();
        
        if (entries.isEmpty()) {
            view.displayMessage("No entries in the current day's log.");
            return;
        }
        
        view.displayMessage("\n--- Current Log Entries ---");
        for (int i = 0; i < entries.size(); i++) {
            FoodEntry entry = entries.get(i);
            view.displayMessage((i + 1) + ". " + entry.getFood().getName() + " (" + entry.getServings() + " servings)");
        }
        
        view.displayMessage("\nSelect entry to remove (0 to cancel):");
        int selection = view.getIntInput();
        
        if (selection > 0 && selection <= entries.size()) {
            final FoodEntry entryToRemove = entries.get(selection - 1);
            
            Command removeEntryCommand = new Command() {
                @Override
                public void execute() {
                    log.removeEntry(entryToRemove);
                }
                
                @Override
                public void undo() {
                    log.addEntry(entryToRemove);
                }
            };
            
            commandHistory.executeCommand(removeEntryCommand);
            view.displayMessage("Entry removed from log.");
        }
    }
    
    private void changeDateForLog() {
        view.displayMessage("\n--- Change Date ---");
        view.displayMessage("Enter date (YYYY-MM-DD):");
        String dateStr = view.getStringInput();
        
        try {
            LocalDate newDate = DateUtil.parseDate(dateStr);
            currentDate = newDate;
            view.displayMessage("Date changed to: " + currentDate);
        } catch (Exception e) {
            view.displayMessage("Invalid date format. Please use YYYY-MM-DD.");
        }
    }
    
    private void saveLogData() {
        logPersistence.saveLogs(logManager);
        view.displayMessage("Log data saved successfully!");
    }

    public void manageProfile() {
        boolean running = true;
        
        while (running) {
            view.displayProfileMenu();
            int choice = view.getIntInput();
            
            switch (choice) {
                case 0:
                    running = false;
                    break;
                case 1:
                    updateProfileInfo();
                    break;
                case 2:
                    changeDailyInfo();
                    break;
                case 3:
                    changeCalculationMethod();
                    break;
                case 4:
                    viewProfile();
                    break;
                default:
                    view.displayMessage("Invalid option. Please try again.");
            }
        }
    }
    
    private void updateProfileInfo() {
        view.displayMessage("\n--- Update Profile Information ---");
        
        view.displayMessage("Gender (M/F):");
        String gender = view.getStringInput();
        
        view.displayMessage("Height (cm):");
        double height = view.getDoubleInput();
        
        view.displayMessage("Age:");
        int age = view.getIntInput();
        
        view.displayMessage("Weight (kg):");
        double weight = view.getDoubleInput();
        
        view.displayMessage("Activity Level (1-5):");
        view.displayMessage("1. Sedentary");
        view.displayMessage("2. Lightly active");
        view.displayMessage("3. Moderately active");
        view.displayMessage("4. Very active");
        view.displayMessage("5. Extremely active");
        int activityLevel = view.getIntInput();
        
        final String oldGender = profile.getGender();
        final double oldHeight = profile.getHeight();
        final int oldAge = profile.getAge();
        final double oldWeight = profile.getWeight();
        final int oldActivityLevel = profile.getActivityLevel();
        
        Command updateProfileCommand = new Command() {
            @Override
            public void execute() {
                profile.setGender(gender);
                profile.setHeight(height);
                profile.setAge(age);
                profile.setWeight(weight);
                profile.setActivityLevel(activityLevel);
            }
            
            @Override
            public void undo() {
                profile.setGender(oldGender);
                profile.setHeight(oldHeight);
                profile.setAge(oldAge);
                profile.setWeight(oldWeight);
                profile.setActivityLevel(oldActivityLevel);
            }
        };
        
        commandHistory.executeCommand(updateProfileCommand);
        view.displayMessage("Profile updated successfully!");
    }
    
    private void changeDailyInfo() {
        view.displayMessage("\n--- Update Daily Information ---");
        
        view.displayMessage("Weight (kg):");
        double weight = view.getDoubleInput();
        
        view.displayMessage("Activity Level (1-5):");
        view.displayMessage("1. Sedentary");
        view.displayMessage("2. Lightly active");
        view.displayMessage("3. Moderately active");
        view.displayMessage("4. Very active");
        view.displayMessage("5. Extremely active");
        int activityLevel = view.getIntInput();
        
        final double oldWeight = profile.getWeight();
        final int oldActivityLevel = profile.getActivityLevel();
        
        Command updateDailyInfoCommand = new Command() {
            @Override
            public void execute() {
                profile.setWeight(weight);
                profile.setActivityLevel(activityLevel);
            }
            
            @Override
            public void undo() {
                profile.setWeight(oldWeight);
                profile.setActivityLevel(oldActivityLevel);
            }
        };
        
        commandHistory.executeCommand(updateDailyInfoCommand);
        view.displayMessage("Daily information updated successfully!");
    }
    
    private void changeCalculationMethod() {
        view.displayMessage("\n--- Change Calorie Calculation Method ---");
        view.displayMessage("Available methods:");
        view.displayMessage("1. Harris-Benedict Equation");
        view.displayMessage("2. Mifflin-St Jeor Equation");
        
        view.displayMessage("\nSelect a method:");
        int methodChoice = view.getIntInput();
        
        if (methodChoice == 1 || methodChoice == 2) {
            final int oldMethod = profile.getCalculationMethod();
            
            Command changeMethodCommand = new Command() {
                @Override
                public void execute() {
                    profile.setCalculationMethod(methodChoice);
                }
                
                @Override
                public void undo() {
                    profile.setCalculationMethod(oldMethod);
                }
            };
            
            commandHistory.executeCommand(changeMethodCommand);
            view.displayMessage("Calculation method changed successfully!");
        } else {
            view.displayMessage("Invalid method choice.");
        }
    }
    
    private void viewProfile() {
        view.displayMessage("\n--- Current Profile ---");
        view.displayMessage("Gender: " + profile.getGender());
        view.displayMessage("Height: " + profile.getHeight() + " cm");
        view.displayMessage("Age: " + profile.getAge());
        view.displayMessage("Weight: " + profile.getWeight() + " kg");
        
        String activityDesc;
        switch (profile.getActivityLevel()) {
            case 1: activityDesc = "Sedentary"; break;
            case 2: activityDesc = "Lightly active"; break;
            case 3: activityDesc = "Moderately active"; break;
            case 4: activityDesc = "Very active"; break;
            case 5: activityDesc = "Extremely active"; break;
            default: activityDesc = "Unknown"; break;
        }
        view.displayMessage("Activity Level: " + activityDesc);
        
        String methodDesc;
        switch (profile.getCalculationMethod()) {
            case 1: methodDesc = "Harris-Benedict Equation"; break;
            case 2: methodDesc = "Mifflin-St Jeor Equation"; break;
            default: methodDesc = "Unknown"; break;
        }
        view.displayMessage("Calculation Method: " + methodDesc);
        
        int targetCalories = profile.calculateTargetCalories();
        view.displayMessage("Target Daily Calories: " + targetCalories);
    }

    public void showDailySummary() {
        DailyLog log = logManager.getOrCreateLog(currentDate);
        int consumedCalories = log.getTotalCalories();
        int targetCalories = profile.calculateTargetCalories();
        int difference = consumedCalories - targetCalories;
        
        view.displayMessage("\n--- Daily Summary for " + currentDate + " ---");
        view.displayMessage("Target Calories: " + targetCalories);
        view.displayMessage("Consumed Calories: " + consumedCalories);
        
        if (difference < 0) {
            view.displayMessage("Calories Available: " + Math.abs(difference));
        } else {
            view.displayMessage("Calories Over Target: " + difference);
        }
        
        // Display food entries
        List<FoodEntry> entries = log.getEntries();
        if (!entries.isEmpty()) {
            view.displayMessage("\nFood Entries:");
            for (int i = 0; i < entries.size(); i++) {
                FoodEntry entry = entries.get(i);
                Food food = entry.getFood();
                int servings = entry.getServings();
                int calories = food.getCaloriesPerServing() * servings;
                
                view.displayMessage((i + 1) + ". " + food.getName() + " - " + 
                                   servings + " serving(s), " + calories + " calories");
            }
        } else {
            view.displayMessage("\nNo food entries for today.");
        }
    }

    public void undo() {
        if (commandHistory.canUndo()) {
            commandHistory.undo();
            view.displayMessage("Last action undone successfully.");
        } else {
            view.displayMessage("Nothing to undo.");
        }
    }
}
