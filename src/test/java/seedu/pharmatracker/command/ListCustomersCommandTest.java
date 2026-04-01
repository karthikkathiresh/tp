package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link ListCustomersCommand}.
 *
 * <p>Redirects {@code System.out} before each test and restores it after,
 * allowing assertion of printed output without coupling to the UI layer.
 */
public class ListCustomersCommandTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream original = System.out;

    /**
     * Redirects {@code System.out} to an in-memory stream before each test.
     */
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(out));
    }

    /**
     * Restores {@code System.out} to its original stream after each test.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(original);
    }

    /**
     * Tests that executing {@link ListCustomersCommand} on an empty customer list
     * prints the no-customers message.
     */
    @Test
    public void execute_emptyCustomerList_printsEmptyMessage() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        new ListCustomersCommand().execute(inventory, ui, customerList);
        assertTrue(out.toString().contains("No customers registered yet."));
    }

    /**
     * Tests that executing {@link ListCustomersCommand} with a single customer
     * prints their ID, name, and phone number.
     */
    @Test
    public void execute_singleCustomer_printsCustomerDetails() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ListCustomersCommand().execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("C001"));
        assertTrue(output.contains("John Tan"));
        assertTrue(output.contains("99887766"));
    }

    /**
     * Tests that executing {@link ListCustomersCommand} with multiple customers
     * prints each entry with a 1-based index prefix.
     */
    @Test
    public void execute_multipleCustomers_printsAllWithIndex() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        customerList.addCustomer(new Customer("C003", "David Ng", "93456789", ""));
        new ListCustomersCommand().execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("3."));
        assertTrue(output.contains("John Tan"));
        assertTrue(output.contains("Mary Tan"));
        assertTrue(output.contains("David Ng"));
    }

    /**
     * Tests that executing {@link ListCustomersCommand} on a non-empty list
     * prints both the header and the total customer count footer.
     */
    @Test
    public void execute_nonEmptyCustomerList_printsHeaderAndFooter() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ListCustomersCommand().execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("PharmaTracker Customers:"));
        assertTrue(output.contains("Total Customers: 1."));
    }

    /**
     * Tests that the total customer count in the footer reflects the
     * exact number of customers added to the list.
     */
    @Test
    public void execute_multipleCustomers_correctTotalCount() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        customerList.addCustomer(new Customer("C003", "David Ng", "93456789", ""));
        new ListCustomersCommand().execute(inventory, ui, customerList);
        assertTrue(out.toString().contains("Total Customers: 3."));
    }
}
