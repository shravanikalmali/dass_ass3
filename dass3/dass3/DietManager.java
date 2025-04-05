package yada;

import yada.controller.MenuController;
import yada.model.DietProfile;
import yada.model.food.FoodDatabase;
import yada.model.log.DailyLog;
import yada.model.log.DailyLogManager;
import yada.persistence.DatabasePersistence;
import yada.persistence.LogPersistence;
import yada.persistence.ProfilePersistence;
import yada.util.CommandHistory;
import yada.view.ConsoleView;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Main controller for the YADA Diet Manager application
 */
public class DietManager {
    private final FoodDatabase foodDatabase;
    private final DailyLogManager logManager;
    private final DietProfile profile;
    private final CommandHistory commandHistory;
    private final ConsoleView view;
    private final MenuController menuController;
    private final Scanner scanner;

    public DietManager() {
        this.scanner = new Scanner(System.in);
        this.commandHistory = new CommandHistory();
        
        // Load database and logs
        DatabasePersistence databasePersistence = new DatabasePersistence("foods.txt");
        this.foodDatabase = databasePersistence.loadDatabase();
        
        LogPersistence logPersistence = new LogPersistence("logs.txt");
        this.logManager = logPersistence.loadLogs();
        
        ProfilePersistence profilePersistence = new ProfilePersistence("profile.txt");
        this.profile = profilePersistence.loadProfile();
        
        // Initialize view and controller
        this.view = new ConsoleView(scanner);
        this.menuController = new MenuController(
            foodDatabase, 
            logManager, 
            profile, 
            commandHistory,
            view,
            databasePersistence,
            logPersistence,
            profilePersistence
        );
    }

    public void start() {
        view.displayWelcomeMessage();
        boolean running = true;
        
        while (running) {
            view.displayMainMenu();
            int choice = view.getIntInput();
            
            switch (choice) {
                case 0:
                    running = false;
                    menuController.saveAndExit();
                    break;
                case 1:
                    menuController.manageFoods();
                    break;
                case 2:
                    menuController.manageDailyLog();
                    break;
                case 3:
                    menuController.manageProfile();
                    break;
                case 4:
                    menuController.showDailySummary();
                    break;
                case 5:
                    menuController.undo();
                    break;
                default:
                    view.displayMessage("Invalid option. Please try again.");
            }
        }
        
        scanner.close();
    }
}