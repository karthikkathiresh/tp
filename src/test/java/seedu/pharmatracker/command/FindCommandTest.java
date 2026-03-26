package seedu.pharmatracker.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

//@@author yihernggggg
public class FindCommandTest {

    @Test
    public void findCommand_matchingKeyword_findsMedication() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("Amox").execute(inventory, ui, customerList);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Amoxicillin"));
    }

    @Test
    public void findCommand_caseInsensitive_findsMedication() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("paracetamol").execute(inventory, ui, customerList);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Paracetamol"));
    }

    @Test
    public void findCommand_noMatch_printsNotFound() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("Aspirin").execute(inventory, ui, customerList);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("No medications found matching: Aspirin"));
    }

    @Test
    public void findCommand_multipleMatches_findsAll() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol 500mg", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Paracetamol Extra", "1000mg", 50, "2027-01-15", "painkiller"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("Paracetamol").execute(inventory, ui, customerList);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("2 matching medication(s)"));
        assertTrue(output.contains("Paracetamol 500mg"));
        assertTrue(output.contains("Paracetamol Extra"));
    }

    @Test
    public void findCommand_emptyInventory_printsNotFound() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("anything").execute(inventory, ui, customerList);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("No medications found matching: anything"));
    }

    @Test
    public void findCommand_partialKeyword_findsMedication() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("cillin").execute(inventory, ui, customerList);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Amoxicillin"));
    }
}
