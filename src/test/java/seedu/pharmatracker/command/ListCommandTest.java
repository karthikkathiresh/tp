package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link ListCommand}.
 *
 * <p>Redirects {@code System.out} before each test and restores it after,
 * allowing assertion of printed output without coupling to the UI layer.
 */
public class ListCommandTest {
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
     * Tests that executing {@link ListCommand} on an empty inventory
     * prints the empty-inventory message.
     */
    @Test
    public void execute_emptyInventory_printsEmptyMessage() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        new ListCommand().execute(inventory, ui, customerList);
        assertEquals("Inventory is empty.", out.toString().trim());
    }

    /**
     * Tests that executing {@link ListCommand} with a single medication
     * prints all fields: name, dosage, quantity, expiry date, and tag.
     */
    @Test
    public void execute_singleMedication_printsMedicationDetails() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("100mg"));
        assertTrue(output.contains("50"));
        assertTrue(output.contains("2027-01-01"));
        assertTrue(output.contains("pain"));
    }

    /**
     * Tests that executing {@link ListCommand} with multiple medications
     * prints each entry with a 1-based index prefix.
     */
    @Test
    public void execute_multipleMedications_printsAllWithIndex() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        new ListCommand().execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("Paracetamol"));
    }

    /**
     * Tests that a medication with an empty tag string does not produce
     * a "Tag:" line in the output.
     */
    @Test
    public void execute_medicationWithEmptyTag_noTagInOutput() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", ""));
        new ListCommand().execute(inventory, ui, customerList);
        assertFalse(out.toString().contains("| Tag:"));
    }

    /**
     * Tests that executing {@link ListCommand} on a non-empty inventory
     * prints both the inventory header and the total medication count footer.
     */
    @Test
    public void execute_nonEmptyInventory_printsHeaderAndFooter() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("PharmaTracker Inventory:"));
        assertTrue(output.contains("Total Medications: 1"));
    }

    /**
     * Tests that a medication below the low-stock threshold is flagged
     * with {@code [LOW STOCK]} in the output.
     */
    @Test
    public void execute_lowStockMedication_printsLowStockFlag() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 5, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui, customerList);
        assertTrue(out.toString().contains("[LOW STOCK]"));
    }

    /**
     * Tests that a medication above the low-stock threshold does not
     * produce a {@code [LOW STOCK]} flag in the output.
     */
    @Test
    public void execute_normalStockMedication_noLowStockFlag() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui, customerList);
        assertFalse(out.toString().contains("[LOW STOCK]"));
    }

    /**
     * Tests that the total medication count in the footer reflects the
     * exact number of medications added to the inventory.
     */
    @Test
    public void execute_multipleMedications_correctTotalCount() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 3, "2026-03-01", "pain"));
        new ListCommand().execute(inventory, ui, customerList);
        assertTrue(out.toString().contains("Total Medications: 3"));
    }
}
