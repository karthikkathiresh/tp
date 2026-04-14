package seedu.pharmatracker.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import seedu.pharmatracker.exceptions.PharmaTrackerException;

/**
 * Provides utility methods and constants specifically for extracting and parsing
 * medication attributes from user input strings.
 * This class defines the flag boundaries associated with medications (e.g., name, dosage, quantity)
 * and handles the validation and formatting of these specific fields.
 */
public class MedicationParserUtil {
    public static final String FLAG_NAME = "/n";
    public static final String FLAG_DOSAGE = "/d ";
    public static final String FLAG_QUANTITY = "/q";
    public static final String FLAG_EXPIRY_DATE = "/e";
    public static final String FLAG_TAG = "/t";
    public static final String FLAG_DOSAGE_FORM = "/df";
    public static final String FLAG_MANUFACTURER = "/mfr";
    public static final String FLAG_DIRECTION = "/dir";
    public static final String FLAG_FREQUENCY = "/freq";
    public static final String FLAG_ROUTE = "/route";
    public static final String FLAG_MAX_DOSAGE = "/max";
    public static final String FLAG_WARNINGS = "/warn";
    public static final String[] ALL_FLAGS = {
        FLAG_NAME, FLAG_DOSAGE, FLAG_QUANTITY, FLAG_EXPIRY_DATE, FLAG_TAG,
        FLAG_DOSAGE_FORM, FLAG_MANUFACTURER, FLAG_DIRECTION, FLAG_FREQUENCY,
        FLAG_ROUTE, FLAG_MAX_DOSAGE, FLAG_WARNINGS
    };

    /**
     * Extracts the mandatory medication name from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted medication name.
     * @throws PharmaTrackerException If the format is invalid, missing flags, or if the name is empty.
     */
    public static String extractName(String description) throws PharmaTrackerException {
        int nameIndex = description.indexOf(FLAG_NAME);
        int dosageIndex = description.indexOf(FLAG_DOSAGE);

        if (nameIndex == -1 || dosageIndex == -1 || nameIndex >= dosageIndex) {
            throw new PharmaTrackerException("Invalid format! Please ensure you include '/n' followed by '/d'.");
        }

        String name = description.substring(nameIndex + 2, dosageIndex).trim();
        if (name.isEmpty()) {
            throw new PharmaTrackerException("Medication name cannot be empty!");
        }

        return name;
    }

    /**
     * Extracts the mandatory medication storage from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted medication dosage.
     * @throws PharmaTrackerException If the format is invalid, missing flags, or if the dosage is empty.
     */
    public static String extractDosage(String description) throws PharmaTrackerException {
        int dosageIndex = description.indexOf(FLAG_DOSAGE);
        int quantityIndex = description.indexOf(FLAG_QUANTITY);

        if (dosageIndex == -1 || quantityIndex == -1 || dosageIndex > quantityIndex) {
            throw new PharmaTrackerException("Invalid format! Please ensure you include '/d' followed by '/q'.");
        }

        String dosage = description.substring(dosageIndex + FLAG_DOSAGE.length(), quantityIndex).trim();
        if (dosage.isEmpty()) {
            throw new PharmaTrackerException("Dosage cannot be empty!");
        }

        return dosage;
    }

    /**
     * Extracts the mandatory medication quantity from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted medication quantity.
     * @throws PharmaTrackerException If the format is invalid, missing flags, or if the quantity is empty.
     */
    public static int extractQuantity(String description) throws PharmaTrackerException {
        int quantityIndex = description.indexOf(FLAG_QUANTITY);
        int expiryIndex = description.indexOf(FLAG_EXPIRY_DATE);

        if (quantityIndex == -1 || expiryIndex == -1 || quantityIndex > expiryIndex) {
            throw new PharmaTrackerException("Invalid format! Please ensure you include '/q' followed by '/e'.");
        }

        String quantityString = description.substring(quantityIndex + 2, expiryIndex).trim();
        if (quantityString.isEmpty()) {
            throw new PharmaTrackerException("Quantity cannot be empty.");
        }

        try {
            int quantity = Integer.parseInt(quantityString);
            if (quantity < 0) {
                throw new PharmaTrackerException("Quantity cannot be negative or zero!");
            }
            return quantity;
        } catch (NumberFormatException e) {
            throw new PharmaTrackerException("Invalid quantity! Please enter a valid whole number.");
        }
    }

