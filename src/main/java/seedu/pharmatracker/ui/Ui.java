package seedu.pharmatracker.ui;

import java.util.Scanner;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String DETAIL_DIVIDER = "----------------------------------------";
    private static final String DETAIL_BORDER = "========================================";
    private Scanner in;

    public Ui() {
        this.in = new Scanner(System.in);
    }

    public void printWelcomeMessage() {
        String logo =
                "  _____  _                               _______              _\n" +
                        " |  __ \\| |                             |__   __|            | |\n" +
                        " | |__) | |__   __ _ _ __ _ __ ___   __ _  | | _ __ __ _  ___| | _____ _ __\n" +
                        " |  ___/| '_ \\ / _` | '__| '_ ` _ \\ / _` | | || '__/ _` |/ __| |/ / _ \\ '__|\n" +
                        " | |    | | | | (_| | |  | | | | | | (_| | | || | | (_| | (__|   <  __/ |\n" +
                        " |_|    |_| |_|\\__,_|_|  |_| |_| |_|\\__,_| |_||_|  \\__,_|\\___|_|\\_\\___|_|\n";
        System.out.println("Hello from\n" + logo);
        printLineDivider();
        System.out.println("Welcome to Pharma Tracker!");
        System.out.println("What can I do for you today?");
        printLineDivider();
    }

    public void printLineDivider() {
        System.out.println(DIVIDER);
    }

    public String readCommand() {
        System.out.print("Enter command: ");
        return in.nextLine().trim();
    }

    public void printMessage(String message) {
        printLineDivider();
        System.out.println(message);
        printLineDivider();
    }

    public void printDeletedMessage(Medication med, Inventory inventory) {
        System.out.println("You have deleted the following medication: ");
        System.out.println(med.toString());
        int count = inventory.getMedicationCount();
        System.out.println("You now have " + count + " medications in your inventory!");
        System.out.println(DETAIL_DIVIDER);
    }

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
