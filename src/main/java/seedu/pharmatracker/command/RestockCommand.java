package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to restock a specified quantity of a medication in the inventory.
 * Restocking is additive — it increases the current stock by the given quantity
 * rather than overwriting it.
 */
public class RestockCommand extends Command {

    public static final String COMMAND_WORD = "restock";

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 100000;

    private static final Logger logger = Logger.getLogger(RestockCommand.class.getName());

    private final int index;
    private final int quantity;

    /**
     * Constructs a RestockCommand with the target medication index and quantity to add.
     *
     * @param index    1-based position of the medication in the inventory.
     * @param quantity Number of units to add to existing stock; must be positive.
     */
    public RestockCommand(int index, int quantity) {
        this.index = index;
        this.quantity = quantity;
    }

    /**
     * Executes the restock command, additively increasing the medication's stock
     * by the specified quantity.
     *
     * @param inventory    The current medication inventory.
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory must not be null";
        assert ui != null : "Ui must not be null";
        assert customerList != null : "CustomerList must not be null";

        logger.log(Level.INFO, "RestockCommand executing: index={0}, quantity={1}",
                new Object[]{index, quantity});

        if (!isValidQuantity()) {
            logger.log(Level.WARNING, "Restock failed: invalid quantity {0}", quantity);
            System.out.println("Quantity to restock must be between " + MIN_QUANTITY
                    + " and " + MAX_QUANTITY + ".");
            return;
        }

        if (inventory.getMedications() == null || inventory.getMedications().isEmpty()) {
            logger.log(Level.WARNING, "Restock failed: inventory is empty.");
            System.out.println("Inventory is empty. No medications to restock.");
            return;
        }

        if (!isValidMedicationIndex(inventory)) {
            logger.log(Level.WARNING, "Restock failed: invalid index {0}, inventory size={1}",
                    new Object[]{index, inventory.getMedicationCount()});
            System.out.println("Invalid index. Please enter a number between 1 and "
                    + inventory.getMedicationCount() + ".");
            return;
        }

        Medication medication = inventory.getMedication(index - 1);

        if (medication == null) {
            logger.log(Level.SEVERE, "Medication at index {0} returned null unexpectedly.", index);
            System.out.println("An unexpected error occurred. Medication not found.");
            return;
        }

        int oldStock = medication.getQuantity();

        if (!isStockNonNegative(oldStock)) {
            logger.log(Level.SEVERE, "Medication {0} has negative stock: {1}",
                    new Object[]{medication.getName(), oldStock});
            System.out.println("Data integrity error: " + medication.getName()
                    + " has invalid stock (" + oldStock + "). Restock aborted.");
            return;
        }

        if (wouldOverflow(oldStock)) {
            logger.log(Level.WARNING, "Restock failed: resulting stock would exceed maximum for {0}",
                    medication.getName());
            System.out.println("Restock rejected. Resulting stock would exceed the maximum allowed ("
                    + MAX_QUANTITY + "). Current stock: " + oldStock + ".");
            return;
        }

        performRestock(medication, oldStock);
    }

    /**
     * Updates the medication's stock and prints the restock confirmation.
     *
     * @param medication The medication to restock.
     * @param oldStock   The stock level before restocking.
     */
    private void performRestock(Medication medication, int oldStock) {
        medication.setQuantity(oldStock + quantity);
        int updatedStock = medication.getQuantity();

        assert updatedStock == oldStock + quantity : "Stock was not updated correctly after restock";
        assert updatedStock > oldStock : "Updated stock must be greater than old stock after restock";

        logger.log(Level.INFO, "Restock successful: medication={0}, added={1}, oldStock={2}, updatedStock={3}",
                new Object[]{medication.getName(), quantity, oldStock, updatedStock});

        System.out.println("Restocked successfully!");
        System.out.println("Medication: " + medication.getName()
                + " | Added: " + quantity + " units"
                + " | Updated Stock: " + updatedStock + " units.");
    }

    /**
     * Returns true if the quantity is within the allowed range.
     *
     * @return true if quantity is between MIN_QUANTITY and MAX_QUANTITY inclusive.
     */
    private boolean isValidQuantity() {
        return quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY;
    }

    /**
     * Returns true if the medication index is within the bounds of the inventory.
     *
     * @param inventory The current medication inventory.
     * @return true if index is valid.
     */
    private boolean isValidMedicationIndex(Inventory inventory) {
        return index >= 1 && index <= inventory.getMedicationCount();
    }

    /**
     * Returns true if the existing stock value is non-negative.
     *
     * @param stock The current stock level.
     * @return true if stock is zero or positive.
     */
    private boolean isStockNonNegative(int stock) {
        return stock >= 0;
    }

    /**
     * Returns true if adding the restock quantity would exceed MAX_QUANTITY.
     *
     * @param currentStock The current stock level.
     * @return true if the resulting stock would overflow the maximum.
     */
    private boolean wouldOverflow(int currentStock) {
        return currentStock + quantity > MAX_QUANTITY;
    }
}
