package seedu.pharmatracker.command;

import java.util.ArrayList;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(Inventory inventory, Ui ui) {
        ArrayList<Medication> medicationList = inventory.getMedications();
        ArrayList<Medication> matchingMedications = new ArrayList<>();

        for (Medication med : medicationList) {
            if (med.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matchingMedications.add(med);
            }
        }

        if (matchingMedications.isEmpty()) {
            System.out.println("No medications found matching: " + keyword);
            return;
        }

        System.out.println("Found " + matchingMedications.size() + " matching medication(s):");
        for (int i = 0; i < matchingMedications.size(); i++) {
            System.out.println((i + 1) + ". " + matchingMedications.get(i).toString());
        }
    }
}
