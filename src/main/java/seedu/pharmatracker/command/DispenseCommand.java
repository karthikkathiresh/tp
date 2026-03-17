package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class DispenseCommand extends Command {
    public static final String COMMAND_WORD = "dispense";
    private static final Logger logger = Logger.getLogger(DispenseCommand.class.getName());

    private final int index;
    private final int quantity;

    public DispenseCommand(int index, int quantity) {
        this.index = index;
        this.quantity = quantity;
    }

    @Override
    public void execute(Inventory inventory, Ui ui) {
        logger.log(Level.INFO, "Executing DispenseCommand: index={0}, quantity={1}",
                new Object[]{index, quantity});
        if (index < 1 || index > inventory.getMedications().size()) {
            logger.log(Level.WARNING, "Invalid index: {0}", index);
            System.out.println("Invalid index. Please enter a valid index.");
            return;
        }
        Medication med = inventory.getMedication(index - 1);
        if (quantity > med.getQuantity()) {
            logger.log(Level.WARNING, "Insufficient stock for {0}: requested={1}, available={2}",
                    new Object[]{med.getName(), quantity, med.getQuantity()});
            System.out.println("Insufficient stock. Current stock: " + med.getQuantity());
            return;
        }
        med.setQuantity(med.getQuantity() - quantity);
        logger.log(Level.INFO, "Dispensed {0} units of {1}. Updated stock: {2}",
                new Object[]{quantity, med.getName(), med.getQuantity()});
        System.out.println("Dispensing successfully!");
        System.out.println("Medication: " + med.getName());
        System.out.println("Amount: " + quantity + " units");
        System.out.println("Updated Stock: " + med.getQuantity() + " units");
    }
}
