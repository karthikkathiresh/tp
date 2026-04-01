package seedu.pharmatracker.command;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Lists all customers currently registered in the system.
 */
public class ListCustomersCommand extends Command {

    public static final String COMMAND_WORD = "list-customers";

    /**
     * Executes the list customers command by printing all registered customers.
     *
     * @param inventory    The current medication inventory (unused).
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        ui.printCustomerList(customerList);
    }
}
