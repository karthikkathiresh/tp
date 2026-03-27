package seedu.pharmatracker.command;

import java.util.ArrayList;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

public class AddCustomerCommand extends Command {

    public static final String COMMAND_WORD = "add customer";

    private final String customerId;
    private final String name;
    private final String phone;
    private final String address;

    public AddCustomerCommand(String customerId, String name, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        Customer customer = new Customer(customerId, name, phone, address);
        customerList.addCustomer(customer);
    }
}
