
// File: util/Command.java
package yada.util;

/**
 * Interface for commands that can be executed and undone
 */
public interface Command {
    void execute();
    void undo();
}
