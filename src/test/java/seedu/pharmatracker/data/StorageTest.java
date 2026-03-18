package seedu.pharmatracker.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StorageTest {
    private Storage storage;
    private static final String TEST_FILE_PATH = "data/pharmatracker.txt";

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
    void save_andLoad_singleMedication_success() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 50, "2026-12-31", "painkiller"));

        storage.save(inventory);

        Inventory loaded = storage.load();
        assertEquals(1, loaded.getMedications().size());
        Medication med = loaded.getMedication(0);
        assertEquals("Paracetamol", med.getName());
        assertEquals("500mg", med.getDosage());
        assertEquals(50, med.getQuantity());
        assertEquals("2026-12-31", med.getExpiryDate());
        assertEquals("painkiller", med.getTag());
    }

    @Test
    void save_andLoad_multipleMedications_success() {
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
    void save_emptyInventory_loadsEmptyInventory() {
        Inventory inventory = new Inventory();
        storage.save(inventory);

        Inventory loaded = storage.load();
        assertEquals(0, loaded.getMedications().size());
    }

    @Test
    void load_corruptedLine_skipsInvalidEntry() {
        try {
            new File("data").mkdirs();
            java.io.FileWriter fw = new java.io.FileWriter(TEST_FILE_PATH);
            fw.write("Paracetamol|500mg|50|2026-12-31|painkiller\n");
            fw.write("CORRUPTED_LINE_NO_PIPES\n");
            fw.write("Ibuprofen|200mg|30|2027-06-15|anti-inflammatory\n");
            fw.close();
        } catch (java.io.IOException e) {
            System.out.println("Error writing test file: " + e.getMessage());
        }

        Inventory loaded = storage.load();
        assertEquals(2, loaded.getMedications().size());
    }
}