    /**
     * Extracts the mandatory medication expiry date from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted medication expiry date.
     * @throws PharmaTrackerException If the format is invalid, missing flags, or if the expiry date is empty.
     */
    public static String extractExpiryDate(String description) throws PharmaTrackerException {
        int expiryIndex = description.indexOf(FLAG_EXPIRY_DATE);
        if (expiryIndex == -1) {
            throw new PharmaTrackerException("Invalid format! Please ensure you include the '/e' flag.");
        }

        int valueStart = expiryIndex + 2;
        int valueEnd = ParserUtil.findNextFlagIndex(description, valueStart);

        String rawExpiryDate = description.substring(valueStart, valueEnd).trim();
        if (rawExpiryDate.isEmpty()) {
            throw new PharmaTrackerException("Expiry date cannot be empty!");
        }

        LocalDate parsedDate = parseDate(rawExpiryDate);
        if (parsedDate == null) {
            throw new PharmaTrackerException("Invalid date format! Supported formats: yyyy-MM-dd, d/M/yyyy, d-M-yyyy");
        }

        validateExpiryDateRules(parsedDate);

        return parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Extracts an optional expiry date for commands that modify existing entries.
     * @param description The raw string containing command arguments.
     * @return The extracted and formatted date string, or null if the flag is not present.
     * @throws PharmaTrackerException If the format is invalid or the date violates business rules.
     */
    public static String extractOptionalExpiryDate(String description) throws PharmaTrackerException {
        int expiryIndex = description.indexOf(FLAG_EXPIRY_DATE);
        if (expiryIndex == -1) {
            return null;
        }

        int valueStart = expiryIndex + 2;
        int valueEnd = ParserUtil.findNextFlagIndex(description, valueStart);

        String rawExpiryDate = description.substring(valueStart, valueEnd).trim();
        if (rawExpiryDate.isEmpty()) {
            throw new PharmaTrackerException("Expiry date cannot be empty if the '/e' flag is provided!");
        }

        LocalDate parsedDate = parseDate(rawExpiryDate);
        if (parsedDate == null) {
            throw new PharmaTrackerException("Invalid date format! Supported formats: yyyy-MM-dd, d/M/yyyy, d-M-yyyy");
        }

        validateExpiryDateRules(parsedDate);

        return parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Validates a parsed date against the pharmacy's business rules.
     *
     * @param parsedDate The LocalDate to validate.
     * @throws PharmaTrackerException If the date is in the past or exceeds the 10-year limit.
     */
    private static void validateExpiryDateRules(LocalDate parsedDate) throws PharmaTrackerException {
        LocalDate today = LocalDate.now();

        if (parsedDate.isBefore(today)) {
            throw new PharmaTrackerException("Invalid expiry date! Expired medications cannot be added or updated.");
        }

        if (parsedDate.isAfter(today.plusYears(10))) {
            throw new PharmaTrackerException("Invalid expiry date! Expiry dates at least 10 years away are not valid.");
        }
    }

    /**
     * Extracts all occurrences of the warning flag and compiles them into a list.
     *
     * @param description The raw string containing command arguments.
     * @return An {@code ArrayList} containing all extracted warning strings.
     * @throws PharmaTrackerException If an error occurs during flag boundary detection.
     */
    static ArrayList<String> extractWarnings(String description) throws PharmaTrackerException {
        ArrayList<String> warnings = new ArrayList<>();
        int searchFrom = 0;
        while (true) {
            int idx = description.indexOf(FLAG_WARNINGS, searchFrom);
            if (idx == -1) {
                break;
            }
            int valueStart = idx + FLAG_WARNINGS.length();
            int valueEnd = ParserUtil.findNextFlagIndex(description, valueStart);
            String value = description.substring(valueStart, valueEnd).trim();
            if (!value.isEmpty()) {
                warnings.add(value);
            }
            searchFrom = valueStart;
        }
        return warnings;
    }

    /**
     * Extracts the optional medication quantity and converts it to an Integer object.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted Integer quantity, or null if the /q flag is not present.
     * @throws PharmaTrackerException If the quantity format is invalid or negative.
     */
    static Integer extractOptionalQuantity(String description) throws PharmaTrackerException {
        String quantityString = ParserUtil.extractOptionalFlag(description, FLAG_QUANTITY);
        if (quantityString == null) {
            return null;
        }

        try {
            int quantity = Integer.parseInt(quantityString);
            if (quantity < 0) {
                throw new PharmaTrackerException("Quantity cannot be negative!");
            }
            return quantity;
        } catch (NumberFormatException e) {
            throw new PharmaTrackerException("Invalid quantity! Please enter a valid whole number.");
        }
    }

    /**
     * Attempts to parse a date string using the supported formats: yyyy-MM-dd, d/M/yyyy, d-M-yyyy
     * @param dateString The raw date string from user input.
     * @return The parsed LocalDate object, or null if the format is unsupported.
     */
    private static LocalDate parseDate(String dateString) {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT),
                DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT),
                DateTimeFormatter.ofPattern("d-M-uuuu").withResolverStyle(ResolverStyle.STRICT)
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Continue to the next formatter if parsing fails
            }
        }
        return null;
    }
}
