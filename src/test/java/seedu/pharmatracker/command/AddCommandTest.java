package seedu.pharmatracker.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class AddCommandTest {

    @Test
    public void execute_mandatoryFieldsOnly_addsToInventory() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        ArrayList<String> emptyWarnings = new ArrayList<>();

        AddCommand addCommand = new AddCommand(
                "Paracetamol", "500mg", 100, "2026-12-31", "painkiller", // Mandatory
                "", "", "", "", "", "", emptyWarnings                    // Optional
        );
        addCommand.execute(inventory, ui);
        assertEquals(1, inventory.getMedications().size());

        Medication addedMed = inventory.getMedication(0);
        assertEquals("Paracetamol", addedMed.getName());
        assertEquals("500mg", addedMed.getDosage());
        assertEquals(100, addedMed.getQuantity());
        assertEquals("2026-12-31", addedMed.getExpiryDate());
        assertEquals("painkiller", addedMed.getTag());

        assertEquals("", addedMed.getDosageForm());
        assertEquals("", addedMed.getManufacturer());
        assertTrue(addedMed.getWarnings().isEmpty());
    }

    @Test
    public void execute_allFieldsPresent_addsSuccessfully() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        ArrayList<String> warnings = new ArrayList<>();
        warnings.add("May cause drowsiness");
        warnings.add("Do not take with alcohol");

        AddCommand addCommand = new AddCommand(
                "Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic",
                "Capsule", "Pfizer", "Take after meals",
                "Twice daily", "Oral", "1000mg", warnings
        );

        addCommand.execute(inventory, ui);

        assertEquals(1, inventory.getMedications().size());

        Medication addedMed = inventory.getMedication(0);
        assertEquals("Amoxicillin", addedMed.getName());

        assertEquals("Capsule", addedMed.getDosageForm());
        assertEquals("Pfizer", addedMed.getManufacturer());
        assertEquals("Take after meals", addedMed.getDirections());
        assertEquals("Twice daily", addedMed.getFrequency());
        assertEquals("Oral", addedMed.getRoute());
        assertEquals("1000mg", addedMed.getMaxDailyDose());

        assertEquals(2, addedMed.getWarnings().size());
        assertTrue(addedMed.getWarnings().contains("May cause drowsiness"));
        assertTrue(addedMed.getWarnings().contains("Do not take with alcohol"));
    }

    @Test
    public void execute_multipleAddCommands_appendsToInventory() {
        Inventory inventory = getInventory();
        assertEquals(2, inventory.getMedications().size());

        Medication secondMed = inventory.getMedication(1);
        assertEquals("MedB", secondMed.getName());
        assertEquals("Tablet", secondMed.getDosageForm());
    }

    private static Inventory getInventory() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        ArrayList<String> emptyWarnings = new ArrayList<>();

        AddCommand command1 = new AddCommand(
                "MedA", "10mg", 10, "2026-01-01", "tag1",
                "", "", "", "", "", "", emptyWarnings
        );
        AddCommand command2 = new AddCommand(
                "MedB", "20mg", 20, "2026-02-01", "tag2",
                "Tablet", "Moderna", "", "", "", "", emptyWarnings
        );

        command1.execute(inventory, ui);
        command2.execute(inventory, ui);
        return inventory;
    }
}
