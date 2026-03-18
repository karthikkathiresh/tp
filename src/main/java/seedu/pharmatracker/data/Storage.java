package seedu.pharmatracker.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {
    private static final String FILE_PATH = "data/pharmatracker.txt";
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    public void save(Inventory inventory) {
        assert inventory != null : "Inventory should not be null";
        logger.log(Level.INFO, "Saving inventory to {0}", FILE_PATH);
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            for (Medication med : (ArrayList<Medication>) inventory.getMedications()) {
                assert med != null : "Medication entry should not be null";
                fw.write(med.getName() + "|" + med.getDosage() + "|"
                        + med.getQuantity() + "|" + med.getExpiryDate()
                        + "|" + med.getTag() + "\n");
            }
            fw.close();
            logger.log(Level.INFO, "Inventory saved successfully with {0} entries",
                    inventory.getMedications().size());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving data: {0}", e.getMessage());
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public Inventory load() {
        logger.log(Level.INFO, "Loading inventory from {0}", FILE_PATH);
        Inventory inventory = new Inventory();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            logger.log(Level.WARNING, "No save file found at {0}, starting fresh", FILE_PATH);
            return inventory;
        }
        try {
            Scanner sc = new Scanner(file);
            int lineNumber = 0;
            while (sc.hasNextLine()) {
                lineNumber++;
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) {
                    logger.log(Level.WARNING, "Skipping corrupted line {0}: {1}",
                            new Object[]{lineNumber, line});
                    continue;
                }
                String name = parts[0];
                String dosage = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                String expiry = parts[3];
                String tag = parts[4];
                assert !name.isEmpty() : "Medication name should not be empty";
                inventory.addMedication(new Medication(name, dosage, quantity, expiry, tag));
            }
            sc.close();
            logger.log(Level.INFO, "Loaded {0} medications from file",
                    inventory.getMedications().size());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading data: {0}", e.getMessage());
            System.out.println("Error loading data: " + e.getMessage());
        }
        return inventory;
    }
}
