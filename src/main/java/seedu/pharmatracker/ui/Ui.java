package seedu.pharmatracker.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import seedu.pharmatracker.alert.RestockAlert;
import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.DispenseRecord;
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
    private static final String MESSAGE_ADDED_CUSTOMER = "You have added the following customer:";
    private static final String MESSAGE_DELETED_CUSTOMER = "You have removed the following customer:";

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
                MESSAGE_ADDED
        );

        printMedicationDetails(med);
        printToScreen(
                "You now have " + count + " medications in your inventory!",
                DIVIDER
        );
    }

    /**
     * Displays a confirmation message indicating that a medication has been successfully
     * removed from the inventory, along with the updated total medication count.
     *
     * @param med       The {@link Medication} that was just deleted.
     * @param inventory The current {@Link Inventory} to retrieve the updated total count.
     */
    public void printDeletedMessage(Medication med, Inventory inventory) {
        int count = inventory.getMedicationCount();
        printToScreen(
                DIVIDER,
                MESSAGE_DELETED
        );

        printMedicationDetails(med);
        printToScreen(
                "You now have " + count + " medications in your inventory!",
                DIVIDER
        );
    }

    /**
     * Displays a confirmation message indicating that a customer has been successfully
     * added to the database, along with the updated total customer count.
     *
     * @param customer     THe {@link Customer} that was added.
     * @param customerList The current {@link CustomerList} to retrieve the updated total count.
     */
    public void printAddedCustomerMessage(Customer customer, CustomerList customerList) {
        int count = customerList.getCustomerCount();
        printToScreen(
                DIVIDER,
                MESSAGE_ADDED_CUSTOMER,
                INDENT + customer.toString(),
                "You now have " + count + " customers in your database!",
                DIVIDER
        );
    }

    /**
     * Displays a confirmation message indicating that a customer has been successfully
     * removed from the database, along with the updated total customer count.
     *
     * @param customer     The {@link Customer} that was removed.
     * @param customerList The current {@link CustomerList} to retrieve the updated total count.
     */
    public void printDeletedCustomerMessage(Customer customer, CustomerList customerList) {
        int count = customerList.getCustomerCount();
        printToScreen(
                DIVIDER,
                MESSAGE_DELETED_CUSTOMER,
                INDENT + customer.toString(),
                "You now have " + count + " customers in your database!",
                DIVIDER
        );
    }

    /**
     * Prints the list of medications that matched the search keyword.
     *
     * @param matchingMedications The list of matched medications to display.
     */
    public void printFindResults(ArrayList<Medication> matchingMedications) {
        System.out.println("Found " + matchingMedications.size() + " matching medication(s):");
        for (int i = 0; i < matchingMedications.size(); i++) {
            System.out.println((i + 1) + ". " + matchingMedications.get(i).toString());
        }
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

    /**
     * Displays the full details of a customer including dispensing history.
     *
     * @param customer The customer whose details are to be displayed.
     */
    public void showCustomerDetails(Customer customer) {
        System.out.println("========================================");
        System.out.println("CUSTOMER DETAILS");
        System.out.println("========================================");
        System.out.printf("%-20s %s%n", "Customer ID:", customer.getCustomerId());
        System.out.printf("%-20s %s%n", "Name:", customer.getName());
        System.out.printf("%-20s %s%n", "Phone:", customer.getPhone());
        System.out.printf("%-20s %s%n", "Address:",
                customer.getAddress().isEmpty() ? "N/A" : customer.getAddress());
        System.out.printf("%-20s %s%n", "Allergies:",
                customer.getAllergies().isEmpty() ? "None" : String.join(", ", customer.getAllergies()));
        System.out.println("----------------------------------------");
        System.out.println("DISPENSING HISTORY");
        System.out.println("----------------------------------------");

        if (customer.getDispensingHistory().isEmpty()) {
            System.out.println("No medications dispensed yet.");
        } else {
            for (int i = 0; i < customer.getDispensingHistory().size(); i++) {
                System.out.printf("%d. %s%n", i + 1, customer.getDispensingHistory().get(i));
            }
        }

        System.out.println("========================================");
    }

    /**
     * Displays medications in two sections: already expired and expiring soon.
     * If both lists are empty, a single message is shown indicating no results.
     *
     * @param expiredMeds  The list of medications that have already expired.
     * @param expiringMeds The list of medications expiring within the given window.
     * @param days         The expiry window in days used for the search.
     */
    public void showExpiringMedications(ArrayList<Medication> expiredMeds,
                                        ArrayList<Medication> expiringMeds, int days) {
        System.out.println(DIVIDER);
        if (expiredMeds.isEmpty() && expiringMeds.isEmpty()) {
            System.out.println("No expired or expiring medications found.");
            System.out.println(DIVIDER);
            return;
        }

        if (!expiredMeds.isEmpty()) {
            System.out.println("Already expired:");
            for (int i = 0; i < expiredMeds.size(); i++) {
                System.out.println((i + 1) + ". " + expiredMeds.get(i).toString());
            }
            System.out.println("Total: " + expiredMeds.size() + " medication(s) expired.");
            System.out.println("----------------------------------------");
        }

        if (!expiringMeds.isEmpty()) {
            System.out.println("Expiring within " + days + " days:");
            for (int i = 0; i < expiringMeds.size(); i++) {
                System.out.println((i + 1) + ". " + expiringMeds.get(i).toString());
            }
            System.out.println("Total: " + expiringMeds.size() + " medication(s) expiring soon.");
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints all medications whose quantity is below the given threshold.
     *
     * @param lowStockMeds List of medications below the threshold.
     * @param threshold    The stock threshold used to filter medications.
     */
    public void printLowStockList(java.util.List<Medication> lowStockMeds, int threshold) {
        System.out.println(DIVIDER);
        if (lowStockMeds.isEmpty()) {
            System.out.println("No medications are low on stock (below " + threshold + " units).");
            System.out.println(DIVIDER);
            return;
        }
        System.out.println("Medications low on stock (below " + threshold + " units):");
        for (int i = 0; i < lowStockMeds.size(); i++) {
            Medication med = lowStockMeds.get(i);
            System.out.println((i + 1) + ". " + med.getName()
                    + " | " + med.getDosage()
                    + " | Qty: " + med.getQuantity()
                    + " | Expiry: " + med.getExpiryDate());
        }
        System.out.println("Total: " + lowStockMeds.size() + " medication(s) low on stock.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints active automatic restock alerts.
     *
     * @param activeAlerts Active, unacknowledged alerts.
     */
    public void printActiveAlerts(ArrayList<RestockAlert> activeAlerts) {
        System.out.println(DIVIDER);
        if (activeAlerts.isEmpty()) {
            System.out.println("No active restock alerts.");
            System.out.println(DIVIDER);
            return;
        }

        System.out.println("Active Restock Alerts:");
        for (int i = 0; i < activeAlerts.size(); i++) {
            RestockAlert alert = activeAlerts.get(i);
            System.out.println((i + 1) + ". " + alert.getMedicationName()
                    + " | Current Stock: " + alert.getCurrentStock()
                    + " | Threshold: " + alert.getThreshold()
                    + " | Created: " + alert.getCreatedAtString());
        }
        System.out.println("Use 'ack-alert ALERT_INDEX' to acknowledge an alert.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints full restock alert history including acknowledged and auto-resolved alerts.
     *
     * @param history Full alert history list.
     */
    public void printAlertHistory(ArrayList<RestockAlert> history) {
        System.out.println(DIVIDER);
        if (history.isEmpty()) {
            System.out.println("No restock alert history found.");
            System.out.println(DIVIDER);
            return;
        }

        System.out.println("Restock Alert History:");
        for (int i = 0; i < history.size(); i++) {
            RestockAlert alert = history.get(i);
            String status = alert.isAcknowledged()
                    ? "Acknowledged"
                    : "Active";
            System.out.println((i + 1) + ". " + alert.getMedicationName()
                    + " | Stock: " + alert.getCurrentStock()
                    + " | Threshold: " + alert.getThreshold()
                    + " | Status: " + status
                    + " | Created: " + alert.getCreatedAtString());
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints a concise summary for newly detected active alerts.
     *
     * @param activeAlerts Active alerts to summarize.
     */
    public void printAutoRestockAlertSummary(ArrayList<RestockAlert> activeAlerts) {
        if (activeAlerts.isEmpty()) {
            return;
        }

        System.out.println(DIVIDER);
        System.out.println("AUTO RESTOCK ALERT: " + activeAlerts.size() + " medication(s) below threshold.");
        for (RestockAlert alert : activeAlerts) {
            System.out.println("- " + alert.getMedicationName() + " | Stock: " + alert.getCurrentStock()
                    + " | Min Threshold: " + alert.getThreshold());
        }
        System.out.println("Use 'alerts' to view details or 'ack-alert ALERT_INDEX' to acknowledge.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints an allergy conflict warning when a medication is about to be dispensed
     * to a customer with a known allergy to that medication.
     * The dispense operation should be aborted after this message is printed.
     *
     * @param customerName The name of the customer with the allergy.
     * @param allergen     The matched allergen keyword that triggered the warning.
     */
    public void printAllergyWarning(String customerName, String allergen) {
        System.out.println(DIVIDER);
        System.out.println("WARNING: Allergy conflict detected!");
        System.out.println("Customer \"" + customerName + "\" has a recorded allergy to \"" + allergen + "\".");
        System.out.println("Dispense aborted. Please verify with a pharmacist before proceeding.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints a confirmation message after a customer record has been successfully updated.
     *
     * @param customer The updated {@link Customer}.
     */
    public void printUpdatedCustomerMessage(Customer customer) {
        printToScreen(
                DIVIDER,
                "Customer updated: " + customer.toString(),
                DIVIDER
        );
    }

    /**
     * Displays the list of customers matching a name search keyword.
     *
     * @param keyword The keyword that was searched.
     * @param matches The list of matching customers to display.
     */
    public void printFindCustomerResults(String keyword, ArrayList<Customer> matches) {
        printMessage("Customers matching \"" + keyword + "\" (" + matches.size() + " found):");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". " + matches.get(i).toString());
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays a numbered list of all registered customers.
     *
     * @param customerList The list of customers to display.
     */
    public void printCustomerList(CustomerList customerList) {
        System.out.println(DIVIDER);
        if (customerList.size() == 0) {
            System.out.println("No customers registered yet.");
        } else {
            System.out.println("PharmaTracker Customers:");
            for (int i = 0; i < customerList.size(); i++) {
                Customer customer = customerList.getCustomer(i);
                System.out.println((i + 1) + ". " + customer.toString());
            }
            System.out.println("Total Customers: " + customerList.size() + ".");
        }
        System.out.println(DIVIDER);
    }

    public void printUpdatedMedicationMessage(Medication med, ArrayList<String> changes) {
        String changeString = String.join(" | ", changes);
        printToScreen(
                DIVIDER,
                "Medication updated: " + med.getName() + " | " + changeString,
                DIVIDER
        );
    }

    /**
     * Prints the comprehensive help menu showing all available commands and their formats.
     */
    public void printHelpMessage() {
        System.out.println("Here are the possible features of PharmaTracker:");
        System.out.println("--- Medication Commands ---");
        System.out.println(" 1. Add Medication           (add /n NAME /d DOSAGE /q QUANTITY /e EXPIRY /t TAG)");
        System.out.println(" 2. Delete Medication        (delete INDEX)");
        System.out.println(" 3. Dispense Medication      (dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX])");
        System.out.println(" 4. List Inventory           (list)");
        System.out.println(" 5. Find / Search Medication (find KEYWORD)");
        System.out.println(" 6. View Medication Details  (view INDEX)");
        System.out.println(" 7. Sort by Expiry Date      (sort)");
        System.out.println(" 8. Print Description Label  (label INDEX)");
        System.out.println(" 9. Update Medication        "
                + "(update INDEX /n NAME /d DOSAGE /q QUANTITY /e EXPIRY /t TAG)");
        System.out.println("10. Restock Medication       (restock INDEX /q QUANTITY)");
        System.out.println("11. Low Stock Alert          (lowstock | lowstock /threshold NUMBER)");
        System.out.println("12. Expiring Medications     (expiring | expiring /days NUMBER)");
        System.out.println("13. Set Min Threshold        (set-threshold INDEX /threshold NUMBER)");
        System.out.println("14. View Active Alerts       (alerts)");
        System.out.println("15. Acknowledge Alert        (ack-alert ALERT_INDEX)");
        System.out.println("16. View Alert History       (alert-history)");
        System.out.println("17. Daily Dispense Log       (dispenselog | dispenselog /date YYYY-MM-DD)");
        System.out.println("--- Customer Commands ---");
        System.out.println("18. Add Customer             "
            + "(add-customer /id CUSTOMER_ID /n NAME /p PHONE [/addr ADDRESS] "
            + "[/allergy ALLERGY1,ALLERGY2,...])");
        System.out.println("19. Delete Customer          (delete-customer INDEX)");
        System.out.println("20. Update Customer          "
            + "(update-customer INDEX [/n NAME] [/p PHONE] [/addr ADDRESS] "
            + "[/allergy ALLERGY1,ALLERGY2,...])");
        System.out.println("21. List Customers           (list-customers)");
        System.out.println("22. Find Customer            (find-customer KEYWORD)");
        System.out.println("23. View Customer            (view-customer INDEX)");
        System.out.println("--- Authentication ---");
        System.out.println("24. Register                 (register USERNAME /p PASSWORD)");
        System.out.println("25. Login                    (login USERNAME /p PASSWORD)");
        System.out.println("26. Logout                   (logout)");
        System.out.println("--- General ---");
        System.out.println("27. Viewing Help             (help)");
        System.out.println("28. Exiting the Program      (exit)");
    }

    /**
     * Prints a formatted daily dispense summary for the given date.
     *
     * @param date    The date of the summary.
     * @param records The list of dispense records for that date.
     */
    public void printDispenseSummary(LocalDate date, List<DispenseRecord> records) {
        System.out.println(DIVIDER);
        System.out.println("Dispense Log for " + date);
        System.out.println(DIVIDER);
        if (records.isEmpty()) {
            System.out.println("No dispense events recorded for " + date + ".");
        } else {
            int totalUnits = 0;
            for (int i = 0; i < records.size(); i++) {
                System.out.println((i + 1) + ". " + records.get(i));
                totalUnits += records.get(i).getQuantity();
            }
            System.out.println(DIVIDER);
            System.out.println("Total: " + records.size() + " dispense event(s), "
                    + totalUnits + " unit(s) dispensed.");
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints the exit message when the application is shutting down.
     */
    public void printExitMessage() {
        System.out.println("Exiting Application");
        System.out.println("Stay Healthy!");
    }

    /**
     * Prints an error message to the console.
     *
     * @param message The error message to display.
     */
    public void printError(String message) {
        System.out.println(message);
    }

}
