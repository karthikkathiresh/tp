package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents an executable command in the PharmaTracker application.
 * This is an abstract base class that all specific command types (e.g. AddCommand, DeleteCommand)
 * must inherit from and implement.
 */
public abstract class Command {

    /**
     * Executes the specific logic of the command.
     * Subclasses must implement this method to perform their designated operations,
     * such as modifying the inventory, retrieving data, or displaying information.
     *
     * @param inventory The current inventory containing all stored medications.
     * @param ui        The user interface used to display messages and interact with the user.
     */
    public abstract void execute(Inventory inventory, Ui ui);

}
