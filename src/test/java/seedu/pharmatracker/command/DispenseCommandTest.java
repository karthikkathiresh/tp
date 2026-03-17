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

    @Test
    public void execute_validDispense_reducesQuantity() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 150, "2026-12-31", "fever"));
        new DispenseCommand(1, 20).execute(inventory, ui);
        assertEquals(130, inventory.getMedication(0).getQuantity());
    }

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

    @Test
    public void execute_quantityExceedsStock_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 20).execute(inventory, ui);
        assertTrue(out.toString().contains("Insufficient stock."));
        assertEquals(10, inventory.getMedication(0).getQuantity());
    }

    @Test
    public void execute_exactStockDispensed_quantityBecomesZero() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(1, 10).execute(inventory, ui);
        assertEquals(0, inventory.getMedication(0).getQuantity());
        assertTrue(out.toString().contains("Dispensing successfully!"));
    }

    @Test
    public void execute_invalidIndexTooHigh_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(5, 1).execute(inventory, ui);
        assertTrue(out.toString().contains("Invalid index."));
    }

    @Test
    public void execute_invalidIndexZero_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, "2027-01-01", "pain"));
        new DispenseCommand(0, 1).execute(inventory, ui);
        assertTrue(out.toString().contains("Invalid index."));
    }
}
