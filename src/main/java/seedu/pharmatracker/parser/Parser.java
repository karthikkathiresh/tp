package seedu.pharmatracker.parser;

import java.util.ArrayList;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.command.AddCustomerCommand;
import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.DeleteCommand;
import seedu.pharmatracker.command.ListCommand;
import seedu.pharmatracker.command.SortCommand;
import seedu.pharmatracker.command.FindCommand;
import seedu.pharmatracker.command.ViewCommand;
import seedu.pharmatracker.command.DispenseCommand;
import seedu.pharmatracker.command.HelpCommand;
import seedu.pharmatracker.command.ExitCommand;
import seedu.pharmatracker.command.LabelCommand;
import seedu.pharmatracker.command.ExpiringCommand;
import seedu.pharmatracker.command.LowStockCommand;
import seedu.pharmatracker.command.UpdateCustomerCommand;
import seedu.pharmatracker.command.ViewCustomerCommand;
import seedu.pharmatracker.command.RestockCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

/**
 * Parses user input into executable commands.
 * Handles the extraction and validation of mandatory and optional flags from the raw command string.
 */
public class Parser {

    public static final String FLAG_NAME = "/n";
    public static final String FLAG_DOSAGE = "/d";
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

    private static final String[] ALL_FLAGS = {
        FLAG_NAME, FLAG_DOSAGE, FLAG_QUANTITY, FLAG_EXPIRY_DATE, FLAG_TAG,
        FLAG_DOSAGE_FORM, FLAG_MANUFACTURER, FLAG_DIRECTION, FLAG_FREQUENCY,
        FLAG_ROUTE, FLAG_MAX_DOSAGE, FLAG_WARNINGS
    };

    private static final String[] CUSTOMER_UPDATE_FLAGS = {"/n", "/p", "/a"};

    /**
     * Finds the index of the next flag appearing in the description string after a specified index.
     * This is used to determine the end bound of a flag's associated value.
     *
     * @param description The raw string containing command arguments.
     * @param afterIndex The index to start searching from.
     * @return The index of the next occurring flag, or the length of the string if no more flags exist.
     * @throws PharmaTrackerException If the search index provided is invalid.
     */
    private static int findNextFlagIndex(String description, int afterIndex) throws PharmaTrackerException {
        if (afterIndex < 0 || afterIndex > description.length()) {
            throw new PharmaTrackerException("Error parsing command flags: Invalid search index.");
        }

        int earliest = description.length();
        for (String flag : ALL_FLAGS) {
            int idx = description.indexOf(flag, afterIndex);
            if (idx != -1 && idx < earliest) {
                earliest = idx;
            }
        }
        return earliest;
    }

    /**
     * Extracts the string value associated with a generic optional flag.
     *
     * @param description The raw string containing command arguments.
     * @param flag The specific flag to search for.
     * @return The extracted string value, or an empty string if the flag is not present.
     * @throws PharmaTrackerException If the flag is present but has no accompanying value.
     */
    private static String extractFlag(String description, String flag) throws PharmaTrackerException {
        int flagIndex = description.indexOf(flag);
        if (flagIndex == -1) {
            return "";
        }

        int valueStart = flagIndex + flag.length();
        if (valueStart >= description.length()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }

        int valueEnd = findNextFlagIndex(description, valueStart);
        String extractedValue = description.substring(valueStart, valueEnd).trim();

        if (extractedValue.isEmpty()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }

        return extractedValue;
    }

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

