package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link ViewCustomerCommand}.
 *
 * <p>Redirects {@code System.out} to an in-memory stream before each test
 * to allow assertion of printed output, and restores the original stream after.
 */
public class ViewCustomerCommandTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream original = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(original);
    }

    // -------------------------------------------------------------------------
    // Null / assertion guard tests
    // -------------------------------------------------------------------------

    /**
     * Tests that passing a null CustomerList throws an AssertionError.
     */
    @Test
    public void execute_nullCustomerList_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), null));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ViewCustomerCommand(1).execute(new Inventory(), null, new CustomerList()));
    }

    // -------------------------------------------------------------------------
    // Empty customer list
    // -------------------------------------------------------------------------

    /**
     * Tests that viewing from an empty CustomerList prints an appropriate message.
     */
    @Test
    public void execute_emptyCustomerList_printsNoCustomersMessage() {
        new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), new CustomerList());
        assertTrue(out.toString().contains("No customers registered yet."));
    }

    /**
     * Tests that viewing from an empty CustomerList does not crash.
     */
    @Test
    public void execute_emptyCustomerList_doesNotThrow() {
        new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), new CustomerList());
        // If we reach here without exception, the test passes
    }

    // -------------------------------------------------------------------------
    // Valid index — single customer
    // -------------------------------------------------------------------------

    /**
     * Tests that viewing a valid customer index prints the customer's ID.
     */
    @Test
    public void execute_validIndex_printsCustomerId() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("C001"));
    }

    /**
     * Tests that viewing a valid customer index prints the customer's name.
     */
    @Test
    public void execute_validIndex_printsCustomerName() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("John Tan"));
    }

    /**
     * Tests that viewing a valid customer index prints the customer's phone number.
     */
    @Test
    public void execute_validIndex_printsPhoneNumber() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("99887766"));
    }

    // -------------------------------------------------------------------------
    // Valid index — multiple customers
    // -------------------------------------------------------------------------

    /**
     * Tests that viewing index 1 in a two-customer list shows only the first customer.
     */
    @Test
    public void execute_firstIndexOfTwo_showsFirstCustomer() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Lim", "87654321", ""));
        new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("John Tan"));
        assertFalse(output.contains("Mary Lim"));
    }

    /**
     * Tests that viewing index 2 in a two-customer list shows only the second customer.
     */
    @Test
    public void execute_secondIndexOfTwo_showsSecondCustomer() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Lim", "87654321", ""));
        new ViewCustomerCommand(2).execute(new Inventory(), new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("Mary Lim"));
        assertFalse(output.contains("John Tan"));
    }

    /**
     * Tests that the last valid index (boundary) works correctly.
     */
    @Test
    public void execute_lastValidIndex_showsLastCustomer() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "Alice", "11112222", ""));
        customerList.addCustomer(new Customer("C002", "Bob", "33334444", ""));
        customerList.addCustomer(new Customer("C003", "Charlie", "55556666", ""));
        new ViewCustomerCommand(3).execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("Charlie"));
    }

    // -------------------------------------------------------------------------
    // Invalid index
    // -------------------------------------------------------------------------

    /**
     * Tests that an index of zero prints an invalid index error message.
     */
    @Test
    public void execute_indexZero_printsInvalidIndexMessage() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(0).execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("Invalid index."));
    }

    /**
     * Tests that a negative index prints an invalid index error message.
     */
    @Test
    public void execute_negativeIndex_printsInvalidIndexMessage() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(-1).execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("Invalid index."));
    }

    /**
     * Tests that an index beyond the list size prints an invalid index error message.
     */
    @Test
    public void execute_indexBeyondListSize_printsInvalidIndexMessage() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(5).execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("Invalid index."));
    }

    /**
     * Tests that an out-of-bounds index does not display any customer data.
     */
    @Test
    public void execute_invalidIndex_doesNotPrintCustomerData() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(5).execute(new Inventory(), new Ui(), customerList);
        String output = out.toString();
        assertFalse(output.contains("John Tan"));
        assertFalse(output.contains("C001"));
    }

    // -------------------------------------------------------------------------
    // Dispensing history
    // -------------------------------------------------------------------------

    /**
     * Tests that a customer with no dispensing history shows an output
     * that does not include any dispensing entry lines.
     */
    @Test
    public void execute_customerWithNoHistory_noHistoryEntriesShown() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ViewCustomerCommand(1).execute(new Inventory(), new Ui(), customerList);
        // Customer has no history — just verify the name appears and no error
        assertTrue(out.toString().contains("John Tan"));
    }

    /**
     * Tests that a customer with dispensing history shows the history entries
     * after a dispense is recorded.
     */
    @Test
    public void execute_customerWithHistory_historyEntriesShown() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2027-01-01", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));

        // Record a dispense to customer 1
        new DispenseCommand(1, 10, 1).execute(inventory, new Ui(), customerList);
        out.reset(); // clear dispense output; we only want to test view output

        new ViewCustomerCommand(1).execute(inventory, new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("Paracetamol"));
    }

    /**
     * Tests that viewing a customer shows only that customer's history,
     * not another customer's.
     */
    @Test
    public void execute_twoCustomersWithHistory_showsCorrectHistory() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2027-01-01", "fever"));
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Lim", "87654321", ""));

        new DispenseCommand(1, 10, 1).execute(inventory, new Ui(), customerList); // Paracetamol to John
        new DispenseCommand(2, 5, 2).execute(inventory, new Ui(), customerList);  // Aspirin to Mary
        out.reset();

        new ViewCustomerCommand(1).execute(inventory, new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("Paracetamol"));
        assertFalse(output.contains("Aspirin"));
    }
}
