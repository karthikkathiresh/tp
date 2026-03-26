package seedu.pharmatracker.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

/**
 * Test cases for the SortCommand class.
 * Tests the sorting functionality by expiry date and handling of edge cases.
 */
public class SortCommandTest {

    private SortCommand sortCommand;
    private Inventory inventory;
    private Ui ui;
    private CustomerList customerList;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    /**
     * Sets up test fixtures before each test method.
     * Initializes the SortCommand, inventory, and captures system output.
     */
    @BeforeEach
    public void setUp() {
        sortCommand = new SortCommand();
        inventory = new Inventory();
        ui = new Ui();
        customerList = new CustomerList();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Test SortCommand with an empty inventory.
     * Expected: Displays message indicating inventory is empty.
     */
    @Test
    public void execute_emptyInventory_displaysEmptyMessage() {
        sortCommand.execute(inventory, ui, customerList);
        String output = outputStream.toString();
        assertTrue(output.contains("Inventory is empty."), "Output should contain empty inventory message");
    }

    /**
     * Test SortCommand with a single medication.
     * Expected: Displays the single medication with index 1.
     */
    @Test
    public void execute_singleMedication_displaysSingleItem() {
        Medication medication = new Medication("Aspirin", "500mg", 10, "2026-12-31", "painkiller");
        inventory.addMedication(medication);

        sortCommand.execute(inventory, ui, customerList);
        String output = outputStream.toString();

        assertTrue(output.contains("Medications sorted by expiry date:"), "Output should contain sort header");
        assertTrue(output.contains("1. "), "Output should contain medication at index 1");
    }

    /**
     * Test SortCommand with multiple medications.
     * Expected: Medications are sorted in ascending order by expiry date.
     */
    @Test
    public void execute_multipleMedications_sortsByExpiryDateAscending() {
        // Add medications with different expiry dates in random order
        Medication medication1 = new Medication("Aspirin", "500mg", 10, "2026-03-20", "painkiller");
        Medication medication2 = new Medication("Paracetamol", "500mg", 20, "2026-01-15", "painkiller");
        Medication medication3 = new Medication("Ibuprofen", "200mg", 15, "2026-06-10", "painkiller");

        inventory.addMedication(medication1);
        inventory.addMedication(medication2);
        inventory.addMedication(medication3);

        ArrayList<Medication> medicationsBeforeSort = inventory.getMedications();
        assertEquals(3, medicationsBeforeSort.size(), "Inventory should contain 3 medications");

        sortCommand.execute(inventory, ui, customerList);

        ArrayList<Medication> medicationsAfterSort = inventory.getMedications();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(medicationsAfterSort.get(0).getExpiryDate(), formatter);
        LocalDate date2 = LocalDate.parse(medicationsAfterSort.get(1).getExpiryDate(), formatter);
        LocalDate date3 = LocalDate.parse(medicationsAfterSort.get(2).getExpiryDate(), formatter);

        assertTrue(date1.compareTo(date2) <= 0, "First medication should expire before or same as second");
        assertTrue(date2.compareTo(date3) <= 0, "Second medication should expire before or same as third");
    }

    /**
     * Test SortCommand with medications having invalid expiry dates.
     * Expected: Medications with invalid dates are treated as having maximum expiry date
     * and appear at the end of the sorted list.
     */
    @Test
    public void execute_invalidExpiryDates_treatsAsMaximumDate() {
        Medication validMedication = new Medication("ValidMed", "100mg", 10, "2026-03-20", "general");
        Medication invalidMedication = new Medication("InvalidMed", "50mg", 5, "invalid-date", "general");

        inventory.addMedication(invalidMedication);
        inventory.addMedication(validMedication);

        sortCommand.execute(inventory, ui, customerList);

        ArrayList<Medication> medicationsAfterSort = inventory.getMedications();
        assertEquals(2, medicationsAfterSort.size(), "Inventory should still contain 2 medications");

        // The valid medication should be first (earlier date)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate firstDate = LocalDate.parse(
                    medicationsAfterSort.get(0).getExpiryDate(), formatter);
            assertEquals(LocalDate.parse("2026-03-20", formatter), firstDate,
                    "First medication should be the valid one");
        } catch (Exception e) {
            fail("First medication should have valid expiry date");
        }
    }

    /**
     * Test SortCommand displays sorted output correctly.
     * Expected: Output contains sort header and displays medications with indices.
     */
    @Test
    public void execute_multipleMedications_displaysCorrectFormat() {
        Medication medication1 = new Medication("Aspirin", "500mg", 10, "2026-03-20", "painkiller");
        Medication medication2 = new Medication("Paracetamol", "500mg", 20, "2026-01-15", "painkiller");

        inventory.addMedication(medication1);
        inventory.addMedication(medication2);

        sortCommand.execute(inventory, ui, customerList);
        String output = outputStream.toString();

        assertTrue(output.contains("Medications sorted by expiry date:"), "Output should contain header");
        assertTrue(output.contains("1. "), "Output should display first medication");
        assertTrue(output.contains("2. "), "Output should display second medication");
    }
}
