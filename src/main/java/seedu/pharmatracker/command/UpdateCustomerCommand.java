package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Updates one or more fields of an existing customer record.
 * Only the fields provided will be changed; all others remain unchanged.
 */
public class UpdateCustomerCommand extends Command {

    public static final String COMMAND_WORD = "update-customer";

    private static final Logger logger = Logger.getLogger(UpdateCustomerCommand.class.getName());

    private final int index;
    private final String name;
    private final String phone;
    private final String address;
    private final ArrayList<String> allergies;

    /**
     * Constructs an UpdateCustomerCommand without allergy update.
     * Pass {@code null} for any field that should not be updated.
     *
     * @param index   1-based index of the customer to update.
     * @param name    New name, or null to leave unchanged.
     * @param phone   New phone number, or null to leave unchanged.
     * @param address New address, or null to leave unchanged.
     */
    public UpdateCustomerCommand(int index, String name, String phone, String address) {
        this.index = index;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.allergies = null;
    }

    /**
     * Constructs an UpdateCustomerCommand. Pass {@code null} for any field that should not be updated.
     *
     * @param index    1-based index of the customer to update.
     * @param name     New name, or null to leave unchanged.
     * @param phone    New phone number, or null to leave unchanged.
     * @param address  New address, or null to leave unchanged.
     * @param allergies New allergy list (replaces existing), or null to leave unchanged.
     */
    public UpdateCustomerCommand(int index, String name, String phone,
                                 String address, ArrayList<String> allergies) {
        this.index = index;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.allergies = allergies;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert customerList != null : "CustomerList cannot be null in UpdateCustomerCommand execution.";
        assert ui != null : "Ui cannot be null in UpdateCustomerCommand execution.";
        logger.log(Level.INFO, "Starting execution of UpdateCustomerCommand for index: " + index);

        if (index < 1 || index > customerList.size()) {
            ui.printMessage("Invalid index. Please enter a number between 1 and "
                    + customerList.size() + ".");
            return;
        }

        if (name == null && phone == null && address == null && allergies == null) {
            ui.printMessage("No fields provided to update! Use /n, /p, /addr, or /allergy flags.");
            return;
        }

        Customer customer = customerList.getCustomer(index - 1);

        if (name != null) {
            customer.setName(name);
        }
        if (phone != null) {
            customer.setPhone(phone);
        }
        if (address != null) {
            customer.setAddress(address);
        }
        if (allergies != null) {
            customer.setAllergies(allergies);
        }

        ui.printUpdatedCustomerMessage(customer);
        logger.log(Level.INFO, "Successfully executed UpdateCustomerCommand.");
    }
}
