package seedu.pharmatracker.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class LowStockCommandTest {

    private Inventory inventory;
    private Ui ui;
    private CustomerList customerList;
    private ByteArrayOutputStream outContent;
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        customerList = new CustomerList();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_defaultThreshold_listsOnlyBelowTwenty() {
        inventory.addMedication(new Medication("Ibuprofen", "400mg", 5, "15/06/2026", ""));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 25, "01/01/2027", ""));

        new LowStockCommand().execute(inventory, ui, customerList);

        String output = outContent.toString();
        assertTrue(output.contains("Ibuprofen"));
        assertTrue(output.contains("below 20"));
        assertTrue(output.contains("1 medication(s) low on stock"));
    }

    @Test
    public void execute_defaultThreshold_excludesAboveOrEqualTwenty() {
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "01/01/2027", ""));
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "01/01/2027", ""));

        new LowStockCommand().execute(inventory, ui, customerList);

        String output = outContent.toString();
        assertTrue(output.contains("No medications are low on stock"));
    }

    @Test
    public void execute_customThreshold_listsAllBelowCustomValue() {
        inventory.addMedication(new Medication("Ibuprofen", "400mg", 5, "15/06/2026", ""));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 25, "01/01/2027", ""));
        inventory.addMedication(new Medication("Aspirin", "100mg", 60, "01/01/2027", ""));

        new LowStockCommand(50).execute(inventory, ui, customerList);

        String output = outContent.toString();
        assertTrue(output.contains("Ibuprofen"));
        assertTrue(output.contains("Paracetamol"));
        assertTrue(output.contains("below 50"));
        assertTrue(output.contains("2 medication(s) low on stock"));
    }

    @Test
    public void execute_emptyInventory_printsNoLowStockMessage() {
        new LowStockCommand().execute(inventory, ui, customerList);

        String output = outContent.toString();
        assertTrue(output.contains("No medications are low on stock"));
    }

    @Test
    public void execute_allMedicationsLowStock_listsAll() {
        inventory.addMedication(new Medication("MedA", "10mg", 3, "01/01/2027", ""));
        inventory.addMedication(new Medication("MedB", "20mg", 10, "01/01/2027", ""));

        new LowStockCommand().execute(inventory, ui, customerList);

        String output = outContent.toString();
        assertTrue(output.contains("MedA"));
        assertTrue(output.contains("MedB"));
        assertTrue(output.contains("2 medication(s) low on stock"));
    }

    @Test
    public void execute_outputContainsMedicationDetails() {
        inventory.addMedication(new Medication("Ibuprofen", "400mg", 5, "15/06/2026", ""));

        new LowStockCommand().execute(inventory, ui, customerList);

        String output = outContent.toString();
        assertTrue(output.contains("Ibuprofen"));
        assertTrue(output.contains("400mg"));
        assertTrue(output.contains("Qty: 5"));
        assertTrue(output.contains("Expiry: 15/06/2026"));
    }

    @Test
    public void execute_defaultThreshold_correctConstantValue() {
        assertEquals(20, LowStockCommand.DEFAULT_THRESHOLD);
    }

    @Test
    public void execute_quantityExactlyAtThreshold_notIncluded() {
        inventory.addMedication(new Medication("Ibuprofen", "400mg", 20, "15/06/2026", ""));

        new LowStockCommand().execute(inventory, ui, customerList);

        String output = outContent.toString();
        assertTrue(output.contains("No medications are low on stock"));
    }
}
