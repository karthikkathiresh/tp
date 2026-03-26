package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

/**
 * Represents an abstract command in PharmaTracker.
 * All commands must implement the {@link #execute(Inventory, Ui, CustomerList)} method.
 */
public abstract class Command {

    /**
     * Executes the command with access to the medication inventory,
     * user interface, and customer list.
     *
     * @param inventory    The current medication inventory.
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    public abstract void execute(Inventory inventory, Ui ui, CustomerList customerList);

}
