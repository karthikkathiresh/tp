package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

public abstract class Command {

    public abstract void execute(Inventory inventory, Ui ui);

}
