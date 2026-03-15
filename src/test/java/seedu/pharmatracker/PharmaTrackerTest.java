package seedu.pharmatracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.data.Inventory;

public class PharmaTrackerTest {

    @Test
    public void medication_getName_returnsCorrectName() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals("Panadol", med.getName());
    }

    @Test
    public void medication_getDosage_returnsCorrectDosage() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals("500mg", med.getDosage());
    }

    @Test
    public void medication_getQuantity_returnsCorrectQuantity() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals(100, med.getQuantity());
    }

    @Test
    public void medication_setQuantity_updatesQuantity() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        med.setQuantity(50);
        assertEquals(50, med.getQuantity());
    }

    @Test
    public void medication_getExpiryDate_returnsCorrectDate() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals("2026-12-31", med.getExpiryDate());
    }

    @Test
    public void inventory_initiallyEmpty() {
        Inventory inventory = new Inventory();
        assertTrue(inventory.getMedications().isEmpty());
    }

    @Test
    public void inventory_addMedication_increasesSize() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller");
        inventory.getMedications().add(med);
        assertEquals(1, inventory.getMedications().size());
    }

    @Test
    public void inventory_getMedication_returnsCorrectMedication() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller");
        inventory.getMedications().add(med);
        assertEquals("Aspirin", inventory.getMedication(0).getName());
    }
}
