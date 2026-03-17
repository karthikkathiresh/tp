package seedu.pharmatracker.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class DeleteCommandTest {

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
