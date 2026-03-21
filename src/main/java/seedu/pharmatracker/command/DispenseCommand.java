package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Command that dispenses a specified quantity of a medication from the inventory.
 *
 * <p>The target medication is identified by a 1-based index. Dispensing fails
 * if the index is out of range or if the requested quantity exceeds current stock,
 * in both cases printing an error message and leaving the inventory unchanged.
 */
public class DispenseCommand extends Command {

    /** Command keyword used to trigger this command. */
    public static final String COMMAND_WORD = "dispense";

    private static final Logger logger = Logger.getLogger(DispenseCommand.class.getName());

    /** 1-based index of the medication to dispense from the inventory. */
    private final int index;

    /** Number of units to dispense. */
    private final int quantity;

    /**
     * Constructs a {@code DispenseCommand} with the target medication index and quantity.
     *
     * @param index    1-based position of the medication in the inventory
     * @param quantity number of units to dispense; must not exceed available stock
     */
    public DispenseCommand(int index, int quantity) {
        this.index = index;
        this.quantity = quantity;
    }

    /**
     * Executes the dispense command, reducing the medication's stock by the specified quantity.
     *
     * <p>Prints an error and returns early under two conditions:
     * <ul>
     *   <li>The index is less than 1 or exceeds the inventory size.</li>
     *   <li>The requested quantity exceeds the medication's current stock.</li>
     * </ul>
     * On success, prints a confirmation with the medication name, dispensed amount,
     * and updated stock level.
     *
     * @param inventory the inventory from which to dispense the medication
     * @param ui        the UI instance (unused directly; output goes via {@code System.out})
     */
    @Override
    public void execute(Inventory inventory, Ui ui) {
        logger.log(Level.INFO, "Executing DispenseCommand: index={0}, quantity={1}",
                new Object[]{index, quantity});
        if (index < 1 || index > inventory.getMedications().size()) {
            logger.log(Level.WARNING, "Invalid index: {0}", index);
            System.out.println("Invalid index. Please enter a valid index.");
            return;
        }
        Medication med = inventory.getMedication(index - 1);
        if (quantity > med.getQuantity()) {
            logger.log(Level.WARNING, "Insufficient stock for {0}: requested={1}, available={2}",
                    new Object[]{med.getName(), quantity, med.getQuantity()});
            System.out.println("Insufficient stock. Current stock: " + med.getQuantity());
            return;
        }
        med.setQuantity(med.getQuantity() - quantity);
        logger.log(Level.INFO, "Dispensed {0} units of {1}. Updated stock: {2}",
                new Object[]{quantity, med.getName(), med.getQuantity()});
        System.out.println("Dispensing successfully!");
        System.out.println("Medication: " + med.getName());
        System.out.println("Amount: " + quantity + " units");
        System.out.println("Updated Stock: " + med.getQuantity() + " units");
    }
}
