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
    public static final int DEFAULT_MINIMUM_STOCK_THRESHOLD = 20;

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
    private int minimumStockThreshold;

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
        this.minimumStockThreshold = DEFAULT_MINIMUM_STOCK_THRESHOLD;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getMinimumStockThreshold() {
        return this.minimumStockThreshold;
    }

    public void setMinimumStockThreshold(int minimumStockThreshold) {
        if (minimumStockThreshold <= 0) {
            return;
        }
        this.minimumStockThreshold = minimumStockThreshold;
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
        if (this.expiryDate == null || this.expiryDate.trim().isEmpty()) {
            return false;
        }

        try {
            // Safe to assume yyyy-MM-dd now
            LocalDate expiryDateParsed = LocalDate.parse(this.expiryDate.trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return expiryDateParsed.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            // This would only happen if legacy data exists in the .txt file
            return true;
        }
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

        if (tag != null && !tag.isEmpty()) {
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

    /**
     * Compares this medication to another object to determine if they represent the exact same batch.
     * Two medications are considered equal if all their descriptive fields (name, dosage, expiry date,
     * manufacturer, warnings, etc.) match exactly.
     *
     * @param obj The object to compare with this medication.
     * @return true if the specified object is a Medication with identical details (ignoring quantity), false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Medication other = (Medication) obj;

        return this.name.equalsIgnoreCase(other.name) &&
                this.dosage.equalsIgnoreCase(other.dosage) &&
                this.expiryDate.equals(other.expiryDate) &&
                this.dosageForm.equalsIgnoreCase(other.dosageForm) &&
                this.manufacturer.equalsIgnoreCase(other.manufacturer) &&
                this.tag.equalsIgnoreCase(other.tag) &&
                this.directions.equalsIgnoreCase(other.directions) &&
                this.frequency.equalsIgnoreCase(other.frequency) &&
                this.route.equalsIgnoreCase(other.route) &&
                this.maxDailyDose.equalsIgnoreCase(other.maxDailyDose) &&
                this.warnings.equals(other.warnings);
    }
}
