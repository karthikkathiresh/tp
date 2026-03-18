package seedu.pharmatracker.ui;

import java.util.Scanner;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

/**
 * Handles all user interactions for the PharmaTracker application.
 * This class is responsible for reading user input from the console and
 * formatting and displaying messages, errors, and data to the user.
 */
public class Ui {
    public static final String LOGO = "  _____  _                               _______              _\n" +
            " |  __ \\| |                             |__   __|            | |\n" +
            " | |__) | |__   __ _ _ __ _ __ ___   __ _  | | _ __ __ _  ___| | _____ _ __\n" +
            " |  ___/| '_ \\ / _` | '__| '_ ` _ \\ / _` | | || '__/ _` |/ __| |/ / _ \\ '__|\n" +
            " | |    | | | | (_| | |  | | | | | | (_| | | || | | (_| | (__|   <  __/ |\n" +
            " |_|    |_| |_|\\__,_|_|  |_| |_| |_|\\__,_| |_||_|  \\__,_|\\___|_|\\_\\___|_|\n";

    private static final String DIVIDER = "____________________________________________________________";
    private static final String DETAIL_DIVIDER = "----------------------------------------";
    private static final String DETAIL_BORDER = "========================================";
    private static final String MESSAGE_ADDED = "You have added the following medication:";
    private static final String INDENT = "  ";
    private static final String MESSAGE_DELETED = "You have deleted the following medication:";
    private static final String MESSAGE_WELCOME = "Welcome to Pharma Tracker!\nWhat can I do for you today?";
    private static final String MESSAGE_COMMAND = "Enter command: ";

    private final Scanner in;

    /**
     * Constructs a {@code Ui} instance.
     * Initializes the standard input scanner used to read commands from the user.
     */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /**
     * Prints an arbitrary number of messages to the console, each on a new line.
     *
     * @param message A variable number of string arguments to be printed.
     */
    public void printToScreen(String... message) {
        for (String m : message) {
            System.out.println(m);
        }
    }

    /**
     * Displays the initial greeting message to the user, including the application logo.
     */
    public void printWelcomeMessage() {
        printToScreen(
                "Hello from\n" + LOGO,
                DIVIDER,
                MESSAGE_WELCOME,
                DIVIDER
        );
    }

    /**
     * Prompts the user for a command and reads their input from the console.
     *
     * @return The raw, trimmed string entered by the user.
     */
    public String readCommand() {
        System.out.print(MESSAGE_COMMAND);
        return in.nextLine().trim();
    }

    /**
     * Prints a standard message to the console.
     *
     * @param message The text message to be displayed.
     */
    public void printMessage(String message) {
        printToScreen(
                DIVIDER,
                message,
                DIVIDER
        );
    }

    /**
     * Displays a confirmation message indicating that a medication has been successfully
     * added to the inventory, along with the updated total medication count.
     *
     * @param med       The {@link Medication} that was just added.
     * @param inventory The current {@Link Inventory} to retrieve the updated total count.
     */
    public void printAddedMessage(Medication med, Inventory inventory) {
        int count = inventory.getMedicationCount();
        printToScreen(
                DIVIDER,
                MESSAGE_ADDED,
                INDENT + med.toString(),
                "You now have " + count + " medications in your inventory!",
                DIVIDER
        );
    }

    /**
     * Displays a confirmation message indicating that a medication has been successfully
     * removed from the inventory, along with the updated total medication count.
     *
     * @param med The {@Link Medication} that was just deleted.
     * @param inventory The current {@Link Inventory} to retrieve the updated total count.
     */
    public void printDeletedMessage(Medication med, Inventory inventory) {
        int count = inventory.getMedicationCount();
        printToScreen(
                DIVIDER,
                MESSAGE_DELETED,
                INDENT + med.toString(),
                "You now have " + count + " medications in your inventory!",
                DIVIDER
        );
    }

    /**
     * Prints a highly detailed, formatted view of a specific medication's attributes.
     * Empty optional fields are represented as "N/A" for better readability.
     *
     * @param med The {@Link Medication} whose detailed information is to be displayed.
     */
    public void printMedicationDetails(Medication med) {
        System.out.println(DETAIL_BORDER);
        System.out.println("MEDICATION DETAILS");
        System.out.println(DETAIL_BORDER);
        System.out.printf("%-20s %s%n", "Drug Name:", med.getName());
        System.out.printf("%-20s %s%n", "Strength:", med.getDosage());
        System.out.printf("%-20s %s%n", "Dosage Form:",
                med.getDosageForm().isEmpty() ? "N/A" : med.getDosageForm());
        System.out.printf("%-20s %s%n", "Manufacturer:",
                med.getManufacturer().isEmpty() ? "N/A" : med.getManufacturer());
        System.out.println(DETAIL_DIVIDER);
        System.out.println("DOSAGE & ADMINISTRATION");
        System.out.println(DETAIL_DIVIDER);
        System.out.printf("%-20s %s%n", "Directions:",
                med.getDirections().isEmpty() ? "N/A" : med.getDirections());
        System.out.printf("%-20s %s%n", "Frequency:",
                med.getFrequency().isEmpty() ? "N/A" : med.getFrequency());
        System.out.printf("%-20s %s%n", "Route:",
                med.getRoute().isEmpty() ? "N/A" : med.getRoute());
        System.out.printf("%-20s %s%n", "Max Daily Dose:",
                med.getMaxDailyDose().isEmpty() ? "N/A" : med.getMaxDailyDose());
        System.out.println(DETAIL_DIVIDER);
        System.out.println("WARNINGS & PRECAUTIONS");
        System.out.println(DETAIL_DIVIDER);
        if (med.getWarnings().isEmpty()) {
            System.out.println("None");
        } else {
            for (String warning : med.getWarnings()) {
                System.out.println("- " + warning);
            }
        }
        System.out.println(DETAIL_BORDER);
    }

}
