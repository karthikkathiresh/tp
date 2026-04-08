package seedu.pharmatracker.parser;

import java.util.ArrayList;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

/**
 * Parses input arguments and creates a new AddCommand object.
 * This class is responsible for extracting the necessary medication details
 * from the user's input string to facilitate the addition of a new medication
 * into the inventory.
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given arguments string in the context of the add medication command
     * and returns an AddCommand object for execution.
     *
     * @param description The string containing the arguments provided by the user.
     * @return An AddCommand object instantiated with the extracted medication details.
     * @throws PharmaTrackerException If the user input is invalid.
     */
    @Override
    public AddCommand parse(String description) throws PharmaTrackerException {
        String name = MedicationParserUtil.extractName(description);
        String dosage = MedicationParserUtil.extractDosage(description);
        int quantity = MedicationParserUtil.extractQuantity(description);
        String expiryDate = MedicationParserUtil.extractExpiryDate(description);
        String tag = ParserUtil.extractFlag(description, MedicationParserUtil.FLAG_TAG);
        String dosageForm = ParserUtil.extractFlag(description, MedicationParserUtil.FLAG_DOSAGE_FORM);
        String manufacturer = ParserUtil.extractFlag(description, MedicationParserUtil.FLAG_MANUFACTURER);
        String directions = ParserUtil.extractFlag(description, MedicationParserUtil.FLAG_DIRECTION);
        String frequency = ParserUtil.extractFlag(description, MedicationParserUtil.FLAG_FREQUENCY);
        String route = ParserUtil.extractFlag(description, MedicationParserUtil.FLAG_ROUTE);
        String maxDailyDose = ParserUtil.extractFlag(description, MedicationParserUtil.FLAG_MAX_DOSAGE);
        ArrayList<String> warnings = MedicationParserUtil.extractWarnings(description);

        return new AddCommand(name, dosage, quantity, expiryDate, tag,
                dosageForm, manufacturer, directions, frequency,
                route, maxDailyDose, warnings);
    }

}
