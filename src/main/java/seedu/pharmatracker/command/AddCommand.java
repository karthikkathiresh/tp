package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to add a new medication to the inventory.
 * Encapsulates all necessary details required to construct a Medication object.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    private static final Logger logger = Logger.getLogger(AddCommand.class.getName());

    private final String name;
    private final String dosage;
    private final int quantity;
    private final String expiryDate;
    private final String tag;

    private final String dosageForm;
    private final String manufacturer;
    private final String directions;
    private final String frequency;
    private final String route;
    private final String maxDailyDose;
    private final ArrayList<String> warnings;

    /**
     * Constructs an AddCommand with the specified medication details.
     *
     * @param name         The name of the medication.
     * @param dosage       The strength or dosage of the medication (e.g. 500mg).
     * @param quantity     The number of units being added to the inventory.
     * @param expiryDate   The expiration date of the medication.
     * @param tag          The category or tag associated with the medication.
     * @param dosageForm   The physical form of the medication (e.g. Tablet, Capsule).
     * @param manufacturer The manufacturer of the medication.
     * @param directions   Instructions for taking the medication.
     * @param frequency    How often the medication should be taken.
     * @param route        The route of administration (e.g. Oral).
     * @param maxDailyDose The maximum allowed dosage per day.
     * @param warnings     A list of precautions or warnings associated with the medication.
     */
    public AddCommand(String name, String dosage, int quantity, String expiryDate, String tag,
                      String dosageForm, String manufacturer, String directions,
                      String frequency, String route, String maxDailyDose,
                      ArrayList<String> warnings) {

        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.tag = tag;
        this.dosageForm = dosageForm;
        this.manufacturer = manufacturer;
        this.directions = directions;
        this.frequency = frequency;
        this.route = route;
        this.maxDailyDose = maxDailyDose;
        this.warnings = warnings;
    }

    /**
     * Executes the command by creating a new Medication object with the provided results
     * and adding it to the inventory. Also prints a success message to the user.
     *
     * @param inventory The current inventory where the medication will be added.
     * @param ui        The user interface to display the success message.
     */
    @Override
    public void execute(Inventory inventory, Ui ui) {
        assert inventory != null : "Inventory cannot be null in AddCommand execution.";
        assert ui != null : "Ui canot be null in AddCommand execution.";
        logger.log(Level.INFO, "Starting execution of AddCommand for medication: " + name);
        Medication med = new Medication(name, dosage, quantity, expiryDate, tag);

        med.setDosageForm(dosageForm);
        med.setManufacturer(manufacturer);
        med.setDirections(directions);
        med.setFrequency(frequency);
        med.setRoute(route);
        med.setMaxDailyDose(maxDailyDose);

        for (String warning : warnings) {
            med.addWarning(warning);
        }

        inventory.addMedication(med);
        ui.printAddedMessage(med, inventory);
        logger.log(Level.INFO, "Successfully executed AddCommand.");
    }
}
