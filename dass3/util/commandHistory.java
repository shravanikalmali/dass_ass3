
// File: util/CommandHistory.java
package yada.util;

import java.util.Stack;

/**
 * Maintains a history of commands for undo functionality
 */
public class CommandHistory {
    private Stack<Command> history;
    
    public CommandHistory() {
        history = new Stack<>();
    }
    
    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }
    
    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
        }
    }
    
    public boolean canUndo() {
        return !history.isEmpty();
    }
}
