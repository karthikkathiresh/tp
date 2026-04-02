package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to list all customers currently registered in the system.
 */
public class ListCustomersCommand extends Command {

    public static final String COMMAND_WORD = "list-customers";

    private static final Logger logger = Logger.getLogger(ListCustomersCommand.class.getName());

    /**
     * Executes the list customers command by printing all registered customers.
     *
     * @param inventory    The current medication inventory (unused).
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory must not be null";
        assert ui != null : "Ui must not be null";
        assert customerList != null : "CustomerList must not be null";

        logger.log(Level.INFO, "Executing ListCustomersCommand: customerCount={0}",
                customerList.size());

        ui.printCustomerList(customerList);

        logger.log(Level.INFO, "ListCustomersCommand completed successfully.");
    }
}
