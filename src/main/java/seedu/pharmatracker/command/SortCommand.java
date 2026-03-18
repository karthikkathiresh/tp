package seedu.pharmatracker.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.logging.Level;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Sorts medications in the inventory by expiry date.
 * Medications with invalid expiry dates are treated as having the latest possible expiry date.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    private static final Logger logger = Logger.getLogger(SortCommand.class.getName());

    /**
     * Executes the sort command by sorting all medications in the inventory by expiry date
     * in ascending order and displaying the sorted list to the user.
     * If the inventory is empty, displays a message indicating the inventory is empty.
     *
     * @param inventory The inventory containing medications to be sorted.
     */
    @Override
    public void execute(Inventory inventory, Ui ui) {
        ArrayList<Medication> medicationList = inventory.getMedications();

        // Check if inventory is empty
        if (medicationList.isEmpty()) {
            logger.log(Level.INFO, "Inventory is empty");
            System.out.println("Inventory is empty.");
            return;
        }

        logger.log(Level.FINE, "Sorting " + medicationList.size() + " medications by expiry date");

        // Sort medications by expiry date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        medicationList.sort(Comparator.comparing(med -> {
            try {
                return LocalDate.parse(med.getExpiryDate(), formatter);
            } catch (DateTimeParseException e) {
                // Treat invalid dates as latest possible date
                logger.log(Level.FINE, "Invalid expiry date for medication: " + med.getName());
                return LocalDate.MAX;
            }
        }));

        logger.log(Level.INFO, "Medications sorted successfully");

        // Display sorted medications
        System.out.println("Medications sorted by expiry date:");
        for (int i = 0; i < medicationList.size(); i++) {
            Medication medication = medicationList.get(i);
            System.out.println((i + 1) + ". " + medication.toString());
        }
    }
}
