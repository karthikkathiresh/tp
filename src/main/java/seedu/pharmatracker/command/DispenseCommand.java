package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

public class DispenseCommand extends Command {
    public static final String COMMAND_WORD = "dispense";

    private final int index;
    private final int quantity;

    public DispenseCommand(int index, int quantity) {
        this.index = index;
        this.quantity = quantity;
    }

    @Override
    public void execute(Inventory inventory) {
        if (index < 1 || index > inventory.getMedications().size()) {
            System.out.println("Invalid index. Please enter a valid index.");
            return;
        }
        Medication med = inventory.getMedication(index - 1);
        if (quantity > med.getQuantity()) {
            System.out.println("Insufficient stock. Current stock: " + med.getQuantity());
            return;
        }
        med.setQuantity(med.getQuantity() - quantity);
        System.out.println("Dispensing successfully!");
        System.out.println("Medication: " + med.getName());
        System.out.println("Amount: " + quantity + " units");
        System.out.println("Updated Stock: " + med.getQuantity() + " units");
    }
}
