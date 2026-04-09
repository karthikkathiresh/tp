package seedu.pharmatracker.command;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.DispenseRecord;
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
    private static final int MIN_QUANTITY = 1;

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
        assert inventory != null : "Inventory must not be null";
        assert ui != null : "Ui must not be null";
        assert customerList != null : "CustomerList must not be null";

        logger.log(Level.INFO, "Executing DispenseCommand: index={0}, quantity={1}, customerIndex={2}",
                new Object[]{index, quantity, customerIndex});

        if (!isValidQuantity()) {
            logger.log(Level.WARNING, "Invalid quantity: {0}", quantity);
            System.out.println("Invalid quantity. Quantity to dispense must be at least 1.");
            return;
        }

        if (inventory.getMedications() == null || inventory.getMedications().isEmpty()) {
            logger.log(Level.WARNING, "Attempted to dispense from empty inventory.");
            System.out.println("Inventory is empty. No medications to dispense.");
            return;
        }

        if (!isValidMedicationIndex(inventory)) {
            logger.log(Level.WARNING, "Invalid medication index: {0}", index);
            System.out.println("Invalid index. Please enter a valid medication index.");
            return;
        }

        Medication med = inventory.getMedication(index - 1);

        if (med == null) {
            logger.log(Level.SEVERE, "Medication at index {0} returned null unexpectedly.", index);
            System.out.println("An unexpected error occurred. Medication not found.");
            return;
        }

        if (!hasSufficientStock(med)) {
            logger.log(Level.WARNING, "Insufficient stock for {0}: requested={1}, available={2}",
                    new Object[]{med.getName(), quantity, med.getQuantity()});
            System.out.println("Insufficient stock. Current stock: " + med.getQuantity());
            return;
        }

        if (isCustomerLinked() && !isValidCustomerIndex(customerList)) {
            logger.log(Level.WARNING, "Invalid customer index: {0}", customerIndex);
            System.out.println("Invalid customer index. Please enter a valid customer index.");
            return;
        }

        performDispense(med);

        // Record to daily dispense log
        String patientName = "";
        if (isCustomerLinked() && isValidCustomerIndex(customerList)) {
            Customer logCustomer = customerList.getCustomer(customerIndex - 1);
            if (logCustomer != null) {
                patientName = logCustomer.getName();
            }
        }
        String dosageStr = (med.getDosage() != null && !med.getDosage().isEmpty()) ? med.getDosage() : "N/A";
        DispenseRecord record = new DispenseRecord(
                LocalDate.now(), LocalTime.now(),
                med.getName(), dosageStr, quantity, patientName);
        inventory.getDispenseLog().addRecord(record);

        if (isCustomerLinked()) {
            linkToCustomer(med, customerList);
        }
    }

    /**
     * Reduces the medication stock and prints the dispense confirmation.
     *
     * @param med The medication to dispense from.
     */
    private void performDispense(Medication med) {
        int updatedStock = med.getQuantity() - quantity;
        med.setQuantity(updatedStock);

        logger.log(Level.INFO, "Dispensed {0} units of {1}. Updated stock: {2}",
                new Object[]{quantity, med.getName(), updatedStock});

        System.out.println("Dispensing successfully!");
        System.out.println("Medication: " + med.getName());
        System.out.println("Amount: " + quantity + " units");
        System.out.println("Updated Stock: " + updatedStock + " units");
    }

    /**
     * Records the dispense event in the linked customer's history and prints confirmation.
     *
     * @param med          The medication that was dispensed.
     * @param customerList The list of registered customers.
     */
    private void linkToCustomer(Medication med, CustomerList customerList) {
        Customer customer = customerList.getCustomer(customerIndex - 1);

        if (customer == null) {
            logger.log(Level.SEVERE, "Customer at index {0} returned null unexpectedly.", customerIndex);
            System.out.println("An unexpected error occurred. Customer record not updated.");
            return;
        }

        String dosage = (med.getDosage() != null && !med.getDosage().isEmpty())
                ? med.getDosage()
                : "N/A";
        String record = med.getName() + " | " + dosage + " | Qty: " + quantity;
        customer.addDispensingHistory(record);

        logger.log(Level.INFO, "Linked dispense to customer: {0}", customer.getName());
        System.out.println("Recorded for customer: [" + customer.getCustomerId() + "] "
                + customer.getName() + ".");
    }

    /**
     * Returns true if the dispense quantity is at least the minimum allowed value.
     *
     * @return true if quantity is valid.
     */
    private boolean isValidQuantity() {
        return quantity >= MIN_QUANTITY;
    }

    /**
     * Returns true if the medication index is within the bounds of the inventory.
     *
     * @param inventory The current medication inventory.
     * @return true if index is valid.
     */
    private boolean isValidMedicationIndex(Inventory inventory) {
        return index >= 1 && index <= inventory.getMedications().size();
    }

    /**
     * Returns true if the medication has enough stock to fulfill the dispense request.
     *
     * @param med The target medication.
     * @return true if stock is sufficient.
     */
    private boolean hasSufficientStock(Medication med) {
        return quantity <= med.getQuantity();
    }

    /**
     * Returns true if this command is linked to a customer.
     *
     * @return true if customerIndex is not NO_CUSTOMER.
     */
    private boolean isCustomerLinked() {
        return customerIndex != NO_CUSTOMER;
    }

    /**
     * Returns true if the customer index is within the bounds of the customer list.
     *
     * @param customerList The list of registered customers.
     * @return true if customer index is valid.
     */
    private boolean isValidCustomerIndex(CustomerList customerList) {
        return customerIndex >= 1 && customerIndex <= customerList.size();
    }
}
