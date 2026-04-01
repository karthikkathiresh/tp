package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to dispense a specified quantity of a medication from the inventory.
 * Optionally links the dispense event to a registered customer's dispensing history.
 */
public class DispenseCommand extends Command {

    public static final String COMMAND_WORD = "dispense";

    private static final int NO_CUSTOMER = -1;

    private static final Logger logger = Logger.getLogger(DispenseCommand.class.getName());

    private final int index;
    private final int quantity;
    private final int customerIndex;

    /**
     * Constructs a DispenseCommand with no customer linking.
     *
     * @param index    1-based position of the medication in the inventory.
     * @param quantity Number of units to dispense.
     */
    public DispenseCommand(int index, int quantity) {
        this(index, quantity, NO_CUSTOMER);
    }

    /**
     * Constructs a DispenseCommand with optional customer linking.
     *
     * @param index         1-based position of the medication in the inventory.
     * @param quantity      Number of units to dispense.
     * @param customerIndex 1-based position of the customer to link, or -1 if none.
     */
    public DispenseCommand(int index, int quantity, int customerIndex) {
        this.index = index;
        this.quantity = quantity;
        this.customerIndex = customerIndex;
    }

    /**
     * Executes the dispense command by reducing the medication's stock by the specified quantity.
     * If a valid customer index is provided, records the dispense event in that customer's history.
     *
     * @param inventory    The current medication inventory.
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
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

        if (customerIndex != NO_CUSTOMER && (customerIndex < 1 || customerIndex > customerList.size())) {
            logger.log(Level.WARNING, "Invalid customer index: {0}", customerIndex);
            System.out.println("Invalid customer index. Please enter a valid index.");
            return;
        }

        med.setQuantity(med.getQuantity() - quantity);
        logger.log(Level.INFO, "Dispensed {0} units of {1}. Updated stock: {2}",
                new Object[]{quantity, med.getName(), med.getQuantity()});

        System.out.println("Dispensing successfully!");
        System.out.println("Medication: " + med.getName());
        System.out.println("Amount: " + quantity + " units");
        System.out.println("Updated Stock: " + med.getQuantity() + " units");

        if (customerIndex != NO_CUSTOMER) {
            Customer customer = customerList.getCustomer(customerIndex - 1);
            String record = med.getName() + " | " + med.getDosage()
                    + " | Qty: " + quantity;
            customer.addDispensingHistory(record);
            logger.log(Level.INFO, "Linked dispense to customer: {0}", customer.getName());
            System.out.println("Recorded for customer: [" + customer.getCustomerId() + "] "
                    + customer.getName() + ".");
        }
    }
}
