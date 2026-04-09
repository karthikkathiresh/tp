package seedu.pharmatracker.storage;

import seedu.pharmatracker.alert.RestockAlert;
import seedu.pharmatracker.data.DispenseLog;
import seedu.pharmatracker.data.DispenseRecord;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {
    private static final String FILE_PATH = "data/pharmatracker.txt";
    private static final String DELIMITER = "|";
    private static final String WARNINGS_SEPARATOR = ";";
    private static final int MIN_FIELDS = 4;
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private static final String CUSTOMER_FILE_PATH = "data/customers.txt";
    private static final String HISTORY_SEPARATOR = ";";
    private static final String USERS_FILE_PATH = "data/users.txt";
    private static final String SESSION_FILE_PATH = "data/session.txt";
    private static final String ALERTS_FILE_PATH = "data/alerts.txt";
    private static final String DISPENSE_LOG_FILE_PATH = "data/dispense_log.txt";

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
                        + warnings + DELIMITER
                        + med.getMinimumStockThreshold() + "\n");
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
                int minimumStockThreshold = parts.length > 12
                    ? Integer.parseInt(parts[12])
                    : Medication.DEFAULT_MINIMUM_STOCK_THRESHOLD;

                Medication med = new Medication(name, dosage, quantity, expiry, tag);
                med.setDosageForm(dosageForm);
                med.setManufacturer(manufacturer);
                med.setDirections(directions);
                med.setFrequency(frequency);
                med.setRoute(route);
                med.setMaxDailyDose(maxDailyDose);
                med.setMinimumStockThreshold(minimumStockThreshold);
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

    /**
     * Saves the current list of customers and their dispensing histories to a text file.
     * History entries are joined using a semicolon (;) to handle multiple medications.
     *
     * @param customerList The manager containing the customers to save.
     */
    public void saveCustomers(seedu.pharmatracker.customer.CustomerList customerList) {
        assert customerList != null : "CustomerList should not be null";
        try {
            java.io.File file = new java.io.File(CUSTOMER_FILE_PATH);
            file.getParentFile().mkdirs();
            java.io.FileWriter fw = new java.io.FileWriter(file);

            for (int i = 0; i < customerList.size(); i++) {
                seedu.pharmatracker.customer.Customer c = customerList.getCustomer(i);
                // Join all dispensing history strings into one part using a semicolon
                String joinedHistory = String.join(HISTORY_SEPARATOR, c.getDispensingHistory());

                fw.write(c.getCustomerId() + " | "
                        + c.getName() + " | "
                        + c.getPhone() + " | "
                        + c.getAddress() + " | "
                        + joinedHistory + "\n");
            }
            fw.close();
        } catch (java.io.IOException e) {
            System.out.println("Error saving customer data: " + e.getMessage());
        }
    }

    /**
     * Loads customer data from the local text file.
     * Correctly reconstructs the dispensing history for customers with multiple records.
     *
     * @return A CustomerList populated with data from the file.
     */
    public seedu.pharmatracker.customer.CustomerList loadCustomers() {
        seedu.pharmatracker.customer.CustomerList list = new seedu.pharmatracker.customer.CustomerList();
        java.io.File file = new java.io.File(CUSTOMER_FILE_PATH);
        if (!file.exists()) {
            return list;
        }
        try {
            java.util.Scanner sc = new java.util.Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(" \\| ");
                if (parts.length >= 4) {
                    seedu.pharmatracker.customer.Customer c =
                            new seedu.pharmatracker.customer.Customer(parts[0], parts[1], parts[2], parts[3]);

                    // If there is history data (the 5th part), split and add it
                    if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                        String[] historyEntries = parts[4].split(HISTORY_SEPARATOR);
                        for (String entry : historyEntries) {
                            c.addDispensingHistory(entry);
                        }
                    }
                    list.addCustomer(c);
                }
            }
            sc.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Customer file not found.");
        }
        return list;
    }

    /**
     * Saves all registered users and password hashes.
     *
     * @param users Map of username to password hash.
     */
    public void saveUsers(Map<String, String> users) {
        if (users == null) {
            return;
        }
        try {
            File file = new File(USERS_FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            for (Map.Entry<String, String> entry : users.entrySet()) {
                fw.write(entry.getKey() + DELIMITER + entry.getValue() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    /**
     * Loads registered users and password hashes.
     *
     * @return Map of username to password hash.
     */
    public Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        File file = new File(USERS_FILE_PATH);
        if (!file.exists()) {
            return users;
        }
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    users.put(parts[0].trim(), parts[1].trim());
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
        return users;
    }

    /**
     * Persists the currently logged-in username for session continuity.
     *
     * @param username Username to persist, or null to clear session.
     */
    public void saveSession(String username) {
        try {
            File file = new File(SESSION_FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            if (username != null && !username.trim().isEmpty()) {
                fw.write(username.trim());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving session data: " + e.getMessage());
        }
    }

    /**
     * Loads the persisted session username.
     *
     * @return Session username, or null if no active session exists.
     */
    public String loadSession() {
        File file = new File(SESSION_FILE_PATH);
        if (!file.exists()) {
            return null;
        }

        try {
            Scanner sc = new Scanner(file);
            if (!sc.hasNextLine()) {
                sc.close();
                return null;
            }
            String username = sc.nextLine().trim();
            sc.close();
            return username.isEmpty() ? null : username;
        } catch (Exception e) {
            System.out.println("Error loading session data: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves full alert history to storage.
     *
     * @param alertHistory Complete alert history list.
     */
    public void saveAlertHistory(List<RestockAlert> alertHistory) {
        if (alertHistory == null) {
            return;
        }

        try {
            File file = new File(ALERTS_FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            for (RestockAlert alert : alertHistory) {
                fw.write(alert.getId() + DELIMITER
                        + alert.getMedicationKey() + DELIMITER
                        + alert.getMedicationName() + DELIMITER
                        + alert.getCurrentStock() + DELIMITER
                        + alert.getThreshold() + DELIMITER
                        + alert.getCreatedAtString() + DELIMITER
                        + alert.isAcknowledged() + DELIMITER
                        + alert.getAcknowledgedAtString() + DELIMITER
                        + alert.getAcknowledgmentNote() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving alert history: " + e.getMessage());
        }
    }

    /**
     * Loads alert history from storage.
     *
     * @return Persisted alert history list.
     */
    public ArrayList<RestockAlert> loadAlertHistory() {
        ArrayList<RestockAlert> alertHistory = new ArrayList<>();
        File file = new File(ALERTS_FILE_PATH);
        if (!file.exists()) {
            return alertHistory;
        }

        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
     * Saves all dispense records to the dispense log file.
     * Each record is written as one pipe-delimited line.
     *
     * @param dispenseLog The dispense log to persist.
     */
    public void saveDispenseLog(DispenseLog dispenseLog) {
        assert dispenseLog != null : "DispenseLog should not be null";
        try {
            File file = new File(DISPENSE_LOG_FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            for (DispenseRecord record : dispenseLog.getAllRecords()) {
                fw.write(record.toStorageString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving dispense log: {0}", e.getMessage());
            System.out.println("Error saving dispense log: " + e.getMessage());
        }
    }

    /**
     * Loads the dispense log from the dispense log file.
     * Returns an empty {@link DispenseLog} if the file does not exist or is unreadable.
     *
     * @return A {@link DispenseLog} populated from the saved file.
     */
    public DispenseLog loadDispenseLog() {
        DispenseLog log = new DispenseLog();
        File file = new File(DISPENSE_LOG_FILE_PATH);
        if (!file.exists()) {
            return log;
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
                if (parts.length < 9) {
                    continue;
                }

                LocalDateTime createdAt = RestockAlert.parseDateTime(parts[5]);
                boolean acknowledged = Boolean.parseBoolean(parts[6]);
                LocalDateTime acknowledgedAt = RestockAlert.parseDateTime(parts[7]);
                RestockAlert alert = new RestockAlert(
                        parts[0],
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]),
                        createdAt,
                        acknowledged,
                        acknowledgedAt,
                        parts[8]
                );
                alertHistory.add(alert);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading alert history: " + e.getMessage());
        }

        return alertHistory;
                DispenseRecord record = DispenseRecord.fromStorageString(line);
                if (record == null) {
                    logger.log(Level.WARNING, "Skipping malformed dispense log line {0}: {1}",
                            new Object[]{lineNumber, line});
                    continue;
                }
                log.addRecord(record);
            }
            sc.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading dispense log: {0}", e.getMessage());
            System.out.println("Error loading dispense log: " + e.getMessage());
        }
        return log;
    }
}
