package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.AbstractDocument;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to remove a customer from the database.
 * The customer to be removed is identified by his 1-based index as shown in the customer list.
 */
public class DeleteCustomerCommand extends Command {

    public static final String COMMAND_WORD = "delete-customer";

    private static final Logger logger = Logger.getLogger(DeleteCustomerCommand.class.getName());
    private final String description;

    /**
     * Constructs a DeleteCustomerCommand with the specified target index description.
     *
     * @param description The 1-based index of the customer to be deleted, provided as a String.
     */
    public DeleteCustomerCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the delete customer command by parsing the target index, locating the corresponding
     * {@link Customer} in the {@link CustomerList}, removing them, and displaying a confirmation message.
     *
     * @param inventory    The current inventory containing all stored medications (unused in this command).
     * @param ui           The user interface used to display messages and interact with the user.
     * @param customerList The list of registered customers in the system.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        logger.log(Level.INFO, "Starting execution of DeleteCommand for index: " + description);

        assert customerList != null : "Customer list cannot be empty in DeleteCustomerCommand";
        assert ui != null : "Ui cannot be null in DeleteCustomerCommand";

        try {
            int index = Integer.parseInt(description.trim());
            if (index < 1 || index > customerList.getCustomerCount()) {
                System.out.println("Invalid index. Please enter a number between 1 and "
                        + customerList.getCustomerCount() + ".");
            }
            int zeroBasedIndex = index - 1;
            Customer customer = customerList.getCustomer(zeroBasedIndex);
            customerList.removeCustomer(customer);
            ui.printDeletedCustomerMessage(customer, customerList);
            logger.log(Level.INFO, "Successfully executed DeleteCommand");
        } catch (NumberFormatException e) {
            System.out.println("Invalid format! Please enter a valid number for the customer index.");
            logger.log(Level.WARNING, "Failed to parse index in DeleteCustomerCommand: " + description);
        }
    }
}