        String dosage = description.substring(dosageIndex + 2, quantityIndex).trim();
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
                throw new PharmaTrackerException("Quantity cannot be negative!");
            }
            return quantity;
        } catch (NumberFormatException e) {
            throw new PharmaTrackerException("Invalid quantity! Please enter a valid whole number.");
        }
    }

    /**
     * Extracts the mandatory medication expiry date from the user input.
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
        int valueEnd = findNextFlagIndex(description, valueStart);

        if (valueStart > description.length()) {
            throw new PharmaTrackerException("Expiry date cannot be empty!");
        }

        String expiryDate = description.substring(valueStart, valueEnd);

        if (expiryDate.isEmpty()) {
            throw new PharmaTrackerException("Expiry date cannot be empty!");
        }

        return expiryDate;
    }

    /**
     * Extracts all occurrences of the warning flag and compiles them into a list.
     *
     * @param description The raw string containing command arguments.
     * @return An {@code ArrayList} containing all extracted warning strings.
     * @throws PharmaTrackerException If an error occurs during flag boundary detection.
     */
    private static ArrayList<String> extractWarnings(String description) throws PharmaTrackerException {
        ArrayList<String> warnings = new ArrayList<>();
        int searchFrom = 0;
        while (true) {
            int idx = description.indexOf(FLAG_WARNINGS, searchFrom);
            if (idx == -1) {
                break;
            }
            int valueStart = idx + FLAG_WARNINGS.length();
            int valueEnd = findNextFlagIndex(description, valueStart);
            String value = description.substring(valueStart, valueEnd).trim();
            if (!value.isEmpty()) {
                warnings.add(value);
            }
            searchFrom = valueStart;
        }
        return warnings;
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
    private static String extractCustomerUpdateFlag(String description, String flag)
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

    private static String extractCustomerID(String description) {
        int idIndex = description.indexOf("/id");
        int nameIndex = description.indexOf("/n");
        return description.substring(idIndex + 3, nameIndex).trim();
    }

    public static String extractCustomerName(String description) {
        int nameIndex = description.indexOf("/n");
        int phoneIndex = description.indexOf("/p");
        return description.substring(nameIndex + 2, phoneIndex);
    }

    public static String extractCustomerPhone(String description) {
        int phoneIndex = description.indexOf("/p");
        int addressIndex = description.indexOf("/addr");
        return description.substring(phoneIndex + 2, addressIndex);
    }

    public static String extractCustomerAddress(String description) {
        int addressIndex = description.indexOf("/addr");
        return description.substring(addressIndex + 5);
    }

    /**
     * Parses the full raw user input and returns the corresponding executable {@link Command}.
     *
     * @param userInput The raw string entered by the user.
     * @return The specific {@link Command} object representing the user's intent, or null if unhandled.
     * @throws PharmaTrackerException If the command word is unknown, or if the arguments are invalid / missing.
     */
    public static Command parse(String userInput) throws PharmaTrackerException {
        String[] inputParts = userInput.trim().split("\\s+", 2);
        String commandWord = inputParts[0].toLowerCase();
        String description = (inputParts.length > 1) ? inputParts[1] : "";

        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            String name = extractName(description);
            String dosage = extractDosage(description);
            int quantity = extractQuantity(description);
            String expiryDate = extractExpiryDate(description);
            String tag = extractFlag(description, FLAG_TAG);
            String dosageForm = extractFlag(description, FLAG_DOSAGE_FORM);
            String manufacturer = extractFlag(description, FLAG_MANUFACTURER);
            String directions = extractFlag(description, FLAG_DIRECTION);
            String frequency = extractFlag(description, FLAG_FREQUENCY);
            String route = extractFlag(description, FLAG_ROUTE);
            String maxDailyDose = extractFlag(description, FLAG_MAX_DOSAGE);
            ArrayList<String> warnings = extractWarnings(description);
            return new AddCommand(name, dosage, quantity, expiryDate, tag,
                                  dosageForm, manufacturer, directions, frequency,
                                  route, maxDailyDose, warnings);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommand(description);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case FindCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                System.out.println("Please provide a keyword to search for.");
                break;
            }
            return new FindCommand(description);

        case ViewCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                System.out.println("Please provide an index to view.");
                break;
            }
            try {
                int index = Integer.parseInt(description.trim());
                return new ViewCommand(index);
            } catch (NumberFormatException e) {
                System.out.println("Invalid index. Please enter a valid number.");
                break;
            }

        case DispenseCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                System.out.println("Please provide an index and quantity.");
                break;
            }
            try {
                String[] parts = description.trim().split("q/");
                int dispenseIndex = Integer.parseInt(parts[0].trim());
                int dispenseQuantity = Integer.parseInt(parts[1].trim());
                return new DispenseCommand(dispenseIndex, dispenseQuantity);
            } catch (Exception e) {
                System.out.println("Invalid format. Use: dispense INDEX q/QUANTITY");
                break;
            }

        case SortCommand.COMMAND_WORD:
            return new SortCommand();

        case LabelCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                System.out.println("Please provide an index for the label.");
                break;
            }
            try {
                int descIndex = Integer.parseInt(description.trim());
                return new LabelCommand(descIndex);
            } catch (NumberFormatException e) {
                System.out.println("Invalid index. Please enter a valid number.");
                break;
            }

        case ExpiringCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                return new ExpiringCommand();
            }
            if (description.contains("/days")) {
                try {
                    String daysStr = description.substring(
                            description.indexOf("/days") + "/days".length()).trim();
                    int days = Integer.parseInt(daysStr);
                    if (days <= 0) {
                        System.out.println("Number of days must be a positive integer.");
                        return null;
                    }
                    return new ExpiringCommand(days);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number of days. Usage: expiring /days NUMBER");
                    return null;
                }
            }
            System.out.println("Invalid format. Usage: expiring or expiring /days NUMBER");
            return null;

        case AddCustomerCommand.COMMAND_WORD:
            String id = extractCustomerID(description);
            String customerName = extractCustomerName(description);
            String phone = extractCustomerPhone(description);
            String address = extractCustomerAddress(description);
            return new AddCustomerCommand(id, customerName, phone, address);

        case UpdateCustomerCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                throw new PharmaTrackerException(
                        "Invalid format! Use: updatecustomer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]");
            }
            try {
                String[] ucParts = description.trim().split("\\s+", 2);
                int ucIndex = Integer.parseInt(ucParts[0]);
                String ucArgs = (ucParts.length > 1) ? ucParts[1] : "";
                String ucName = extractCustomerUpdateFlag(ucArgs, "/n");
                String ucPhone = extractCustomerUpdateFlag(ucArgs, "/p");
                String ucAddress = extractCustomerUpdateFlag(ucArgs, "/a");
                return new UpdateCustomerCommand(ucIndex, ucName, ucPhone, ucAddress);
            } catch (NumberFormatException e) {
                throw new PharmaTrackerException(
                        "Invalid index! The first argument must be a valid number.");
            }

        case ViewCustomerCommand.COMMAND_WORD:
            try {
                int index = Integer.parseInt(description.trim());
                return new ViewCustomerCommand(index);
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Usage: viewcustomer INDEX");
                return null;
            }

        case RestockCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                System.out.println("Please provide an index and quantity.");
                break;
            }
            try {
                String[] restockParts = description.trim().split("/q");
                int restockIndex = Integer.parseInt(restockParts[0].trim());
                int restockQuantity = Integer.parseInt(restockParts[1].trim());
                return new RestockCommand(restockIndex, restockQuantity);
            } catch (Exception e) {
                System.out.println("Invalid format. Use: restock INDEX /q QUANTITY");
                break;
            }

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case LowStockCommand.COMMAND_WORD:
            if (!description.isEmpty()) {
                String[] lsParts = description.trim().split("\\s+");
                for (int i = 0; i < lsParts.length; i++) {
                    if (lsParts[i].equalsIgnoreCase("/threshold") && i + 1 < lsParts.length) {
                        try {
                            int threshold = Integer.parseInt(lsParts[i + 1]);
                            if (threshold <= 0) {
                                throw new PharmaTrackerException(
                                        "Threshold must be a positive number.");
                            }
                            return new LowStockCommand(threshold);
                        } catch (NumberFormatException e) {
                            throw new PharmaTrackerException(
                                    "Invalid threshold value. Please enter a valid whole number.");
                        }
                    }
                }
                throw new PharmaTrackerException(
                        "Invalid format! Use: lowstock or lowstock /threshold NUMBER");
            }
            return new LowStockCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        default:
            throw new PharmaTrackerException("Unknown command! " +
                    "Please type 'help' to see the list of available commands.");
        }

        return null;
    }
}
