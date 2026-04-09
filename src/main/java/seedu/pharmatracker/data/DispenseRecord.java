package seedu.pharmatracker.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single dispense event recorded in the daily dispense log.
 */
public class DispenseRecord {

    private static final DateTimeFormatter DISPLAY_TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter STORE_TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final LocalDate date;
    private final LocalTime time;
    private final String medicationName;
    private final String dosage;
    private final int quantity;
    private final String customerName;

    /**
     * Constructs a DispenseRecord.
     *
     * @param date           Date of the dispense event.
     * @param time           Time of the dispense event.
     * @param medicationName Name of the medication dispensed.
     * @param dosage         Dosage of the medication.
     * @param quantity       Number of units dispensed.
     * @param customerName   Name of the linked customer, or empty string if none.
     */
    public DispenseRecord(LocalDate date, LocalTime time,
                          String medicationName, String dosage,
                          int quantity, String customerName) {
        this.date = date;
        this.time = time;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.quantity = quantity;
        this.customerName = (customerName != null) ? customerName : "";
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCustomerName() {
        return customerName;
    }

    /**
     * Returns a pipe-delimited string suitable for file storage.
     * Format: date|time|medicationName|dosage|quantity|customerName
     */
    public String toStorageString() {
        return date.toString()
                + "|" + time.format(STORE_TIME_FMT)
                + "|" + medicationName
                + "|" + dosage
                + "|" + quantity
                + "|" + customerName;
    }

    /**
     * Parses a storage string back into a DispenseRecord.
     *
     * @param line A pipe-delimited line from the dispense log file.
     * @return The reconstructed DispenseRecord, or null if the line is malformed.
     */
    public static DispenseRecord fromStorageString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(parts[0]);
            LocalTime time = LocalTime.parse(parts[1], STORE_TIME_FMT);
            String medName = parts[2];
            String dosage = parts[3];
            int qty = Integer.parseInt(parts[4]);
            String customer = parts[5];
            return new DispenseRecord(date, time, medName, dosage, qty, customer);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a human-readable summary line for display.
     */
    @Override
    public String toString() {
        String line = time.format(DISPLAY_TIME_FMT)
                + " | " + medicationName
                + " | Dosage: " + dosage
                + " | Qty: " + quantity;
        if (!customerName.isEmpty()) {
            line += " | Patient: " + customerName;
        }
        return line;
    }
}
