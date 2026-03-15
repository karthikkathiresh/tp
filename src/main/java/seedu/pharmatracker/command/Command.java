package seedu.pharmatracker.command;

import seedu.pharmatracker.data.Inventory;

public abstract class Command {

    public abstract void execute(Inventory inventory);

}
