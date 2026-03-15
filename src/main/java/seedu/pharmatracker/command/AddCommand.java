package seedu.pharmatracker.command;

import java.util.ArrayList;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public final String description;

    // For reference, the format of the description will be
    // /n NAME /d DOSAGE /q QUANTITY /e EXPIRY /t TAG

    public AddCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(Inventory inventory) {
        int nameIndex = description.indexOf("/n");
        int dosageIndex = description.indexOf("/d");
        int quantityIndex = description.indexOf("/q");
        int expiryIndex = description.indexOf("/e");
        int tagIndex = description.indexOf("/t");

        String name = description.substring(nameIndex + 2, dosageIndex).trim();
        String dosage = description.substring(dosageIndex + 2, quantityIndex).trim();
        int quantity = Integer.parseInt(description.substring(quantityIndex + 2, expiryIndex).trim());
        String expiryDate = description.substring(expiryIndex + 2, tagIndex).trim();
        String tag = description.substring(tagIndex + 2).trim();

        Medication newMedication = new Medication(name, dosage, quantity, expiryDate, tag);
        inventory.addMedication(newMedication);
        String medicationTest = newMedication.toString();
        System.out.println(medicationTest);
    }

}
