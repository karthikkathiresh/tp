package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    // public final String description;

    private final String name;
    private final String dosage;
    private final int quantity;
    private final String expiryDate;
    private final String tag;

    // For reference, the format of the description will be
    // /n NAME /d DOSAGE /q QUANTITY /e EXPIRY /t TAG

    public AddCommand(String name, String dosage, int quantity, String expiryDate, String tag) {
        // this.description = description;
        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.tag = tag;
    }

    @Override
    public void execute(Inventory inventory) {
        Medication med = new Medication(name, dosage, quantity, expiryDate, tag);
        inventory.addMedication(med);
    }

}
