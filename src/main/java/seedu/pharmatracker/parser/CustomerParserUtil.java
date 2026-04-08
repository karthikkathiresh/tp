package seedu.pharmatracker.parser;

import seedu.pharmatracker.exceptions.PharmaTrackerException;

/**
 * Provides utility methods specifically for extracting and parsing customer attributes
 * from user input strings.
 * This class defines the specific flag boundaries associated with customer profiles
 * (such as ID, name, phone number, and address) and handles the validation and formatting
 * of these dedicated fields.
 */
public class CustomerParserUtil {
    static final String[] CUSTOMER_UPDATE_FLAGS = {"/n", "/p", "/a"};

    /**
     * Extracts the mandatory customer ID from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted customer ID.
     * @throws PharmaTrackerException If the format is invalid, missing flags, or if the ID is empty.
     */
    static String extractCustomerID(String description) throws PharmaTrackerException {
        int idIndex = description.indexOf("/id");
        int nameIndex = description.indexOf("/n");

        if (idIndex == -1 || nameIndex == -1 || idIndex >= nameIndex) {
            throw new PharmaTrackerException("Invalid format! Please ensure you include '/id' followed by '/n'.");
        }

        String id = description.substring(idIndex + 3, nameIndex).trim();

        if (id.isEmpty()) {
            throw new PharmaTrackerException("Customer ID cannot be empty!");

        }
        return id;
    }

    /**
     * Extracts the mandatory customer name from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted customer name.
     * @throws PharmaTrackerException PharmaTrackerException If the format is invalid.
     *
     */
    public static String extractCustomerName(String description) throws PharmaTrackerException {
        int nameIndex = description.indexOf("/n");
        int phoneIndex = description.indexOf("/p");

        if (nameIndex == -1 || phoneIndex == -1 || nameIndex >= phoneIndex) {
            throw new PharmaTrackerException("Invalid format! Please ensure you include '/n' followed by '/p'.");
        }

        String name = description.substring(nameIndex + 2, phoneIndex).trim();

        if (name.isEmpty()) {
            throw new PharmaTrackerException("Customer name cannot be empty!");
        }

        return name;
    }

    /**
     * Extracts the mandatory customer phone number from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted customer phone number.
     * @throws PharmaTrackerException PharmaTrackerException If the format is invalid.
     */
    public static String extractCustomerPhone(String description) throws PharmaTrackerException {
        int phoneIndex = description.indexOf("/p");
        int addressIndex = description.indexOf("/addr");

        if (phoneIndex == -1) {
            throw new PharmaTrackerException("Invalid format! Please ensure you include the '/p' flag.");
        }

        int endIndex = (addressIndex == -1) ? description.length() : addressIndex;

        if (phoneIndex >= endIndex) {
            throw new PharmaTrackerException("Invalid format! '/p' must come before '/addr'");
        }

        String phone = description.substring(phoneIndex + 2, endIndex).trim();

        if (phone.isEmpty()) {
            throw new PharmaTrackerException("Customer phone cannot be empty!");
        }

        return phone;
    }

    /**
     * Extracts the optional customer address from the user input.
     *
     * @param description The raw string containing command arguments.
     * @return The extracted customer address, or an empty string if not provided.
     * @throws PharmaTrackerException If the '/addr' flag is present but the address is empty.
     */
    public static String extractCustomerAddress(String description) throws PharmaTrackerException {
        int addressIndex = description.indexOf("/addr");
        String warning = "Customer address cannot be empty if the /addr flag is used!";

        if (addressIndex == -1) {
            return "";
        }

        if (addressIndex + 5 >= description.length()) {
            throw new PharmaTrackerException(warning);
        }

        String address = description.substring(addressIndex + 5).trim();

        if (address.isEmpty()) {
            throw new PharmaTrackerException(warning);
        }

        return address;
    }

    /**
     * Extracts an optional customer flag for updatecustomer, returning {@code null} if absent.
     * Uses customer-specific flag boundaries (/n, /p, /a).
     *
     * @param description The raw argument string (without the command word and index).
     * @param flag        The flag to look for.
     * @return The trimmed value if the flag is present, or {@code null} if the flag is absent.
     * @throws PharmaTrackerException If the flag is present but has no accompanying value.
     */
    static String extractCustomerUpdateFlag(String description, String flag)
            throws PharmaTrackerException {
        int flagIndex = description.indexOf(flag);
        if (flagIndex == -1) {
            return null;
        }
        int valueStart = flagIndex + flag.length();
        if (valueStart >= description.length()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }
        int valueEnd = description.length();
        for (String f : CUSTOMER_UPDATE_FLAGS) {
            int idx = description.indexOf(f, valueStart);
            if (idx != -1 && idx < valueEnd) {
                valueEnd = idx;
            }
        }
        String extractedValue = description.substring(valueStart, valueEnd).trim();
        if (extractedValue.isEmpty()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }
        return extractedValue;
    }
}
