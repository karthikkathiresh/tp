package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

public class LabelCommandTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ui = new Ui();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_validIndex_printsLabel() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2027-12-01", "painkiller"));

        new LabelCommand(1).execute(inventory, ui, customerList);
        String output = outContent.toString();

        assertTrue(output.contains("Medication Label 1"));
        assertTrue(output.contains("Name   : Paracetamol"));
        assertTrue(output.contains("Dosage : 500mg"));
        assertTrue(output.contains("Expiry : 2027-12-01"));
        assertTrue(output.contains("Tag    : painkiller"));
    }

    @Test
    public void execute_validIndexNoTag_omitsTagLine() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", ""));

        new LabelCommand(1).execute(inventory, ui, customerList);
        String output = outContent.toString();

        assertTrue(output.contains("Name   : Aspirin"));
        assertTrue(!output.contains("Tag    :"));
    }

    @Test
    public void execute_emptyInventory_printsEmptyMessage() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        new LabelCommand(1).execute(inventory, ui, customerList);
        String output = outContent.toString();

        assertTrue(output.contains("Inventory is empty."));
    }

    @Test
    public void execute_indexTooHigh_printsInvalidIndex() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));

        new LabelCommand(5).execute(inventory, ui, customerList);
        String output = outContent.toString();

        assertTrue(output.contains("Invalid index"));
    }

    @Test
    public void execute_indexZero_printsInvalidIndex() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));

        new LabelCommand(0).execute(inventory, ui, customerList);
        String output = outContent.toString();

        assertTrue(output.contains("Invalid index"));
    }

    @Test
    public void execute_negativeIndex_printsInvalidIndex() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));

        new LabelCommand(-1).execute(inventory, ui, customerList);
        String output = outContent.toString();

        assertTrue(output.contains("Invalid index"));
    }

    @Test
    public void execute_secondMedication_printsCorrectLabel() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));
        inventory.addMedication(new Medication("Ibuprofen", "400mg", 30, "2026-06-15", "anti-inflammatory"));

        new LabelCommand(2).execute(inventory, ui, customerList);
        String output = outContent.toString();

        assertTrue(output.contains("Medication Label 2"));
        assertTrue(output.contains("Name   : Ibuprofen"));
        assertTrue(output.contains("Dosage : 400mg"));
        assertTrue(output.contains("Expiry : 2026-06-15"));
        assertTrue(output.contains("Tag    : anti-inflammatory"));
    }
}
