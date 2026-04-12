package seedu.pharmatracker.parser;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.command.AddCustomerCommand;
import seedu.pharmatracker.command.AlertHistoryCommand;
import seedu.pharmatracker.command.AlertsCommand;
import seedu.pharmatracker.command.AcknowledgeAlertCommand;
import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.DeleteCommand;
import seedu.pharmatracker.command.DeleteCustomerCommand;
import seedu.pharmatracker.command.DispenseSummaryCommand;
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
import seedu.pharmatracker.command.LoginCommand;
import seedu.pharmatracker.command.LowStockCommand;
import seedu.pharmatracker.command.LogoutCommand;
import seedu.pharmatracker.command.RegisterCommand;
import seedu.pharmatracker.command.SetThresholdCommand;
import seedu.pharmatracker.command.UpdateCustomerCommand;
import seedu.pharmatracker.command.FindCustomerCommand;
import seedu.pharmatracker.command.ViewCustomerCommand;
import seedu.pharmatracker.command.RestockCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.command.ListCustomersCommand;

import java.time.LocalDate;

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
                String[] parts = description.trim().split("\\s+", 2);
                int dispenseIndex = Integer.parseInt(parts[0].trim());
                String args = (parts.length > 1) ? parts[1] : "";
                
                if (!args.contains("/q")) {
                    System.out.println("Invalid format. Use: dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX]");
                    break;
                }
                
                String quantityStr = extractFlagValue(args, "/q", "/c");
                if (quantityStr == null || quantityStr.isEmpty()) {
                    System.out.println("Invalid format. Use: dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX]");
                    break;
                }
                int dispenseQuantity = Integer.parseInt(quantityStr.trim());
                
                if (args.contains("/c")) {
                    String customerStr = extractFlagValue(args, "/c", null);
                    if (customerStr != null && !customerStr.isEmpty()) {
                        int dispenseCustomer = Integer.parseInt(customerStr.trim());
                        return new DispenseCommand(dispenseIndex, dispenseQuantity, dispenseCustomer);
                    }
                }
                return new DispenseCommand(dispenseIndex, dispenseQuantity);
            } catch (Exception e) {
                System.out.println("Invalid format. Use: dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX]");
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

        case AddCustomerCommand.COMMAND_WORD:
            return new AddCustomerCommandParser().parse(description);

        case DeleteCustomerCommand.COMMAND_WORD:
            return new DeleteCustomerCommand(description);

        case UpdateCustomerCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                throw new PharmaTrackerException(
                        "Invalid format! Use: updatecustomer INDEX [/n NAME] [/p PHONE] [/address ADDRESS]"
                                + "[/allergy ALLERGY1,ALLERGY2,...]");
            }
            try {
                String[] ucParts = description.trim().split("\\s+", 2);
                int ucIndex = Integer.parseInt(ucParts[0]);
                String ucArgs = (ucParts.length > 1) ? ucParts[1] : "";
                String ucName = CustomerParserUtil.extractCustomerUpdateFlag(ucArgs, "/n");
                String ucPhone = CustomerParserUtil.extractCustomerUpdateFlag(ucArgs, "/p");
                if (ucPhone != null && !(ucPhone.startsWith("8") || ucPhone.startsWith("9"))) {
                    throw new PharmaTrackerException("Customer phone must be a valid Singapore number!\n"
                            + "Please ensure the number starts with either '8' or '9'");
                }
                String ucAddress = CustomerParserUtil.extractCustomerUpdateFlag(ucArgs, "/address");
                java.util.ArrayList<String> ucAllergies = null;
                if (ucArgs.contains(CustomerParserUtil.FLAG_ALLERGY)) {
                    ucAllergies = CustomerParserUtil.extractCustomerAllergies(ucArgs);
                }
                return new UpdateCustomerCommand(ucIndex, ucName, ucPhone, ucAddress, ucAllergies);
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

        case DispenseSummaryCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                return new DispenseSummaryCommand();
            }
            if (description.startsWith("/date")) {
                String dateStr = description.substring("/date".length()).trim();
                try {
                    LocalDate targetDate = LocalDate.parse(dateStr);
                    return new DispenseSummaryCommand(targetDate);
                } catch (Exception e) {
                    System.out.println("Invalid date format. Use: dispenselog /date YYYY-MM-DD");
                    return null;
                }
            }
            System.out.println("Invalid format. Use: dispenselog or dispenselog /date YYYY-MM-DD");
            return null;

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

        case RegisterCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                throw new PharmaTrackerException("Invalid format! Use: register USERNAME /p PASSWORD");
            }
            return parseRegisterOrLogin(description, true);

        case LoginCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                throw new PharmaTrackerException("Invalid format! Use: login USERNAME /p PASSWORD");
            }
            return parseRegisterOrLogin(description, false);

        case LogoutCommand.COMMAND_WORD:
            return new LogoutCommand();

        case SetThresholdCommand.COMMAND_WORD:
            return parseSetThreshold(description);

        case AlertsCommand.COMMAND_WORD:
            return new AlertsCommand();

        case AcknowledgeAlertCommand.COMMAND_WORD:
            return parseAcknowledgeAlert(description);

        case AlertHistoryCommand.COMMAND_WORD:
            return new AlertHistoryCommand();

        default:
            throw new PharmaTrackerException("Unknown command! " +
                    "Please type 'help' to see the list of available commands.");
        }

        return null;
    }

    private static Command parseRegisterOrLogin(String description, boolean isRegister)
            throws PharmaTrackerException {
        String[] parts = description.trim().split("/p", 2);
        if (parts.length != 2) {
            throw new PharmaTrackerException("Invalid format! Use: "
                    + (isRegister ? "register" : "login") + " USERNAME /p PASSWORD");
        }

        String username = parts[0].trim();
        String password = parts[1].trim();
        if (username.isEmpty() || password.isEmpty()) {
            throw new PharmaTrackerException("Username and password must not be empty.");
        }

        return isRegister ? new RegisterCommand(username, password)
                : new LoginCommand(username, password);
    }

    private static Command parseSetThreshold(String description) throws PharmaTrackerException {
        String[] parts = description.trim().split("/threshold", 2);
        if (parts.length != 2) {
            throw new PharmaTrackerException("Invalid format! Use: set-threshold INDEX /threshold NUMBER");
        }

        try {
            int index = Integer.parseInt(parts[0].trim());
            int threshold = Integer.parseInt(parts[1].trim());
            return new SetThresholdCommand(index, threshold);
        } catch (NumberFormatException e) {
            throw new PharmaTrackerException("Invalid format! INDEX and threshold must be whole numbers.");
        }
    }

    private static Command parseAcknowledgeAlert(String description) throws PharmaTrackerException {
        try {
            int index = Integer.parseInt(description.trim());
            return new AcknowledgeAlertCommand(index);
        } catch (NumberFormatException e) {
            throw new PharmaTrackerException("Invalid format! Use: ack-alert ALERT_INDEX");
        }
    }

    /**
     * Extracts the value of a flag from the command arguments.
     *
     * @param args The full command arguments string.
     * @param flag The flag to extract (e.g., "/q", "/c").
     * @param nextFlag The next flag that marks the end of this flag's value (null if at end).
     * @return The value associated with the flag, or null if flag not found.
     */
    private static String extractFlagValue(String args, String flag, String nextFlag) {
        int flagIndex = args.indexOf(flag);
        if (flagIndex == -1) {
            return null;
        }

        int startIndex = flagIndex + flag.length();
        int endIndex = args.length();

        if (nextFlag != null) {
            int nextFlagIndex = args.indexOf(nextFlag, startIndex);
            if (nextFlagIndex != -1) {
                endIndex = nextFlagIndex;
            }
        }

        return args.substring(startIndex, endIndex).trim();
    }
}
