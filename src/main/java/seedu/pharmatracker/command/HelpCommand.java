package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

/**
 * Represents a command that displays all available commands and their formats.
 * This command provides users with a comprehensive help menu showing medication commands,
 * customer commands, and general application commands.
 */
public class HelpCommand extends Command {
    
    public static final String COMMAND_WORD = "help";
    
    private static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    /**
     * Executes the help command by displaying all available commands and their usage formats.
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
        
        logger.log(Level.INFO, "Executing HelpCommand");
        
        ui.printHelpMessage();
        
        logger.log(Level.INFO, "HelpCommand executed successfully");
    }
}
