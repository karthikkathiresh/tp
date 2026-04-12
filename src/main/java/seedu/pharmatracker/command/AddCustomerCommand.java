package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to add a new customer to the database.
 * Encapsulates all necessary details required to construct a Customer object.
 */
public class AddCustomerCommand extends Command {

    public static final String COMMAND_WORD = "add-customer";

    private static final Logger logger = Logger.getLogger(AddCustomerCommand.class.getName());

    private final String customerId;
    private final String name;
    private final String phone;
    private final String address;
    private final ArrayList<String> allergies;

    /**
     * Constructs an AddCustomerCommand with the specified customer details.
     *
     * @param customerId Unique identifier for the customer.
     * @param name       Full name of the customer.
     * @param phone      Contact phone number of the customer.
     * @param address    Residential address of the customer (can be empty if not provided).
     * @param allergies  List of known allergy keywords (may be empty).
     */
    public AddCustomerCommand(String customerId, String name, String phone,
                              String address, ArrayList<String> allergies) {
        assert customerId != null && !customerId.isEmpty() : "Customer ID cannot be null or empty";
        assert name != null && !name.isEmpty() : "Customer name cannot be null or empty";
        assert phone != null && !phone.isEmpty() : "Customer phone cannot be null or empty";
        assert address != null : "Customer address cannot be null";
        assert allergies != null: "Allergies cannot be null";

        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.allergies = allergies;
    }

    /**
     * Executes the command by creating a new Customer object with the provided details
     * and adding it to the customer list. Also prints a success message to the user.
     *
     * @param inventory    The current medication inventory.
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert ui != null : "Ui cannot be null in AddCustomerCommand execution.";
        assert customerList != null : "CustomerList cannot be null in AddCustomerCommand execution.";

        logger.log(Level.INFO, "Starting execution of AddCustomerCommand for customer: " + name);

        if (customerList.containsCustomerId(customerId)) {
            ui.printMessage("Failed to add customer: A customer with ID '" + customerId + "' already exists.");
            logger.log(Level.WARNING, "Attempted to add duplicate customer ID: " + customerId);
            return;
        }

        Customer customer = new Customer(customerId, name, phone, address);
        customer.setAllergies(allergies);
        customerList.addCustomer(customer);
        ui.printAddedCustomerMessage(customer, customerList);
    }
}
