package seedu.pharmatracker.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Represents a medication stored in the inventory.
 * Contains both compulsory attributes (name, dosage, quantity, expiry date)
 * and various optional attributes for detailed tracking.
 */
public class Medication {
    // Compulsory attributes for a Medication.
    private String name;
    private String dosage;
    private int quantity;
    private String expiryDate;

    // Optional attributes for a Medication.
    private String tag;
    private String dosageForm;
    private String manufacturer;
    private String directions;
    private String frequency;
    private String route;
    private String maxDailyDose;
    private ArrayList<String> warnings;

    /**
     * Constructs a {@code Medication} with the specified mandatory details.
     * All optional string attributes are initialized to empty strings, and the
     * warnings list is initialized as an empty ArrayList.
     *
     * @param name       The name of the medication.
     * @param dosage     The strength or dosage of the medication.
     * @param quantity   The number of units in stock.
     * @param expiryDate The expiration date in YYYY-MM-DD format.
     * @param tag        The category or tag associated with the medication.
     */
    public Medication(String name, String dosage, int quantity, String expiryDate, String tag) {
        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.tag = tag;

        this.dosageForm = "";
        this.manufacturer = "";
        this.directions = "";
        this.frequency = "";
        this.route = "";
        this.maxDailyDose = "";
        this.warnings = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getDosage() {
        return this.dosage;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public String getTag() {
        return this.tag;
    }

    public String getDosageForm() {
        return this.dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDirections() {
        return this.directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRoute() {
        return this.route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getMaxDailyDose() {
        return this.maxDailyDose;
    }

    public void setMaxDailyDose(String maxDailyDose) {
        this.maxDailyDose = maxDailyDose;
    }

    public ArrayList<String> getWarnings() {
        return this.warnings;
    }

    /**
     * Adds a warning string to the medication's list of warnings.
     *
     * @param warning The warning or precaution to be added.
     */
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    /**
     * Checks if the medication has expired as of today.
     * 
     * @return true if the expiry date is before today, false otherwise
     */
    public boolean isExpired() {
        String expiry = this.expiryDate;
        if (expiry == null) {
            return false;
        }
        expiry = expiry.trim();
        LocalDate expiryDateParsed = null;
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            expiryDateParsed = LocalDate.parse(expiry, formatter1);
        } catch (DateTimeParseException e1) {
            try {
                expiryDateParsed = LocalDate.parse(expiry, formatter2);
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
        LocalDate today = LocalDate.now();
        return expiryDateParsed.isBefore(today);
    }

    /**
     * Returns a formatted string representation of the medication.
     * Includes all populated fields and appends an "[EXPIRED]" tag if the medication has expired.
     *
     * @return A string containing the medication's details
     */
    @Override
    public String toString() {
        String s = "Name: " + name +
                   " | Dosage: " + dosage +
                   " | Qty: " + quantity +
                   " | Exp: " + expiryDate;

        // Add expired tag if medication is expired
        if (isExpired()) {
            s += " | [EXPIRED]";
        }

        if (!tag.isEmpty()) {
            s += " | Tag: " + tag;
        }

        if (!dosageForm.isEmpty()) {
            s += " | Dosage Form: " + dosageForm;
        }

        if (!manufacturer.isEmpty()) {
            s += " | Manufacturer: " + manufacturer;
        }

        if (!directions.isEmpty()) {
            s += " | Directions: " + directions;
        }

        if (!frequency.isEmpty()) {
            s += " | Frequency: " + frequency;
        }

        if (!route.isEmpty()) {
            s += " | Route: " + route;
        }

        if (!maxDailyDose.isEmpty()) {
            s += " | Maximum Daily Dosage: " + maxDailyDose;
        }

        return s;
    }
}
