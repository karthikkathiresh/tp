package seedu.pharmatracker.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class SortCommand extends Command {

    @Override
    public void execute(Inventory inventory, Ui ui) {
        ArrayList<Medication> medicationList = inventory.getMedications();
        if (medicationList.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        medicationList.sort(Comparator.comparing(med -> {
            try {
                return LocalDate.parse(med.getExpiryDate(), formatter);
            } catch (DateTimeParseException e) {
                return LocalDate.MAX;
            }
        }));

        System.out.println("Medications sorted by expiry date:");
        for (int i = 0; i < medicationList.size(); i++) {
            Medication med = medicationList.get(i);
            System.out.println((i + 1) + ". " + med.toString());
        }
    }
}
