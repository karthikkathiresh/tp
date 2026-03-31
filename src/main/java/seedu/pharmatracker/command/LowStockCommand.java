package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.List;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Lists all medications whose quantity falls below a defined threshold.
 * Default threshold is 20 units; an optional /threshold flag allows a custom value.
 */
public class LowStockCommand extends Command {

    public static final String COMMAND_WORD = "lowstock";
    public static final int DEFAULT_THRESHOLD = 20;

    private final int threshold;

    public LowStockCommand(int threshold) {
        this.threshold = threshold;
    }

    public LowStockCommand() {
        this(DEFAULT_THRESHOLD);
    }

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory cannot be null in LowStockCommand execution.";
        assert ui != null : "Ui cannot be null in LowStockCommand execution.";
        List<Medication> lowStockMeds = new ArrayList<>();
        for (Medication med : inventory.getMedications()) {
            if (med.getQuantity() < threshold) {
                lowStockMeds.add(med);
            }
        }
        ui.printLowStockList(lowStockMeds, threshold);
    }
}
