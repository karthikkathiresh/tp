package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

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
            System.out.println("Inventory is empty.");
            return;
        }
        if (index < 1 || index > inventory.getMedications().size()) {
            logger.log(Level.WARNING, "Invalid index provided: " + index
                + ". Valid range: 1 to " + inventory.getMedications().size());
            System.out.println("Invalid index. Please enter a number between 1 and "
                    + inventory.getMedications().size() + ".");
            return;
        }

        Medication med = inventory.getMedication(index - 1);

        System.out.println(DIVIDER);
        System.out.println("Medication Label " + index);
        System.out.println(DIVIDER);
        System.out.println("Name   : " + med.getName());
        System.out.println("Dosage : " + med.getDosage());
        System.out.println("Expiry : " + med.getExpiryDate());
        String tag = med.getTag();
        if (tag != null && !tag.isEmpty()) {
            System.out.println("Tag    : " + tag);
        }
        System.out.println(DIVIDER);

        logger.log(Level.INFO, "Successfully executed LabelCommand.");
    }
}
