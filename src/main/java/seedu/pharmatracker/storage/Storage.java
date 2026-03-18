package seedu.pharmatracker.storage;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {
    private static final String FILE_PATH = "data/pharmatracker.txt";
    private static final String DELIMITER = "|";
    private static final String WARNINGS_SEPARATOR = ";";
    private static final int MIN_FIELDS = 4;
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
                String warnings = String.join(WARNINGS_SEPARATOR, med.getWarnings());
                fw.write(med.getName() + DELIMITER
                        + med.getDosage() + DELIMITER
                        + med.getQuantity() + DELIMITER
                        + med.getExpiryDate() + DELIMITER
                        + med.getTag() + DELIMITER
                        + med.getDosageForm() + DELIMITER
                        + med.getManufacturer() + DELIMITER
                        + med.getDirections() + DELIMITER
                        + med.getFrequency() + DELIMITER
                        + med.getRoute() + DELIMITER
                        + med.getMaxDailyDose() + DELIMITER
                        + warnings + "\n");
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
                if (parts.length < MIN_FIELDS) {
                    logger.log(Level.WARNING, "Skipping corrupted line {0}: {1}",
                            new Object[]{lineNumber, line});
                    continue;
                }

                // Compulsory fields
                String name = parts[0];
                String dosage = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                String expiry = parts[3];

                assert !name.isEmpty() : "Medication name should not be empty";

                // Optional fields — default to empty string if absent
                String tag          = parts.length > 4  ? parts[4]  : "";
                String dosageForm   = parts.length > 5  ? parts[5]  : "";
                String manufacturer = parts.length > 6  ? parts[6]  : "";
                String directions   = parts.length > 7  ? parts[7]  : "";
                String frequency    = parts.length > 8  ? parts[8]  : "";
                String route        = parts.length > 9  ? parts[9]  : "";
                String maxDailyDose = parts.length > 10 ? parts[10] : "";
                String warningsRaw  = parts.length > 11 ? parts[11] : "";

                Medication med = new Medication(name, dosage, quantity, expiry, tag);
                med.setDosageForm(dosageForm);
                med.setManufacturer(manufacturer);
                med.setDirections(directions);
                med.setFrequency(frequency);
                med.setRoute(route);
                med.setMaxDailyDose(maxDailyDose);
                if (!warningsRaw.isEmpty()) {
                    Arrays.stream(warningsRaw.split(WARNINGS_SEPARATOR))
                            .forEach(med::addWarning);
                }
                inventory.addMedication(med);
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
