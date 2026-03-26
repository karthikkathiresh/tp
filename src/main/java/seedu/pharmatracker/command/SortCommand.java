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
import seedu.pharmatracker.data.CustomerList;

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
     * @param inventory    The current inventory containing all stored medications.
     * @param ui           The user interface used to display messages and interact with the user.
     * @param customerList The list of registered customers.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {

        assert inventory != null : "Inventory should not be null";
        ArrayList<Medication> medicationList = inventory.getMedications();
        assert medicationList != null : "Medication list should not be null";


        // Check if inventory is empty
        if (medicationList.isEmpty()) {
            logger.log(Level.INFO, "Inventory is empty");
            System.out.println("Inventory is empty.");
            return;
        }


        logger.log(Level.FINE, "Sorting " + medicationList.size() + " medications by expiry date");

        // Sort medications by expiry date
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        medicationList.sort(Comparator.comparing(med -> {
            assert med != null : "Medication should not be null";
            String expiryDate = med.getExpiryDate();
            assert expiryDate != null : "Expiry date should not be null";
            expiryDate = expiryDate.trim();
            LocalDate parsedDate = null;
            try {
                parsedDate = LocalDate.parse(expiryDate, formatter1);
                logger.log(Level.FINE,
                        "Parsed (yyyy-MM-dd) for " + med.getName() + ": " + expiryDate + " -> " + parsedDate);
            } catch (DateTimeParseException e1) {
                try {
                    parsedDate = LocalDate.parse(expiryDate, formatter2);
                    logger.log(Level.FINE,
                            "Parsed (dd/MM/yyyy) for " + med.getName() + ": " + expiryDate + " -> " + parsedDate);
                } catch (DateTimeParseException e2) {
                    logger.log(Level.WARNING,
                            "Invalid expiry date for medication: " + med.getName() + ", value: " + expiryDate);
                    parsedDate = LocalDate.MAX;
                }
            }
            return parsedDate;
        }));

        logger.log(Level.INFO, "Medications sorted successfully");

        // Display sorted medications
        System.out.println("Medications sorted by expiry date:");
        for (int i = 0; i < medicationList.size(); i++) {
            Medication medication = medicationList.get(i);
            assert medication != null : "Medication should not be null in display loop";
            System.out.println((i + 1) + ". " + medication.toString());
        }
    }
}
