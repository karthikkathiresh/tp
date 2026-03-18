package seedu.pharmatracker.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

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
    public void execute_validFirstIndex_deletesCorrectMedication() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));

        assertEquals(2, inventory.getMedicationCount());

        DeleteCommand deleteCommand = new DeleteCommand("1");
        deleteCommand.execute(inventory, ui);

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
    public void execute_validLastIndex_deletesCorrectMedication() {

        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));

        DeleteCommand deleteCommand = new DeleteCommand("2");
        deleteCommand.execute(inventory, ui);

        assertEquals(1, inventory.getMedicationCount());

        Medication remainingMed = inventory.getMedication(0);
        assertEquals("Paracetamol", remainingMed.getName());
    }
}
