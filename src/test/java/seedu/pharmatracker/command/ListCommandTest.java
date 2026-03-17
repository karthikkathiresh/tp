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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCommandTest {
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
    public void execute_emptyInventory_printsEmptyMessage() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        new ListCommand().execute(inventory, ui);
        assertEquals("Inventory is empty.", out.toString().trim());
    }

    @Test
    public void execute_singleMedication_printsMedicationDetails() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui);
        String output = out.toString();
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("100mg"));
        assertTrue(output.contains("50"));
        assertTrue(output.contains("2027-01-01"));
        assertTrue(output.contains("pain"));
    }

    @Test
    public void execute_multipleMedications_printsAllWithIndex() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        new ListCommand().execute(inventory, ui);
        String output = out.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("Paracetamol"));
    }

    @Test
    public void execute_medicationWithEmptyTag_noTagInOutput() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", ""));
        new ListCommand().execute(inventory, ui);
        assertFalse(out.toString().contains("| Tag:"));
    }

    @Test
    public void execute_nonEmptyInventory_printsHeaderAndFooter() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui);
        String output = out.toString();
        assertTrue(output.contains("PharmaTracker Inventory:"));
        assertTrue(output.contains("Total Medications: 1"));
    }

    @Test
    public void execute_lowStockMedication_printsLowStockFlag() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 5, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui);
        assertTrue(out.toString().contains("[LOW STOCK]"));
    }

    @Test
    public void execute_normalStockMedication_noLowStockFlag() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, ui);
        assertFalse(out.toString().contains("[LOW STOCK]"));
    }

    @Test
    public void execute_multipleMedications_correctTotalCount() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 3, "2026-03-01", "pain"));
        new ListCommand().execute(inventory, ui);
        assertTrue(out.toString().contains("Total Medications: 3"));
    }
}
