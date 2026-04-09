package seedu.pharmatracker.command;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.DispenseRecord;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to display the dispense log for a given date.
 * Defaults to today's date when no date is specified.
 * Usage: {@code dispenselog} or {@code dispenselog /date YYYY-MM-DD}
 */
public class DispenseSummaryCommand extends Command {

    public static final String COMMAND_WORD = "dispenselog";

    private static final Logger logger = Logger.getLogger(DispenseSummaryCommand.class.getName());

    private final LocalDate date;

    /**
     * Constructs a DispenseSummaryCommand that shows today's log.
     */
    public DispenseSummaryCommand() {
        this.date = LocalDate.now();
    }

    /**
     * Constructs a DispenseSummaryCommand for a specific date.
     *
     * @param date The date whose dispense records to display.
     */
    public DispenseSummaryCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * Executes the command by printing all dispense records for the target date.
     *
     * @param inventory    The current medication inventory (contains the dispense log).
     * @param ui           The user interface for displaying messages.
     * @param customerList The list of registered customers (unused).
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory must not be null";
        assert ui != null : "Ui must not be null";

        logger.log(Level.INFO, "Executing DispenseSummaryCommand for date: {0}", date);

        List<DispenseRecord> records = inventory.getDispenseLog().getRecordsByDate(date);
        ui.printDispenseSummary(date, records);

        logger.log(Level.INFO, "DispenseSummaryCommand completed. Records shown: {0}", records.size());
    }
}
