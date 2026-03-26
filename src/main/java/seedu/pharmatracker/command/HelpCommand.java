package seedu.pharmatracker.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";
    private static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory should not be null";
        assert ui != null : "Ui should not be null";
        logger.log(Level.INFO, "Executing HelpCommand");
        System.out.println("Here are the possible features of the chatbot:");
        System.out.println("1. Add Medication (add /n NAME /d DOSAGE /q QUANTITY /e EXPIRY /t TAG)");
        System.out.println("2. Delete Medication (delete INDEX)");
        System.out.println("3. Dispense Medication (dispense INDEX q/QUANTITY)");
        System.out.println("4. List Inventory (list)");
        System.out.println("5. Find / Search Medication (find KEYWORD)");
        System.out.println("6. View Medication Details (view INDEX)");
        System.out.println("7. Sort by Expiry Date (sort)");
        System.out.println("8. Print Description Label (label INDEX)");
        System.out.println("9. Viewing Help (help)");
        System.out.println("10. Exiting the Program (exit)");
        logger.log(Level.INFO, "HelpCommand executed successfully");
    }
}
