package seedu.pharmatracker;

import static seedu.pharmatracker.parser.Parser.parse;

import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

public class PharmaTracker {
    // private static final Logger logger = Logger.getLogger(PharmaTracker.class.getName());

    private Ui ui;
    private Inventory inventory;

    public PharmaTracker() {
        ui = new Ui();
        inventory = new Inventory();
    }

    public void run() {
        assert ui != null : "UI should not be null";
        assert inventory != null : "Inventory should not be null";
        // logger.log(Level.INFO, "PharmaTracker starting up");
        ui.printWelcomeMessage();

        while (true) {
            String fullCommand = ui.readCommand();
            Command c = parse(fullCommand);
            if (c != null) {
                c.execute(inventory);
            }
            // logger.log(Level.INFO, "Command received: " + fullCommand);
        }
    }

    public static void main(String[] args) {
        new PharmaTracker().run();
    }
}
