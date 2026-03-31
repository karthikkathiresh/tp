package seedu.pharmatracker;

import static seedu.pharmatracker.parser.Parser.parse;
import seedu.pharmatracker.logger.LoggerSetup;

import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.storage.Storage;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.ui.Ui;

/**
 * The main entry point for the PharmaTracker application.
 * Initializes the core components (UI, Storage and Inventory) and manages
 * the main execution loop of the program.
 */
public class PharmaTracker {
    private Ui ui;
    private Inventory inventory;
    private Storage storage;
    private CustomerList customerList;

    /**
     * Constructs a {@code PharmaTracker} application instance.
     * Initializes the user interface and storage handlers, and attempts to load
     * any existing inventory data from the local storage file.
     */
    public PharmaTracker() {
        ui = new Ui();
        storage = new Storage();
        inventory = storage.load();
        customerList = new CustomerList();
    }

    /**
     * Runs the main execution loop of the application.
     * Continuously reads user input, parses it into executable commands, executes the
     * commands, and saves the updated inventory state to storage. Catches and displays
     * any application-specific exceptions to the user.
     *
     * @throws PharmaTrackerException If a critical, unrecoverable error occurs during execution.
     */
    public void run() throws PharmaTrackerException {
        assert ui != null : "UI should not be null";
        assert inventory != null : "Inventory should not be null";
        ui.printWelcomeMessage();

        while (true) {
            String fullCommand = ui.readCommand();
            try {
                Command c = parse(fullCommand);
                if (c != null) {
                    c.execute(inventory, ui, customerList);
                    storage.save(inventory);
                }
            } catch (PharmaTrackerException e) {
                ui.printMessage(e.getMessage());
            }
        }
    }

    /**
     * The main method that launches the PharmaTracker application.
     *
     * @param args Command line arguments.
     *
     * @throws PharmaTrackerException If a fatal error occurs during application run.
     */
    public static void main(String[] args) throws PharmaTrackerException {
        LoggerSetup.init();
        new PharmaTracker().run();
    }
}
