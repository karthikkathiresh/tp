package seedu.pharmatracker.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

public class ExportMedicationCommand extends Command {

    public static final String COMMAND_WORD = "export-medication";
    private static final String EXPORT_FILE_PATH = "data/medication_export.csv";
    private static final String CSV_HEADERS = "Name,Dosage,Quantity,Expiry Date,Tag,Dosage Form,Manufacturer," +
            "Directions,Frequency,Route,Max Daily Dose,Warnings\n";

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        if (inventory.getMedications().isEmpty()) {
            System.out.println("The inventory is empty. Nothing to export!");
        }

        try {
            File file = new File(EXPORT_FILE_PATH);
            file.getParentFile().mkdirs();

            FileWriter fw = new FileWriter(EXPORT_FILE_PATH, false);

            fw.write(CSV_HEADERS);

            for (Medication med: inventory.getMedications()) {
                fw.write(med.toCsvString() + "\n");
            }

            fw.close();
            System.out.println("Success! Medication inventory has been exported to: " + EXPORT_FILE_PATH);
        } catch (IOException e) {
            System.out.println("An error occured while exporting the file: " + e.getMessage());
        }
    }
}
