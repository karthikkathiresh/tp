package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class DeleteCommand extends Command {

    private final String description;

    public DeleteCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(Inventory inventory, Ui ui) {
        int index = Integer.parseInt(description);
        int zeroBasedIndex = index - 1;
        Medication med = inventory.getMedication(zeroBasedIndex);
        inventory.removeMedication(med);
        ui.printDeletedMessage(med, inventory);
    }
}
