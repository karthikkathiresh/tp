package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@@author yihernggggg
public class FindCommandTest {

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
                new FindCommand("Aspirin").execute(null, new Ui(), new CustomerList()));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new FindCommand("Aspirin").execute(new Inventory(), null, new CustomerList()));
    }

    // -------------------------------------------------------------------------
    // Matching behaviour
    // -------------------------------------------------------------------------

    /**
     * Tests that a keyword matching one medication name finds that medication.
     */
    @Test
    public void execute_matchingKeyword_findsMedication() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "painkiller"));
        new FindCommand("Amox").execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Amoxicillin"));
    }

    /**
     * Tests that the search is case-insensitive.
     */
    @Test
    public void execute_caseInsensitive_findsMedication() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        new FindCommand("paracetamol").execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Paracetamol"));
    }

    /**
     * Tests that no match prints the not-found message.
     */
    @Test
    public void execute_noMatch_printsNotFound() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        new FindCommand("Aspirin").execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("No medications found matching: Aspirin"));
    }

    /**
     * Tests that multiple matching medications are all returned.
     */
    @Test
    public void execute_multipleMatches_findsAll() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol 500mg", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Paracetamol Extra", "1000mg", 50, "2027-01-15", "painkiller"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "painkiller"));
        new FindCommand("Paracetamol").execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("2 matching medication(s)"));
        assertTrue(output.contains("Paracetamol 500mg"));
        assertTrue(output.contains("Paracetamol Extra"));
    }

    /**
     * Tests that searching an empty inventory prints the not-found message.
     */
    @Test
    public void execute_emptyInventory_printsNotFound() {
        new FindCommand("anything").execute(new Inventory(), new Ui(), new CustomerList());
        assertTrue(out.toString().contains("No medications found matching: anything"));
    }

    /**
     * Tests that a suffix keyword (partial match from the middle/end) still finds the medication.
     */
    @Test
    public void execute_partialKeyword_findsMedication() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));
        new FindCommand("cillin").execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Amoxicillin"));
    }
}
