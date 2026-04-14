package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

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
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) throws PharmaTrackerException {
        logger.log(Level.INFO, "Starting execution of DeleteCommand for index: " + description);

        assert inventory != null : "Inventory cannot be null in DeleteCommand";
        assert ui != null : "Ui cannot be null in DeleteCommand";

        int medicationCount = inventory.getMedicationCount();

        if (medicationCount == 0) {
            throw new PharmaTrackerException("Inventory is empty! There are no medications to delete.");
        }

        try {
            int index = Integer.parseInt(description);
            if (index < 1 || index > inventory.getMedicationCount()) {
                throw new PharmaTrackerException("Invalid index. Please enter an integer between 1 and "
                        + medicationCount + ".");
            }
            int zeroBasedIndex = index - 1;
            Medication med = inventory.getMedication(zeroBasedIndex);

            inventory.removeMedication(med);
            ui.printDeletedMessage(med, inventory);

            logger.log(Level.INFO, "Successfully executed DeleteCommand.");

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Failed to parse index in DeleteCommand: " + description);
            if (description.trim().matches("-?\\d+")) {
                throw new PharmaTrackerException("Invalid index. Please enter an integer between 1 and " +
                        medicationCount + ".");
            } else {
                throw new PharmaTrackerException("Invalid format. Please provide a valid integer for the index.");
            }
        }
    }
}
