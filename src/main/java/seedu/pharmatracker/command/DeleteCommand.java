package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

/**
 * Represents a command to delete a specific medication from the inventory.
 * The medication to be deleted is identified by its 1-based index as shown in the inventory list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    private static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());
    private final String description;

    /**
     * Constructs an DeleteCommand with the specified target index description.
     *
     * @param description The 1-based index of the medication to be deleted, provided as a String.
     */
    public DeleteCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the delete command by parsing the target index, locating the corresponding
     * {@link Medication} in the {@link Inventory}, removing it, and displaying a confirmation message.
     *
     * @param inventory The current inventory containing all stored medications.
     * @param ui        The user interface used to display messages and interact with the user.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        logger.log(Level.INFO, "Starting execution of DeleteCommand for index: " + description);

        assert inventory != null : "Inventory cannot be null in DeleteCommand";
        assert ui != null : "Ui cannot be null in DeleteCommand";

        int index = Integer.parseInt(description);
        int zeroBasedIndex = index - 1;

        Medication med = inventory.getMedication(zeroBasedIndex);
        assert med != null : "Retrieved medication to delete cannot be null";

        inventory.removeMedication(med);
        ui.printDeletedMessage(med, inventory);

        logger.log(Level.INFO, "Successfully executed DeleteCommand.");
    }
}
