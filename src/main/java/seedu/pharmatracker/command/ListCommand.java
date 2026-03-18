package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";

    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final String DIVIDER = "------------------------------------------------------";
    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

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
