package seedu.pharmatracker.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class UpdateCommandTest {

    private Inventory inventory;
    private Ui ui;
    private CustomerList customerList;
    private Medication testMedication;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        customerList = new CustomerList();

        testMedication = new Medication("Paracetamol", "500mg", 100, "2026-12-31", "Painkiller");
        testMedication.setDosageForm("Tablet");
        testMedication.setManufacturer("PharmaCorp");
        testMedication.setDirections("Take after meals");
        testMedication.setFrequency("Twice a day");
        testMedication.setRoute("Oral");
        testMedication.setMaxDailyDose("4000mg");
        testMedication.addWarning("May cause drowsiness");

        inventory.addMedication(testMedication);
    }

    @Test
    void execute_allFieldsProvided_updatesAllFields() {
        ArrayList<String> newWarnings = new ArrayList<>(Arrays.asList("Do not take on empty stomach", "Avoid alcohol"));
        UpdateCommand command = new UpdateCommand(
                1, "Panadol Extra", "650mg", 150, "2027-01-01", "Strong Painkiller",
                "Capsule", "GSK", "Take with water", "Thrice a day", "Oral", "3000mg", newWarnings
        );

        command.execute(inventory, ui, customerList);

        Medication updatedMed = inventory.getMedication(0);
        assertEquals("Panadol Extra", updatedMed.getName());
        assertEquals("650mg", updatedMed.getDosage());
        assertEquals(150, updatedMed.getQuantity());
        assertEquals("2027-01-01", updatedMed.getExpiryDate());
        assertEquals("Strong Painkiller", updatedMed.getTag());
        assertEquals("Capsule", updatedMed.getDosageForm());
        assertEquals("GSK", updatedMed.getManufacturer());
        assertEquals("Take with water", updatedMed.getDirections());
        assertEquals("Thrice a day", updatedMed.getFrequency());
        assertEquals("Oral", updatedMed.getRoute());
        assertEquals("3000mg", updatedMed.getMaxDailyDose());

        assertEquals(2, updatedMed.getWarnings().size());
        assertTrue(updatedMed.getWarnings().contains("Avoid alcohol"));
    }

    @Test
    void execute_singleFieldProvided_updatesOnlySpecifiedField() {
        UpdateCommand command = new UpdateCommand(
                1, "Ibuprofen", null, null, null, null,
                null, null, null, null, null, null, null
        );

        command.execute(inventory, ui, customerList);

        Medication updatedMed = inventory.getMedication(0);
        assertEquals("Ibuprofen", updatedMed.getName());
        assertEquals("500mg", updatedMed.getDosage());
        assertEquals(100, updatedMed.getQuantity());
    }

    @Test
    void execute_emptyInventory_abortsExecution() {
        Inventory emptyInventory = new Inventory();
        UpdateCommand command = new UpdateCommand(
                1, "Ibuprofen", null, null, null, null,
                null, null, null, null, null, null, null
        );

        command.execute(emptyInventory, ui, customerList);
        assertEquals(0, emptyInventory.getMedicationCount());
    }

    @Test
    void execute_invalidIndexHigh_abortsExecution() {
        UpdateCommand command = new UpdateCommand(
                2, "Ibuprofen", null, null, null, null,
                null, null, null, null, null, null, null
        );

        command.execute(inventory, ui, customerList);
        assertEquals("Paracetamol", inventory.getMedication(0).getName());
    }

    @Test
    void execute_invalidIndexLow_abortsExecution() {
        UpdateCommand command = new UpdateCommand(
                0, "Ibuprofen", null, null, null, null,
                null, null, null, null, null, null, null
        );

        command.execute(inventory, ui, customerList);
        assertEquals("Paracetamol", inventory.getMedication(0).getName());
    }

    @Test
    void execute_noFieldsProvided_abortsExecution() {
        UpdateCommand command = new UpdateCommand(
                1, null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        command.execute(inventory, ui, customerList);
        assertEquals("Paracetamol", inventory.getMedication(0).getName());
    }

    @Test
    void execute_nullInventory_throwsAssertionError() {
        UpdateCommand command = new UpdateCommand(
                1, "Ibuprofen", null, null, null, null,
                null, null, null, null, null, null, null
        );

        assertThrows(AssertionError.class,
                () -> command.execute(null, ui, customerList));
    }

    @Test
    void execute_nullUi_throwsAssertionError() {
        UpdateCommand command = new UpdateCommand(
                1, "Ibuprofen", null, null, null, null,
                null, null, null, null, null, null, null
        );

        assertThrows(AssertionError.class,
                () -> command.execute(inventory, null, customerList));
    }
}
