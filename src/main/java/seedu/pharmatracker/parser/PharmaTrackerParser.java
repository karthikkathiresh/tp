package seedu.pharmatracker.parser;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.command.AddCustomerCommand;
import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.DeleteCommand;
import seedu.pharmatracker.command.DeleteCustomerCommand;
import seedu.pharmatracker.command.ExportMedicationCommand;
import seedu.pharmatracker.command.ListCommand;
import seedu.pharmatracker.command.SortCommand;
import seedu.pharmatracker.command.FindCommand;
import seedu.pharmatracker.command.UpdateCommand;
import seedu.pharmatracker.command.ViewCommand;
import seedu.pharmatracker.command.DispenseCommand;
import seedu.pharmatracker.command.HelpCommand;
import seedu.pharmatracker.command.ExitCommand;
import seedu.pharmatracker.command.LabelCommand;
import seedu.pharmatracker.command.ExpiringCommand;
import seedu.pharmatracker.command.LowStockCommand;
import seedu.pharmatracker.command.UpdateCustomerCommand;
import seedu.pharmatracker.command.FindCustomerCommand;
import seedu.pharmatracker.command.ViewCustomerCommand;
import seedu.pharmatracker.command.RestockCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.command.ListCustomersCommand;

/**
 * Parses user input into executable commands.
 * Handles the extraction and validation of mandatory and optional flags from the raw command string.
 */
public class PharmaTrackerParser {

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
            return new AddCommandParser().parse(description);

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
                String qPart = parts[1];
                if (qPart.contains("c/")) {
                    String[] qAndC = qPart.split("c/");
                    int dispenseQuantity = Integer.parseInt(qAndC[0].trim());
                    int dispenseCustomer = Integer.parseInt(qAndC[1].trim());
                    return new DispenseCommand(dispenseIndex, dispenseQuantity, dispenseCustomer);
                }
                int dispenseQuantity = Integer.parseInt(qPart.trim());
                return new DispenseCommand(dispenseIndex, dispenseQuantity);
            } catch (Exception e) {
                System.out.println("Invalid format. Use: dispense INDEX q/QUANTITY [c/CUSTOMER_INDEX]");
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

        case UpdateCommand.COMMAND_WORD:
            return new UpdateCommandParser().parse(description);

        case ExportMedicationCommand.COMMAND_WORD:
            return new ExportMedicationCommand();

        case AddCustomerCommand.COMMAND_WORD:
            return new AddCustomerCommandParser().parse(description);

        case DeleteCustomerCommand.COMMAND_WORD:
            return new DeleteCustomerCommand(description);

        case UpdateCustomerCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                throw new PharmaTrackerException(
                        "Invalid format! Use: updatecustomer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]");
            }
            try {
                String[] ucParts = description.trim().split("\\s+", 2);
                int ucIndex = Integer.parseInt(ucParts[0]);
                String ucArgs = (ucParts.length > 1) ? ucParts[1] : "";
                String ucName = CustomerParserUtil.extractCustomerUpdateFlag(ucArgs, "/n");
                String ucPhone = CustomerParserUtil.extractCustomerUpdateFlag(ucArgs, "/p");
                String ucAddress = CustomerParserUtil.extractCustomerUpdateFlag(ucArgs, "/a");
                return new UpdateCustomerCommand(ucIndex, ucName, ucPhone, ucAddress);
            } catch (NumberFormatException e) {
                throw new PharmaTrackerException(
                        "Invalid index! The first argument must be a valid number.");
            }

        case FindCustomerCommand.COMMAND_WORD:
            if (description.trim().isEmpty()) {
                System.out.println("Please provide a name to search for. Usage: find-customer <name>");
            }
            return new FindCustomerCommand(description.trim());

        case ViewCustomerCommand.COMMAND_WORD:
            try {
                int index = Integer.parseInt(description.trim());
                return new ViewCustomerCommand(index);
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Usage: view-customer INDEX");
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

        case ListCustomersCommand.COMMAND_WORD:
            return new ListCustomersCommand();

        default:
            throw new PharmaTrackerException("Unknown command! " +
                    "Please type 'help' to see the list of available commands.");
        }

        return null;
    }
}
