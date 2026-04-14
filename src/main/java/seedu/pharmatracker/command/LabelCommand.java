package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

/**
 * Represents a command to generate a printable medication label containing key
 * information for dispensing or packaging purposes.
 * The medication is identified by its 1-based index.
 */
public class LabelCommand extends Command {

    public static final String COMMAND_WORD = "label";

    private static final String DIVIDER = "----------------------------";
    private static final Logger logger = Logger.getLogger(LabelCommand.class.getName());
    private final int index;

    /**
     * Constructs a LabelCommand with the specified target index.
     *
     * @param index The 1-based index of the medication whose label to print.
     */
    public LabelCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the label command by locating the {@link Medication} at the
     * specified index in the {@link Inventory} and printing a formatted medication label.
     *
     * @param inventory The current inventory containing all stored medications.
     * @param ui        The user interface used to display messages and interact with the user.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        logger.log(Level.INFO, "Starting execution of LabelCommand for index: " + index);

        if (inventory.getMedications().isEmpty()) {
            logger.log(Level.WARNING, "Attempted to print label from empty inventory.");
            ui.printMessage("Inventory is empty.");
            return;
        }
        if (index < 1 || index > inventory.getMedications().size()) {
            logger.log(Level.WARNING, "Invalid index provided: " + index
                + ". Valid range: 1 to " + inventory.getMedications().size());
            ui.printMessage("Invalid index. Please enter a number between 1 and "
                    + inventory.getMedications().size() + ".");
            return;
        }

        Medication med = inventory.getMedication(index - 1);

        StringBuilder label = new StringBuilder();
        label.append(DIVIDER).append("\n");
        label.append("Medication Label ").append(index).append("\n");
        label.append(DIVIDER).append("\n");
        label.append("Name   : ").append(med.getName()).append("\n");
        label.append("Dosage : ").append(med.getDosage()).append("\n");
        label.append("Expiry : ").append(med.getExpiryDate());
        String tag = med.getTag();
        if (tag != null && !tag.isEmpty()) {
            label.append("\nTag    : ").append(tag);
        }
        label.append("\n").append(DIVIDER);
        ui.printMessage(label.toString());

        logger.log(Level.INFO, "Successfully executed LabelCommand.");
    }
}
