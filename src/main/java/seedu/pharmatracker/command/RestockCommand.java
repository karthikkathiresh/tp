package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

/**
 * Command that restocks a specified quantity of a medication in the inventory.
 *
 * <p>The target medication is identified by a 1-based index. Restocking is additive —
 * it increases the current stock by the given quantity rather than overwriting it.
 * Restocking fails if the index is out of range or if the quantity is non-positive,
 * in both cases printing an error message and leaving the inventory unchanged.
 */
public class RestockCommand extends Command {

    /** Command keyword used to trigger this command. */
    public static final String COMMAND_WORD = "restock";

    private static final Logger logger = Logger.getLogger(RestockCommand.class.getName());

    /** 1-based index of the medication to restock in the inventory. */
    private final int index;

    /** Number of units to add to the existing stock. Must be positive. */
    private final int quantity;

    /**
     * Constructs a {@code RestockCommand} with the target medication index and quantity to add.
     *
     * @param index    1-based position of the medication in the inventory
     * @param quantity number of units to add to existing stock; must be positive
     */
    public RestockCommand(int index, int quantity) {
        this.index = index;
        this.quantity = quantity;
    }

    /**
     * Executes the restock command, additively increasing the medication's stock
     * by the specified quantity.
     *
     * <p>Prints an error and returns early under two conditions:
     * <ul>
     *   <li>The index is out of the valid range (1 to inventory size).</li>
     *   <li>The quantity is zero or negative.</li>
     * </ul>
     *
     * @param inventory the inventory containing all medications; must not be null
     * @param ui        the UI used to display messages; must not be null
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory must not be null";
        assert ui != null : "Ui must not be null";

        logger.log(Level.INFO, "RestockCommand executing: index={0}, quantity={1}",
                new Object[]{index, quantity});

        if (index < 1 || index > inventory.getMedicationCount()) {
            logger.log(Level.WARNING, "Restock failed: invalid index {0}, inventory size={1}",
                    new Object[]{index, inventory.getMedicationCount()});
            System.out.println("Invalid index! Please provide a number between 1 and "
                    + inventory.getMedicationCount() + ".");
            return;
        }

        if (quantity <= 0) {
            logger.log(Level.WARNING, "Restock failed: non-positive quantity {0}", quantity);
            System.out.println("Quantity to restock must be a positive number!");
            return;
        }

        Medication medication = inventory.getMedication(index - 1);
        assert medication != null : "Medication retrieved from inventory must not be null";

        int oldStock = medication.getQuantity();
        assert oldStock >= 0 : "Existing stock must not be negative before restock";

        medication.setQuantity(oldStock + quantity);
        int updatedStock = medication.getQuantity();

        assert updatedStock == oldStock + quantity : "Stock was not updated correctly after restock";
        assert updatedStock > oldStock : "Updated stock must be greater than old stock after restock";

        logger.log(Level.INFO, "Restock successful: medication={0}, added={1}, oldStock={2}, updatedStock={3}",
                new Object[]{medication.getName(), quantity, oldStock, updatedStock});

        System.out.println("Restocked successfully! Medication: " + medication.getName()
                + " | Added: " + quantity + " units"
                + " | Updated Stock: " + updatedStock + " units.");
    }
}
