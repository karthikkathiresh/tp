package seedu.pharmatracker.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

/**
 * Represents a command to list all medications that have already expired
 * and all medications expiring within a specified number of days.
 * Defaults to 30 days if no custom window is provided.
 * Medications are separated into two categories: already expired and expiring soon.
 */
public class ExpiringCommand extends Command {

    public static final String COMMAND_WORD = "expiring";
    private static final int DEFAULT_DAYS = 30;
    private static final Logger logger = Logger.getLogger(ExpiringCommand.class.getName());

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int days;

    /**
     * Constructs an ExpiringCommand with the default 30-day window.
     */
    public ExpiringCommand() {
        this.days = DEFAULT_DAYS;
    }

    /**
     * Constructs an ExpiringCommand with a custom expiry window.
     *
     * @param days The number of days to check for expiring medications.
     */
    public ExpiringCommand(int days) {
        this.days = days;
    }

    /**
     * Executes the expiring command by filtering medications into two groups:
     * those that have already expired and those expiring within the specified
     * window from today's date. Results are displayed separately via the Ui.
     *
     * @param inventory The current medication inventory.
     * @param ui        The user interface for displaying results.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        logger.log(Level.INFO, "Executing ExpiringCommand with window: " + days + " days");

        assert inventory != null : "Inventory cannot be null in ExpiringCommand";
        assert ui != null : "Ui cannot be null in ExpiringCommand";

        ArrayList<Medication> medicationList = inventory.getMedications();
        ArrayList<Medication> expiredMeds = new ArrayList<>();
        ArrayList<Medication> expiringMeds = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusDays(days);

        for (Medication med : medicationList) {
            LocalDate expiryDate = null;
            String expiry = med.getExpiryDate();
            if (expiry == null) {
                continue;
            }
            expiry = expiry.trim();
            try {
                expiryDate = LocalDate.parse(expiry, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e1) {
                try {
                    expiryDate = LocalDate.parse(expiry, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException e2) {
                    logger.log(Level.WARNING, "Could not parse expiry date for: "
                            + med.getName() + " (" + expiry + ")");
                    continue;
                }
            }
            if (expiryDate.isBefore(today)) {
                expiredMeds.add(med);
            } else if (!expiryDate.isAfter(cutoff)) {
                expiringMeds.add(med);
            }
        }

        ui.showExpiringMedications(expiredMeds, expiringMeds, days);

        logger.log(Level.INFO, "ExpiringCommand complete. Found " + expiringMeds.size()
                + " medication(s) expiring within " + days + " days.");
    }
}
