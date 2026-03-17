package seedu.pharmatracker.command;

import java.util.ArrayList;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

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

    // For reference, the format of the description will be
    // /n NAME /d DOSAGE /q QUANTITY /e EXPIRY /t TAG
    // Optional: /df DOSAGE_FORM /mfr MANUFACTURER /dir DIRECTIONS
    //           /freq FREQUENCY /route ROUTE /max MAX_DOSE /warn WARNING

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

    @Override
    public void execute(Inventory inventory, Ui ui) {
        Medication med = new Medication(name, dosage, quantity, expiryDate, tag);
        if (!dosageForm.isEmpty()) {
            med.setDosageForm(dosageForm);
        }
        if (!manufacturer.isEmpty()) {
            med.setManufacturer(manufacturer);
        }
        if (!directions.isEmpty()) {
            med.setDirections(directions);
        }
        if (!frequency.isEmpty()) {
            med.setFrequency(frequency);
        }
        if (!route.isEmpty()) {
            med.setRoute(route);
        }
        if (!maxDailyDose.isEmpty()) {
            med.setMaxDailyDose(maxDailyDose);
        }
        for (String warning : warnings) {
            med.addWarning(warning);
        }
        inventory.addMedication(med);
        ui.printAddedMessage(med, inventory);
    }

}
