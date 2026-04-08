package seedu.pharmatracker.parser;

import java.util.ArrayList;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class AddCommandParser implements NewParser<AddCommand> {

    @Override
    public AddCommand parse(String args) throws PharmaTrackerException {
        String name = MedicationParserUtil.extractName(args);
        String dosage = MedicationParserUtil.extractDosage(args);
        int quantity = MedicationParserUtil.extractQuantity(args);
        String expiryDate = MedicationParserUtil.extractExpiryDate(args);
        String tag = ParserUtil.extractFlag(args, MedicationParserUtil.FLAG_TAG);
        String dosageForm = ParserUtil.extractFlag(args, MedicationParserUtil.FLAG_DOSAGE_FORM);
        String manufacturer = ParserUtil.extractFlag(args, MedicationParserUtil.FLAG_MANUFACTURER);
        String directions = ParserUtil.extractFlag(args, MedicationParserUtil.FLAG_DIRECTION);
        String frequency = ParserUtil.extractFlag(args, MedicationParserUtil.FLAG_FREQUENCY);
        String route = ParserUtil.extractFlag(args, MedicationParserUtil.FLAG_ROUTE);
        String maxDailyDose = ParserUtil.extractFlag(args, MedicationParserUtil.FLAG_MAX_DOSAGE);
        ArrayList<String> warnings = MedicationParserUtil.extractWarnings(args);

        return new AddCommand(name, dosage, quantity, expiryDate, tag,
                dosageForm, manufacturer, directions, frequency,
                route, maxDailyDose, warnings);
    }

}
