package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Customer;
import seedu.pharmatracker.data.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to view the full details of a specific customer,
 * including their personal information and dispensing history.
 */
public class ViewCustomerCommand extends Command {

    public static final String COMMAND_WORD = "viewcustomer";

    private static final Logger logger = Logger.getLogger(ViewCustomerCommand.class.getName());
    private final int index;

    /**
     * Constructs a ViewCustomerCommand with the specified 1-based index.
     *
     * @param index The 1-based index of the customer to view.
     */
    public ViewCustomerCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the view customer command by retrieving the customer at the
     * given index and printing their full details including dispensing history.
     *
     * @param inventory    The current medication inventory.
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        logger.log(Level.INFO, "Executing ViewCustomerCommand for index: " + index);

        assert customerList != null : "CustomerList cannot be null in ViewCustomerCommand";
        assert ui != null : "Ui cannot be null in ViewCustomerCommand";

        if (customerList.size() == 0) {
            System.out.println("No customers registered yet.");
            return;
        }

        if (index < 1 || index > customerList.size()) {
            System.out.println("Invalid index. Please enter a number between 1 and "
                    + customerList.size() + ".");
            return;
        }

        Customer customer = customerList.getCustomer(index - 1);
        ui.showCustomerDetails(customer);

        logger.log(Level.INFO, "Successfully displayed details for customer: "
                + customer.getCustomerId());
    }
}
