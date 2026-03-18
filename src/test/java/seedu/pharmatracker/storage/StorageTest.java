package seedu.pharmatracker.storage;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StorageTest {
    private static final String TEST_FILE_PATH = "data/pharmatracker.txt";
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE_PATH).delete();
    }

    @Test
    void load_noFileExists_returnsEmptyInventory() {
        new File(TEST_FILE_PATH).delete();
        Inventory inventory = storage.load();
        assertNotNull(inventory);
        assertEquals(0, inventory.getMedications().size());
    }

    @Test
    void save_compulsoryFieldsOnly_loadsCorrectly() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 50, "2026-12-31", ""));

        storage.save(inventory);

        Inventory loaded = storage.load();
        assertEquals(1, loaded.getMedications().size());
        Medication med = loaded.getMedication(0);
        assertEquals("Paracetamol", med.getName());
        assertEquals("500mg", med.getDosage());
        assertEquals(50, med.getQuantity());
        assertEquals("2026-12-31", med.getExpiryDate());
    }

    @Test
    void save_allFields_loadsCorrectly() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Ibuprofen", "200mg", 30, "2027-06-15", "anti-inflammatory");
        med.setDosageForm("tablet");
        med.setManufacturer("Pfizer");
        med.setDirections("Take after food");
        med.setFrequency("Twice daily");
        med.setRoute("Oral");
        med.setMaxDailyDose("400mg");
        med.addWarning("Do not exceed dose");
        med.addWarning("Avoid alcohol");
        inventory.addMedication(med);

        storage.save(inventory);

        Inventory loaded = storage.load();
        Medication loadedMed = loaded.getMedication(0);
        assertEquals("Ibuprofen", loadedMed.getName());
        assertEquals("tablet", loadedMed.getDosageForm());
        assertEquals("Pfizer", loadedMed.getManufacturer());
        assertEquals("Take after food", loadedMed.getDirections());
        assertEquals("Twice daily", loadedMed.getFrequency());
        assertEquals("Oral", loadedMed.getRoute());
        assertEquals("400mg", loadedMed.getMaxDailyDose());
        assertEquals(2, loadedMed.getWarnings().size());
        assertEquals("Do not exceed dose", loadedMed.getWarnings().get(0));
        assertEquals("Avoid alcohol", loadedMed.getWarnings().get(1));
    }

    @Test
    void save_multipleMedications_success() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 50, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2027-06-15", "anti-inflammatory"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 20, "2025-03-01", "antibiotic"));

        storage.save(inventory);

        Inventory loaded = storage.load();
        assertEquals(3, loaded.getMedications().size());
        assertEquals("Ibuprofen", loaded.getMedication(1).getName());
        assertEquals("Amoxicillin", loaded.getMedication(2).getName());
    }

    @Test
    void save_emptyInventory_loadsEmpty() {
        Inventory inventory = new Inventory();
        storage.save(inventory);

        Inventory loaded = storage.load();
        assertEquals(0, loaded.getMedications().size());
    }

    @Test
    void load_corruptedLine_skipsInvalidEntry() {
        try {
            new File("data").mkdirs();
            FileWriter fw = new FileWriter(TEST_FILE_PATH);
            fw.write("Paracetamol|500mg|50|2026-12-31\n");
            fw.write("BAD\n");
            fw.write("Ibuprofen|200mg|30|2027-06-15\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error writing test file: " + e.getMessage());
        }

        Inventory loaded = storage.load();
        assertEquals(2, loaded.getMedications().size());
    }

    @Test
    void load_missingOptionalFields_defaultsToEmpty() {
        try {
            new File("data").mkdirs();
            FileWriter fw = new FileWriter(TEST_FILE_PATH);
            fw.write("Paracetamol|500mg|50|2026-12-31\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error writing test file: " + e.getMessage());
        }

        Inventory loaded = storage.load();
        Medication med = loaded.getMedication(0);
        assertEquals("Paracetamol", med.getName());
        assertEquals("", med.getTag());
        assertEquals("", med.getDosageForm());
        assertEquals("", med.getManufacturer());
        assertEquals(0, med.getWarnings().size());
    }
}
