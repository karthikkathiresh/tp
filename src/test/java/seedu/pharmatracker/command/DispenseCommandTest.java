package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link DispenseCommand}.
 *
 * <p>Redirects {@code System.out} to an in-memory stream before each test
 * to allow assertion of printed output, and restores the original stream after.
 */
public class DispenseCommandTest {
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
     * Tests that a valid dispense operation reduces the medication's
     * quantity in the inventory by the dispensed amount.
     */
    @Test
    public void execute_validDispense_reducesQuantity() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20).execute(inventory, ui);
        assertEquals(130, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that a valid dispense operation prints a success message containing
     * the medication name, dispensed amount, and updated stock level.
     */
    @Test
    public void execute_validDispense_printsSuccessMessage() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20).execute(inventory, ui);
        String output = out.toString();
        assertTrue(output.contains("Dispensing successfully!"));
        assertTrue(output.contains("Medication: Paracetamol"));
        assertTrue(output.contains("Amount: 20 units"));
        assertTrue(output.contains("Updated Stock: 130 units"));
    }

    /**
     * Tests that dispensing more than the available stock prints an error message
     * and leaves the inventory quantity unchanged.
     */
    @Test
    public void execute_quantityExceedsStock_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 20).execute(inventory, ui);
        assertTrue(out.toString().contains("Insufficient stock."));
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that dispensing exactly the full stock reduces the quantity to zero
     * and still prints a success message.
     */
    @Test
    public void execute_exactStockDispensed_quantityBecomesZero() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 10).execute(inventory, ui);
        assertEquals(0, inventory.getMedication(0).getQuantity());
        assertTrue(out.toString().contains("Dispensing successfully!"));
    }

    /**
     * Tests that providing an index greater than the inventory size prints
     * an invalid index error message.
     */
    @Test
    public void execute_invalidIndexTooHigh_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(5, 1).execute(inventory, ui);
        assertTrue(out.toString().contains("Invalid index."));
    }

    /**
     * Tests that providing an index of zero, which is below the valid 1-based
     * range, prints an invalid index error message.
     */
    @Test
    public void execute_invalidIndexZero_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(0, 1).execute(inventory, ui);
        assertTrue(out.toString().contains("Invalid index."));
    }
}
