package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

/**
 * Represents a command to exit the PharmaTracker application.
 * Displays a farewell message before terminating the program.
 */
public class ExitCommand extends Command {
    
    public static final String COMMAND_WORD = "exit";
    
    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());
    
    private boolean shouldActuallyExit = true;  // For testing purposes

    /**
     * Executes the exit command by printing a farewell message and terminating the application.
     *
     * @param inventory    The current medication inventory (unused).
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers (unused).
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory should not be null";
        assert ui != null : "Ui should not be null";
        assert customerList != null : "CustomerList should not be null";
        
        logger.log(Level.INFO, "Executing ExitCommand");
        
        ui.printExitMessage();
        
        logger.log(Level.INFO, "ExitCommand executed successfully");
        
        if (shouldActuallyExit) {
            System.exit(0);
        }
    }

    /**
     * Sets whether this command should actually terminate the JVM.
     * This method is intended for testing purposes only.
     *
     * @param shouldExit false to prevent termination during testing; true otherwise.
     */
    public void setShouldExit(boolean shouldExit) {
        this.shouldActuallyExit = shouldExit;
    }
}
