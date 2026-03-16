package seedu.pharmatracker.parser;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.ListCommand;
import seedu.pharmatracker.command.SortCommand;
import seedu.pharmatracker.command.FindCommand;
import seedu.pharmatracker.command.ViewCommand;
import seedu.pharmatracker.command.DispenseCommand;
import seedu.pharmatracker.data.Inventory;

public class Parser {

    private static Inventory inventory;

    private static final String[] ALL_FLAGS = {
        "/n", "/d", "/q", "/e", "/t", "/df", "/mfr",
        "/dir", "/freq", "/route", "/max", "/warn"
    };

    public Parser(Inventory inventory) {
        this.inventory = inventory;
    }

    private static int findNextFlagIndex(String description, int afterIndex) {
        int earliest = description.length();
        for (String flag : ALL_FLAGS) {
            int idx = description.indexOf(flag, afterIndex);
            if (idx != -1 && idx < earliest) {
                earliest = idx;
            }
        }
        return earliest;
    }

    private static String extractFlag(String description, String flag) {
        int flagIndex = description.indexOf(flag);
        if (flagIndex == -1) {
            return "";
        }
        int valueStart = flagIndex + flag.length();
        int valueEnd = findNextFlagIndex(description, valueStart);
        return description.substring(valueStart, valueEnd).trim();
    }

    public static String extractName(String description) {
        int nameIndex = description.indexOf("/n");
        int dosageIndex = description.indexOf("/d");
        return description.substring(nameIndex + 2, dosageIndex).trim();
    }

    public static String extractDosage(String description) {
        int dosageIndex = description.indexOf("/d");
        int quantityIndex = description.indexOf("/q");
        return description.substring(dosageIndex + 2, quantityIndex).trim();
    }

    public static int extractQuantity(String description) {
        int quantityIndex = description.indexOf("/q");
        int expiryIndex = description.indexOf("/e");
        return Integer.parseInt(description.substring(quantityIndex + 2, expiryIndex).trim());
    }

    public static String extractExpiryDate(String description) {
        int expiryIndex = description.indexOf("/e");
        int tagIndex = description.indexOf("/t");
        if (tagIndex == -1) {
            return description.substring(expiryIndex + 2).trim();
        }
        return description.substring(expiryIndex + 2, tagIndex).trim();
    }

    public static String extractTag(String description) {
        int tagIndex = description.indexOf("/t");
        return (tagIndex == -1) ? "" : description.substring(tagIndex + 2).trim();
    }

    private static java.util.ArrayList<String> extractWarnings(String description) {
        java.util.ArrayList<String> warnings = new java.util.ArrayList<>();
        int searchFrom = 0;
        while (true) {
            int idx = description.indexOf("/warn", searchFrom);
            if (idx == -1) {
                break;
            }
            int valueStart = idx + "/warn".length();
            int valueEnd = findNextFlagIndex(description, valueStart);
            String value = description.substring(valueStart, valueEnd).trim();
            if (!value.isEmpty()) {
                warnings.add(value);
            }
            searchFrom = valueStart;
        }
        return warnings;
    }

    public static Command parse(String userInput) {
        String[] inputParts = userInput.trim().split("\\s+", 2);
        String commandWord = inputParts[0].toLowerCase();
        String description = (inputParts.length > 1) ? inputParts[1] : "";

        switch (commandWord) {
        case "add":
            System.out.println("Add command triggered.");
            String name = extractName(description);
            String dosage = extractDosage(description);
            int quantity = extractQuantity(description);
            String expiryDate = extractExpiryDate(description);
            String tag = extractTag(description);
            String dosageForm = extractFlag(description, "/df");
            String manufacturer = extractFlag(description, "/mfr");
            String directions = extractFlag(description, "/dir");
            String frequency = extractFlag(description, "/freq");
            String route = extractFlag(description, "/route");
            String maxDailyDose = extractFlag(description, "/max");
            java.util.ArrayList<String> warnings = extractWarnings(description);
            return new AddCommand(name, dosage, quantity, expiryDate, tag,
                    dosageForm, manufacturer, directions, frequency, route,
                    maxDailyDose, warnings);

        case "delete":
            System.out.println("Delete command triggered.");
            break;

        case "list":
            System.out.println("List command triggered.");
            return new ListCommand();

        case "find":
            System.out.println("Find command triggered.");
            if (description.isEmpty()) {
                System.out.println("Please provide a keyword to search for.");
                break;
            }
            return new FindCommand(description);

        case "view":
            System.out.println("View command triggered.");
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

        case "dispense":
            System.out.println("Dispense command triggered.");
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

        case "sort":
            System.out.println("Sort command triggered.");
            return new SortCommand();

        case "label":
            System.out.println("Label command triggered.");
            break;

        case "help":
            System.out.println("Help command triggered.");
            break;

        case "exit":
            System.out.println("Exit command triggered.");
            System.exit(0);
            break;

        default:
            System.out.println("Unknown command! Please type 'help' to see the list of available commands.");
        }

        return null;
    }
}
