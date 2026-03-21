package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Command that lists all medications currently in the inventory.
 *
 * <p>Prints each medication with a 1-based index, appending a {@code [LOW STOCK]}
 * flag if the quantity is at or below {@value #LOW_STOCK_THRESHOLD} units.
 * If the inventory is empty, a single message is printed instead.
 */
public class ListCommand extends Command {

    /** Command keyword used to trigger this command. */
    public static final String COMMAND_WORD = "list";

    /** Quantity at or below which a medication is considered low stock. */
    private static final int LOW_STOCK_THRESHOLD = 10;

    /** Visual divider printed between the medication list and the summary footer. */
    private static final String DIVIDER = "------------------------------------------------------";

    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

    /**
     * Executes the list command by printing all medications in the inventory.
     *
     * <p>If the inventory is empty, prints {@code "Inventory is empty."} and returns early.
     * Otherwise, prints each {@link Medication} with a 1-based index and a
     * {@code [LOW STOCK]} flag for any medication at or below the low-stock threshold,
     * followed by the total medication count.
     *
     * @param inventory the inventory containing the medications to list
     * @param ui        the UI instance (unused directly; output goes via {@code System.out})
     */
    @Override
    public void execute(Inventory inventory, Ui ui) {
        logger.log(Level.INFO, "Executing ListCommand");
        ArrayList<Medication> medicationList = inventory.getMedications();
        if (medicationList.isEmpty()) {
            logger.log(Level.WARNING, "Inventory is empty");
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("PharmaTracker Inventory:");
        for (int i = 0; i < medicationList.size(); i++) {
            Medication med = medicationList.get(i);
            String lowStock = med.getQuantity() <= LOW_STOCK_THRESHOLD ? " [LOW STOCK]" : "";
            if (!lowStock.isEmpty()) {
                logger.log(Level.WARNING, "Low stock detected: {0} (Qty: {1})",
                        new Object[]{med.getName(), med.getQuantity()});
            }
            System.out.println((i + 1) + ". " + med.toString() + lowStock);
        }
        System.out.println(DIVIDER);
        System.out.println("Total Medications: " + medicationList.size());
        logger.log(Level.INFO, "Listed {0} medications", medicationList.size());
    }
}
