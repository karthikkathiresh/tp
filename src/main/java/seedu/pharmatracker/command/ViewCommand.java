package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    private final int index;

    public ViewCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(Inventory inventory, Ui ui) {
        if (inventory.getMedications().isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        if (index < 1 || index > inventory.getMedications().size()) {
            System.out.println("Invalid index. Please enter a number between 1 and "
                    + inventory.getMedications().size() + ".");
            return;
        }
        Medication med = inventory.getMedication(index - 1);
        ui.printMedicationDetails(med);
    }
}
