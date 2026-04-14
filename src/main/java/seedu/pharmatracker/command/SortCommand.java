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
import seedu.pharmatracker.customer.CustomerList;

/**
 * Sorts medications in the inventory by expiry date.
 * Medications with invalid expiry dates are treated as having the latest possible expiry date.
 */
//@@author yaqi66
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
            ui.printMessage("Inventory is empty.");
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

            try {
                return LocalDate.parse(expiryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e)  {
                logger.log(Level.WARNING, "Unexpected date format in inventory: " + expiryDate);
                return LocalDate.MAX;
            }
        }));

        logger.log(Level.INFO, "Medications sorted successfully");

        // Display sorted medications
        StringBuilder sorted = new StringBuilder("Medications sorted by expiry date:");
        for (int i = 0; i < medicationList.size(); i++) {
            Medication medication = medicationList.get(i);
            assert medication != null : "Medication should not be null in display loop";
            sorted.append("\n").append(i + 1).append(". ").append(medication.toString());
        }
        ui.printMessage(sorted.toString());
    }
}
