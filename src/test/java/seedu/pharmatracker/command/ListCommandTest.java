package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

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
        new ListCommand().execute(inventory);
        assertEquals("Inventory is empty.", out.toString().trim());
    }

    @Test
    public void execute_singleMedication_printsMedicationDetails() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory);
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
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        new ListCommand().execute(inventory);
        String output = out.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("Paracetamol"));
    }

    @Test
    public void execute_medicationWithEmptyTag_noTagInOutput() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", ""));
        new ListCommand().execute(inventory);
        assertFalse(out.toString().contains("| Tag:"));
    }
}
