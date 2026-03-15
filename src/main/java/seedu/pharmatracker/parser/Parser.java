package seedu.pharmatracker.parser;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.ListCommand;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

public class Parser {

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
        return description.substring(expiryIndex + 2, tagIndex).trim();
    }

    public static String extractTag(String description) {
        int tagIndex = description.indexOf("/t");
        return description.substring(tagIndex + 2).trim();
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
            return new AddCommand(name, dosage, quantity, expiryDate, tag);

        case "delete":
            System.out.println("Delete command triggered.");
            break;

        case "dispense":
            System.out.println("Dispense command triggered.");
            break;

        case "list":
            System.out.println("List command triggered.");
            return new ListCommand();

        case "find":
            System.out.println("Find command triggered.");
            break;

        case "view":
            System.out.println("View command triggered.");
            break;

        case "sort":
            System.out.println("Sort command triggered.");
            break;

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
