package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

/**
 * Updates one or more fields of an existing medication record in the inventory.
 * Only the fields provided will be changed. All others remain unchanged.
 */
public class UpdateCommand extends Command {

    public static final String COMMAND_WORD = "update";

    private static final Logger logger = Logger.getLogger(UpdateCommand.class.getName());

    private final int index;
    private final String name;
    private final String dosage;
    private final Integer quantity;
    private final String expiryDate;
    private final String tag;
    private final String dosageForm;
    private final String manufacturer;
    private final String directions;
    private final String frequency;
    private final String route;
    private final String maxDailyDose;
    private final ArrayList<String> warnings;

    public UpdateCommand(int index, String name, String dosage, Integer quantity, String expiryDate, String tag,
                         String dosageForm, String manufacturer, String directions, String frequency, String route,
                         String maxDailyDose, ArrayList<String> warnings) {
        this.index = index;
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

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert inventory != null : "Inventory cannot be null in UpdateCommand execution.";
        assert ui != null : "Ui cannot be null in UpdateCommand execution.";

        logger.log(Level.INFO, "Starting execution of UpdateCommand for index:" + index);

        if (inventory.getMedications().isEmpty()) {
            System.out.println("inventory is empty.");
            return;
        }

        if (index < 1 || index > inventory.getMedications().size()) {
            System.out.println("Invalid index. Please enter a number between 1 and "
                    + inventory.getMedications().size() + ".");
            return;
        }

        Medication med = inventory.getMedication(index - 1);
        ArrayList<String> changes = new ArrayList<>();

        if (name != null) {
            med.setName(name);
            changes.add("Name updated to " + name);
        }

        if (dosage != null) {
            med.setDosage(dosage);
            changes.add("Dosage updated to " + dosage);
        }

        if (quantity != null) {
            med.setQuantity(quantity);
            changes.add("Quantity updated to " + quantity);
        }

        if (expiryDate != null) {
            med.setExpiryDate(expiryDate);
            changes.add("Expiry updated to " + expiryDate);
        }

        if (tag != null) {
            med.setTag(tag);
            changes.add("Tag updated to " + tag);
        }

        if (dosageForm != null) {
            med.setDosageForm(dosageForm);
            changes.add("Dosage Form updated to " + dosageForm);
        }

        if (manufacturer != null) {
            med.setManufacturer(manufacturer);
            changes.add("Manufacturer updated to " + manufacturer);
        }

        if (directions != null) {
            med.setDirections(directions);
            changes.add("Directions updated to " + directions);
        }

        if (frequency != null) {
            med.setFrequency(frequency);
            changes.add("Frequency updated to " + frequency);
        }

        if (route != null) {
            med.setRoute(route);
            changes.add("Route updated to " + route);
        }

        if (maxDailyDose != null) {
            med.setMaxDailyDose(maxDailyDose);
            changes.add("Max Daily Dose updated to " + maxDailyDose);
        }

        if (warnings != null && !warnings.isEmpty()) {
            med.getWarnings().clear(); // Clear old warnings and replace with new ones
            for (String warning : warnings) {
                med.addWarning(warning);
            }
            changes.add("Warnings updated");
        }

        ui.printUpdatedMedicationMessage(med, changes);
    }
}
