package seedu.pharmatracker.command;

import seedu.pharmatracker.data.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command that requires access to the customer list.
 * Extends Command so it can be returned by the Parser alongside
 * regular commands. The main loop uses instanceof to determine
 * which execute method to call.
 */
public abstract class CustomerCommand extends Command {

    /**
     * This method exists to satisfy the Command contract.
     * It should not be called directly — the main loop should
     * detect CustomerCommand via instanceof and call the
     * three-parameter version instead.
     */
    @Override
    public void execute(Inventory inventory, Ui ui) {
        System.out.println("Error: This command requires customer data. ");
    }

    /**
     * Executes the customer command with access to both inventory and customer data.
     *
     * @param inventory    The current medication inventory.
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    public abstract void execute(Inventory inventory, Ui ui, CustomerList customerList);
}
