package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to view the details of a specific medication in the inventory.
 * The medication is identified by its 1-based index as shown in the inventory list.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    private static final Logger logger = Logger.getLogger(ViewCommand.class.getName());
    private final int index;

    /**
     * Constructs a ViewCommand with the specified target index.
     *
     * @param index The 1-based index of the medication to view.
     */
    public ViewCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the view command by locating the {@link Medication} at the specified index
     * in the {@link Inventory} and displaying its details through the {@link Ui}.
     *
     * @param inventory The current inventory containing all stored medications.
     * @param ui        The user interface used to display messages and interact with the user.
     */
    @Override
    public void execute(Inventory inventory, Ui ui) {
        logger.log(Level.INFO, "Starting execution of ViewCommand for index: " + index);

        if (inventory.getMedications().isEmpty()) {
            logger.log(Level.WARNING, "Attempted to view medication from empty inventory.");
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
        ui.printMedicationDetails(med);

        logger.log(Level.INFO, "Successfully executed ViewCommand.");
    }
}
