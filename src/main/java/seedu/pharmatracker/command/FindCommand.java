package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;


/**
 * Represents a command to find medications in the inventory by a keyword.
 * The search is case-insensitive and matches against medication names.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    private static final Logger logger = Logger.getLogger(FindCommand.class.getName());
    private final String keyword;

    /**
     * Constructs a FindCommand with the specified search keyword.
     *
     * @param keyword The keyword to search for in medication names.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by searching the {@link Inventory} for medications
     * whose names contain the keyword (case-insensitive), and printing the matching results.
     *
     * @param inventory The current inventory containing all stored medications.
     * @param ui        The user interface used to display messages and interact with the user.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        logger.log(Level.INFO, "Starting execution of FindCommand with keyword: " + keyword);

        assert inventory != null : "Inventory cannot be null in FindCommand";
        assert ui != null : "Ui cannot be null in FindCommand";

        ArrayList<Medication> medicationList = inventory.getMedications();
        ArrayList<Medication> matchingMedications = new ArrayList<>();

        for (Medication med : medicationList) {
            if (med.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matchingMedications.add(med);
            }
        }

        if (matchingMedications.isEmpty()) {
            logger.log(Level.INFO, "No medications found matching keyword: " + keyword);
            System.out.println("No medications found matching: " + keyword);
            return;
        }

        System.out.println("Found " + matchingMedications.size() + " matching medication(s):");
        for (int i = 0; i < matchingMedications.size(); i++) {
            System.out.println((i + 1) + ". " + matchingMedications.get(i).toString());
        }

        logger.log(Level.INFO, "Successfully executed FindCommand. Found "
            + matchingMedications.size() + " result(s).");
    }
}
