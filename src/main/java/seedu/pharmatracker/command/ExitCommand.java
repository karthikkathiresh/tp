package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

public class ExitCommand extends Command {
    public static final String COMMAND_WORD = "exit";
    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory should not be null";
        assert ui != null : "Ui should not be null";
        logger.log(Level.INFO, "Executing ExitCommand");
        System.out.println("Exiting Application");
        System.out.println("Stay Healthy!");
        System.exit(0);
    }
}
