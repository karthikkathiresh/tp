package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to list all medications currently in the inventory.
 * Appends a [LOW STOCK] flag to any medication at or below the low stock threshold.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    private static final int LOW_STOCK_THRESHOLD = 20;
    private static final String DIVIDER = "------------------------------------------------------";

    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

    /**
     * Executes the list command by printing all medications in the inventory.
     *
     * @param inventory    The current medication inventory.
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers (unused).
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory must not be null";
        assert ui != null : "Ui must not be null";
        assert customerList != null : "CustomerList must not be null";

        logger.log(Level.INFO, "Executing ListCommand");

        ArrayList<Medication> medicationList = inventory.getMedications();

        if (medicationList == null) {
            logger.log(Level.SEVERE, "getMedications() returned null unexpectedly.");
            System.out.println("An unexpected error occurred. Unable to retrieve medication list.");
            return;
        }

        if (medicationList.isEmpty()) {
            logger.log(Level.INFO, "Inventory is empty.");
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("PharmaTracker Inventory:");

        int lowStockCount = 0;

        for (int i = 0; i < medicationList.size(); i++) {
            Medication med = medicationList.get(i);

            if (med == null) {
                logger.log(Level.SEVERE, "Null medication entry found at index {0}. Skipping.", i);
                continue;
            }

            boolean isLowStock = med.getQuantity() < LOW_STOCK_THRESHOLD;
            String lowStockFlag = isLowStock ? " [LOW STOCK]" : "";

            if (isLowStock) {
                lowStockCount++;
                logger.log(Level.WARNING, "Low stock detected: {0} (Qty: {1})",
                        new Object[]{med.getName(), med.getQuantity()});
            }

            System.out.println((i + 1) + ". " + med.toString() + lowStockFlag);
        }

        System.out.println(DIVIDER);
        System.out.println("Total Medications: " + medicationList.size());

        if (lowStockCount > 0) {
            System.out.println("Low Stock Alerts: " + lowStockCount + " medication(s) need restocking.");
        }

        logger.log(Level.INFO, "Listed {0} medications. Low stock count: {1}",
                new Object[]{medicationList.size(), lowStockCount});
    }
}
