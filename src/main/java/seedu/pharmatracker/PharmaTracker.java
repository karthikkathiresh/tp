package seedu.pharmatracker;

import static seedu.pharmatracker.parser.PharmaTrackerParser.parse;
import java.util.ArrayList;

import seedu.pharmatracker.alert.RestockAlert;
import seedu.pharmatracker.alert.RestockAlertService;
import seedu.pharmatracker.auth.AuthService;
import seedu.pharmatracker.logger.LoggerSetup;

import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.LogoutCommand;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.core.AppServices;
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
    private AuthService authService;
    private RestockAlertService restockAlertService;

    /**
     * Constructs a {@code PharmaTracker} application instance.
     * Initializes the user interface and storage handlers, and attempts to load
     * any existing inventory data from the local storage file.
     */
    public PharmaTracker() {
        ui = new Ui();
        storage = new Storage();
        inventory = storage.load();
        inventory.setDispenseLog(storage.loadDispenseLog());
        customerList = storage.loadCustomers();

        authService = new AuthService(storage.loadUsers(), storage.loadSession());
        restockAlertService = new RestockAlertService(storage.loadAlertHistory());
        AppServices.initialize(authService, restockAlertService);
        restockAlertService.evaluateInventory(inventory);
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
        if (authService.isAuthenticated()) {
            ui.printMessage("Restored session for user: " + authService.getCurrentUsername());
        } else {
            ui.printMessage("Please login or register to use PharmaTracker features.");
        }

        while (true) {
            String fullCommand = ui.readCommand();
            try {
                Command c = parse(fullCommand);
                if (c != null) {
                    if (c.requiresAuthentication() && !authService.isAuthenticated()) {
                        ui.printMessage("Authentication required. Use: register USERNAME /p PASSWORD or "
                                + "login USERNAME /p PASSWORD");
                        continue;
                    }

                    c.execute(inventory, ui, customerList);
                    if (!(c instanceof LogoutCommand)) {
                        restockAlertService.evaluateInventory(inventory);
                    }

                    storage.save(inventory);
                    storage.saveCustomers(customerList);
                    storage.saveUsers(authService.getUsersSnapshot());
                    storage.saveSession(authService.getCurrentUsername());
                    storage.saveAlertHistory(restockAlertService.getAlertHistory());

                    if (authService.isAuthenticated()) {
                        ArrayList<RestockAlert> activeAlerts = restockAlertService.getActiveAlerts();
                        if (!activeAlerts.isEmpty()) {
                            ui.printAutoRestockAlertSummary(activeAlerts);
                        }
                    }
                    storage.saveDispenseLog(inventory.getDispenseLog());
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
