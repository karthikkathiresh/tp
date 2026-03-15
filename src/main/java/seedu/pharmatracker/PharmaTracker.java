package seedu.pharmatracker;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.parser.Parser;
import seedu.pharmatracker.ui.Ui;

public class PharmaTracker {

    private Ui ui;
    private Parser parser;
    private Inventory inventory;

    public PharmaTracker() {
        ui = new Ui();
        parser = new Parser();
        inventory = new Inventory();
    }

    public void run() {
        ui.printWelcomeMessage();
        while (true) {
            String fullCommand = ui.readCommand();
            parser.parseCommand(fullCommand);

        }
    }

    /**
     * Main entry-point for the PharmaTracker application.
     */
    public static void main(String[] args) {
        new PharmaTracker().run();
    }
}
