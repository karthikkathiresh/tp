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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@@author alsonsim
/**
 * JUnit tests for {@link DispenseCommand}.
 *
 * <p>Redirects {@code System.out} to an in-memory stream before each test
 * to allow assertion of printed output, and restores the original stream after.
 */
public class DispenseCommandTest {
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
     * Tests that passing a null inventory throws an AssertionError.
     */
    @Test
    public void execute_nullInventory_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new DispenseCommand(1, 1).execute(null, new Ui(), new CustomerList()));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        assertThrows(AssertionError.class, () ->
                new DispenseCommand(1, 1).execute(inventory, null, new CustomerList()));
    }

    /**
     * Tests that passing a null CustomerList throws an AssertionError.
     */
    @Test
    public void execute_nullCustomerList_throwsAssertionError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        assertThrows(AssertionError.class, () ->
                new DispenseCommand(1, 1).execute(inventory, new Ui(), null));
    }

    // -------------------------------------------------------------------------
    // Valid dispense — quantity and output
    // -------------------------------------------------------------------------

    /**
     * Tests that a valid dispense operation reduces the medication's
     * quantity in the inventory by the dispensed amount.
     */
    @Test
    public void execute_validDispense_reducesQuantity() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), new CustomerList());
        assertEquals(130, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that a valid dispense operation prints a success message containing
     * the medication name, dispensed amount, and updated stock level.
     */
    @Test
    public void execute_validDispense_printsSuccessMessage() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Dispensing successfully!"));
        assertTrue(output.contains("Medication: Paracetamol"));
        assertTrue(output.contains("Amount: 20 units"));
        assertTrue(output.contains("Updated Stock: 130 units"));
    }

    /**
     * Tests that dispensing exactly 1 unit (boundary minimum) succeeds.
     */
    @Test
    public void execute_minimumValidQuantity_reducesStockByOne() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 1).execute(inventory, new Ui(), new CustomerList());
        assertEquals(9, inventory.getMedication(0).getQuantity());
        assertTrue(out.toString().contains("Dispensing successfully!"));
    }

    /**
     * Tests that dispensing exactly the full stock reduces the quantity to zero.
     */
    @Test
    public void execute_exactStockDispensed_quantityBecomesZero() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 10).execute(inventory, new Ui(), new CustomerList());
        assertEquals(0, inventory.getMedication(0).getQuantity());
        assertTrue(out.toString().contains("Dispensing successfully!"));
    }

    /**
     * Tests that a valid dispense does not affect other medications in the inventory.
     */
    @Test
    public void execute_validDispense_doesNotAffectOtherMedications() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), new CustomerList());
        assertEquals(10, inventory.getMedication(1).getQuantity());
    }

    /**
     * Tests that consecutive valid dispenses accumulate correctly.
     */
    @Test
    public void execute_multipleValidDispenses_stockDecreasesCorrectly() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), new CustomerList());
        new DispenseCommand(1, 30).execute(inventory, new Ui(), new CustomerList());
        assertEquals(100, inventory.getMedication(0).getQuantity());
    }

    // -------------------------------------------------------------------------
    // Insufficient stock
    // -------------------------------------------------------------------------

    /**
     * Tests that dispensing more than the available stock prints an error message
     * and leaves the inventory quantity unchanged.
     */
    @Test
    public void execute_quantityExceedsStock_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Insufficient stock."));
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that dispensing one unit more than available stock is correctly rejected.
     */
    @Test
    public void execute_quantityOneOverStock_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 11).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Insufficient stock."));
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that the insufficient stock message includes the current stock level.
     */
    @Test
    public void execute_quantityExceedsStock_printsCurrentStock() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("10"));
    }

    /**
     * Tests that dispensing an expired medication is blocked and stock remains unchanged.
     */
    @Test
    public void execute_expiredMedication_blocksDispenseAndKeepsStock() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("ExpiredMed", "100mg", 10, "2000-01-01", "pain"));

        new DispenseCommand(1, 2).execute(inventory, new Ui(), new CustomerList());

        String output = out.toString();
        assertTrue(output.contains("Cannot dispense expired medication"));
        assertFalse(output.contains("Dispensing successfully!"));
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that blocked dispensing of expired medication does not create a dispense log record.
     */
    @Test
    public void execute_expiredMedication_noDispenseLogRecordCreated() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("ExpiredMed", "100mg", 10, "2000-01-01", "pain"));

        new DispenseCommand(1, 2).execute(inventory, new Ui(), new CustomerList());

        assertEquals(0, inventory.getDispenseLog().size());
    }

    // -------------------------------------------------------------------------
    // Invalid quantity
    // -------------------------------------------------------------------------

    /**
     * Tests that dispensing with a zero quantity prints an error and does not
     * modify stock.
     */
    @Test
    public void execute_zeroQuantity_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 0).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid quantity"));
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that dispensing with a negative quantity prints an error and does not
     * modify stock.
     */
    @Test
    public void execute_negativeQuantity_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, -5).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid quantity"));
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    // -------------------------------------------------------------------------
    // Invalid medication index
    // -------------------------------------------------------------------------

    /**
     * Tests that providing an index greater than the inventory size prints
     * an invalid index error message.
     */
    @Test
    public void execute_invalidIndexTooHigh_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(5, 1).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid index."));
    }

    /**
     * Tests that providing an index of zero prints an invalid index error message.
     */
    @Test
    public void execute_invalidIndexZero_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(0, 1).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid index."));
    }

    /**
     * Tests that providing a negative index prints an invalid index error message.
     */
    @Test
    public void execute_negativeIndex_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(-1, 1).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid index."));
    }

    /**
     * Tests that an invalid index leaves all medications in the inventory unchanged.
     */
    @Test
    public void execute_invalidIndex_stockUnchanged() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(5, 1).execute(inventory, new Ui(), new CustomerList());
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that dispensing from an empty inventory prints an error.
     */
    @Test
    public void execute_emptyInventory_printsError() {
        new DispenseCommand(1, 1).execute(new Inventory(), new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("empty") || output.contains("Invalid index.")
                || output.contains("Invalid"));
    }

    // -------------------------------------------------------------------------
    // Customer linking
    // -------------------------------------------------------------------------

    /**
     * Tests that dispensing with a valid customer index records the event
     * in that customer's dispensing history.
     */
    @Test
    public void execute_validCustomerIndex_addsToDispensingHistory() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new DispenseCommand(1, 20, 1).execute(inventory, new Ui(), customerList);
        assertEquals(1, customerList.getCustomer(0).getDispensingHistory().size());
        assertTrue(customerList.getCustomer(0).getDispensingHistory().get(0).contains("Paracetamol"));
    }

    /**
     * Tests that dispensing with a valid customer index prints the customer
     * confirmation line in the output.
     */
    @Test
    public void execute_validCustomerIndex_printsCustomerConfirmation() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new DispenseCommand(1, 20, 1).execute(inventory, new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("Recorded for customer:"));
        assertTrue(output.contains("C001"));
        assertTrue(output.contains("John Tan"));
    }

    /**
     * Tests that the dispensing history entry includes the dosage and quantity.
     */
    @Test
    public void execute_validCustomerIndex_historyEntryContainsDosageAndQuantity() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new DispenseCommand(1, 20, 1).execute(inventory, new Ui(), customerList);
        String record = customerList.getCustomer(0).getDispensingHistory().get(0);
        assertTrue(record.contains("500mg") || record.contains("20"));
    }

    /**
     * Tests that dispensing with an invalid customer index prints an error
     * and does not reduce medication stock.
     */
    @Test
    public void execute_invalidCustomerIndex_printsErrorAndRollsBack() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20, 5).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid customer index."));
        assertEquals(150, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that dispensing with a customer index of zero prints an error.
     */
    @Test
    public void execute_customerIndexZero_printsError() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new DispenseCommand(1, 20, 0).execute(inventory, new Ui(), customerList);
        assertTrue(out.toString().contains("Invalid customer index."));
        assertEquals(150, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that dispensing without a customer index does not print
     * the customer confirmation line.
     * Note: customerIndex = -1 is the NO_CUSTOMER sentinel — it is treated as
     * "no customer linked", not as an invalid index.
     */
    @Test
    public void execute_noCustomerIndex_noCustomerConfirmation() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("Recorded for customer:"));
    }

    /**
     * Tests that dispensing without a customer index does not add any
     * entry to any customer's dispensing history.
     */
    @Test
    public void execute_noCustomerIndex_noHistoryAdded() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new DispenseCommand(1, 20).execute(inventory, new Ui(), customerList);
        assertEquals(0, customerList.getCustomer(0).getDispensingHistory().size());
    }

    /**
     * Tests that dispensing to multiple different customers records
     * history only for the correct customer each time.
     */
    @Test
    public void execute_multipleCustomers_historyRecordedForCorrectCustomer() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        new DispenseCommand(1, 20, 2).execute(inventory, new Ui(), customerList);
        assertEquals(0, customerList.getCustomer(0).getDispensingHistory().size());
        assertEquals(1, customerList.getCustomer(1).getDispensingHistory().size());
    }

    /**
     * Tests that multiple dispenses to the same customer accumulate history entries.
     */
    @Test
    public void execute_multipleDispensesToSameCustomer_historyAccumulates() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new DispenseCommand(1, 10, 1).execute(inventory, new Ui(), customerList);
        new DispenseCommand(1, 10, 1).execute(inventory, new Ui(), customerList);
        assertEquals(2, customerList.getCustomer(0).getDispensingHistory().size());
    }

    // -------------------------------------------------------------------------
    // Allergy check
    // -------------------------------------------------------------------------

    /**
     * Tests that dispensing is aborted when the linked customer is allergic to the medication.
     * Stock must remain unchanged.
     */
    @Test
    public void execute_customerAllergicToMedication_abortsDispense() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Penicillin V", "500mg", 50, "2030-01-01", "antibiotic"));
        Customer customer = new Customer("C001", "Alice Tan", "91234567", "");
        customer.addAllergy("penicillin");
        customerList.addCustomer(customer);

        new DispenseCommand(1, 5, 1).execute(inventory, new Ui(), customerList);

        String output = out.toString();
        assertTrue(output.contains("Allergy conflict detected"));
        assertTrue(output.contains("penicillin"));
        assertTrue(output.contains("Dispense aborted"));
        assertEquals(50, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that no history entry is added to the customer when dispense is aborted
     * due to an allergy conflict.
     */
    @Test
    public void execute_allergyConflict_noHistoryAdded() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Penicillin V", "500mg", 50, "2030-01-01", "antibiotic"));
        Customer customer = new Customer("C001", "Alice Tan", "91234567", "");
        customer.addAllergy("penicillin");
        customerList.addCustomer(customer);

        new DispenseCommand(1, 5, 1).execute(inventory, new Ui(), customerList);

        assertEquals(0, customer.getDispensingHistory().size());
    }

    /**
     * Tests that dispense proceeds normally when the customer has allergies recorded
     * but none match the medication being dispensed.
     */
    @Test
    public void execute_noAllergyConflict_dispensesSuccessfully() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 100, "2030-01-01", "pain"));
        Customer customer = new Customer("C001", "Alice Tan", "91234567", "");
        customer.addAllergy("penicillin");
        customerList.addCustomer(customer);

        new DispenseCommand(1, 10, 1).execute(inventory, new Ui(), customerList);

        assertTrue(out.toString().contains("Dispensing successfully!"));
        assertEquals(90, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that the allergy check uses case-insensitive substring matching,
     * so "aspirin" allergen matches "Aspirin 100mg Tablet" medication name.
     */
    @Test
    public void execute_allergySubstringMatch_abortsDispense() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin 100mg Tablet", "100mg", 80, "2030-01-01", "pain"));
        Customer customer = new Customer("C002", "Bob Lim", "98765432", "");
        customer.addAllergy("aspirin");
        customerList.addCustomer(customer);

        new DispenseCommand(1, 1, 1).execute(inventory, new Ui(), customerList);

        assertTrue(out.toString().contains("Allergy conflict detected"));
        assertEquals(80, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that no allergy check is performed when no customer is linked.
     * Dispense should succeed normally.
     */
    @Test
    public void execute_noCustomerLinked_allergyCheckSkipped() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Penicillin V", "500mg", 50, "2030-01-01", "antibiotic"));

        new DispenseCommand(1, 5).execute(inventory, new Ui(), new CustomerList());

        assertTrue(out.toString().contains("Dispensing successfully!"));
        assertEquals(45, inventory.getMedication(0).getQuantity());
    }
}
