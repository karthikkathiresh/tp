package seedu.pharmatracker.command;

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

    private final String customerId;
    private final String name;
    private final String phone;
    private final String address;

    /**
     * Constructs an AddCustomerCommand with the specified customer details.
     *
     * @param customerId Unique identifier for the customer.
     * @param name       Full name of the customer.
     * @param phone      Contact phone number of the customer.
     * @param address    Residential address of the customer (can be empty if not provided).
     */
    public AddCustomerCommand(String customerId, String name, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
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
        Customer customer = new Customer(customerId, name, phone, address);
        customerList.addCustomer(customer);
        ui.printAddedCustomerMessage(customer, customerList);
    }
}
