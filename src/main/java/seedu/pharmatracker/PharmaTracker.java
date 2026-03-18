package seedu.pharmatracker;

import static seedu.pharmatracker.parser.Parser.parse;

import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.storage.Storage;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.ui.Ui;

public class PharmaTracker {
    private Ui ui;
    private Inventory inventory;
    private Storage storage;

    public PharmaTracker() {
        ui = new Ui();
        storage = new Storage();
        inventory = storage.load();
    }

    public void run() throws PharmaTrackerException {
        assert ui != null : "UI should not be null";
        assert inventory != null : "Inventory should not be null";
        ui.printWelcomeMessage();

        while (true) {
            String fullCommand = ui.readCommand();
            try {
                Command c = parse(fullCommand);
                if (c != null) {
                    c.execute(inventory, ui);
                    storage.save(inventory);
                }
            } catch (PharmaTrackerException e) {
                ui.printMessage(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws PharmaTrackerException {
        new PharmaTracker().run();
    }
}
