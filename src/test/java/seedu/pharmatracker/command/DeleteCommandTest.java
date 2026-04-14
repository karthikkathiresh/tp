package seedu.pharmatracker.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

/**
 * Tests the functionality of the {@link DeleteCommand} class.
 * Ensures that medications are correctly removed from the inventory
 * based on the 1-based index provided by the user.
 */
public class DeleteCommandTest {

    /**
     * Tests if executing a {@code DeleteCommand} with the first valid index (1)
     * successfully removes the first {@link Medication} from the {@link Inventory}.
     * Verifies that the inventory size decreases by one and that the remaining
     * medication is the one originally at the second position.
     */
    @Test
    public void execute_validFirstIndex_deletesSuccessfully() throws PharmaTrackerException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));

        assertEquals(2, inventory.getMedicationCount());

        DeleteCommand deleteCommand = new DeleteCommand("1");
        deleteCommand.execute(inventory, ui, customerList);

        assertEquals(1, inventory.getMedicationCount());
        assertEquals(1, inventory.getMedications().size());

        Medication remainingMed = inventory.getMedication(0);
        assertEquals("Amoxicillin", remainingMed.getName());
    }

    /**
     * Tests if executing a {@code DeleteCommand} with the last valid index
     * successfully removes the last {@link Medication} from the {@Link Inventory}.
     * Verifies that the inventory size decreases by one and that the first
     * medication remains completely unaffected.
     */
    @Test
    public void execute_validLastIndex_deletesSuccessfully() throws PharmaTrackerException {

        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));

        DeleteCommand deleteCommand = new DeleteCommand("2");
        deleteCommand.execute(inventory, ui, customerList);

        assertEquals(1, inventory.getMedicationCount());

        Medication remainingMed = inventory.getMedication(0);
        assertEquals("Paracetamol", remainingMed.getName());
    }

    @Test
    public void execute_indexOutOfBoundsLow_throwsPharmaTrackerException() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        DeleteCommand deleteCommand = new DeleteCommand("0");

        PharmaTrackerException thrown = assertThrows(PharmaTrackerException.class,
                () -> deleteCommand.execute(inventory, ui, customerList));

        assertEquals("Invalid index. Please enter an integer between 1 and 1.", thrown.getMessage());
    }

    @Test
    public void execute_indexOutOfBoundsHigh_throwsPharmaTrackerException() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        DeleteCommand deleteCommand = new DeleteCommand("5");

        PharmaTrackerException thrown = assertThrows(PharmaTrackerException.class,
                () -> deleteCommand.execute(inventory, ui, customerList));

        assertEquals("Invalid index. Please enter an integer between 1 and 1.", thrown.getMessage());
    }

    @Test
    public void execute_nonNumericIndex_throwsPharmaTrackerException() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        DeleteCommand deleteCommand = new DeleteCommand("abc");

        PharmaTrackerException thrown = assertThrows(PharmaTrackerException.class,
                () -> deleteCommand.execute(inventory, ui, customerList));

        assertEquals("Invalid format. Please provide a valid integer for the index.", thrown.getMessage());
    }

    @Test
    public void execute_overflowIndex_throwsPharmaTrackerException() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        // 2147483648 is Max Integer + 1, triggering NumberFormatException but passing the regex digit check
        DeleteCommand deleteCommand = new DeleteCommand("2147483648");

        PharmaTrackerException thrown = assertThrows(PharmaTrackerException.class,
                () -> deleteCommand.execute(inventory, ui, customerList));

        assertEquals("Invalid index. Please enter an integer between 1 and 1.", thrown.getMessage());
    }

    @Test
    public void execute_nullInventory_throwsAssertionError() {
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        DeleteCommand deleteCommand = new DeleteCommand("1");

        assertThrows(AssertionError.class,
                () -> deleteCommand.execute(null, ui, customerList));
    }

    @Test
    public void execute_nullUi_throwsAssertionError() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        DeleteCommand deleteCommand = new DeleteCommand("1");

        assertThrows(AssertionError.class,
                () -> deleteCommand.execute(inventory, null, customerList));
    }
}
