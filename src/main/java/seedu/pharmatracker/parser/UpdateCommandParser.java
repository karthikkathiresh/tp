package seedu.pharmatracker.parser;

import java.util.ArrayList;

import seedu.pharmatracker.command.UpdateCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class UpdateCommandParser implements Parser<UpdateCommand> {
    @Override
    public UpdateCommand parse(String description) throws PharmaTrackerException {
        if (description.isEmpty()) {
            throw new PharmaTrackerException(
                    "Invalid format! Use: update INDEX [/n NAME] [/d DOSAGE] [/q QUANTITY] [/e EXPIRY]...");
        }
        try {
            String[] updateParts = description.trim().split("\\s+", 2);
            int updateIndex = Integer.parseInt(updateParts[0]);
            String updateArgs = (updateParts.length > 1) ? updateParts[1] : "";

            String uName = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_NAME);
            String uDosage = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_DOSAGE);
            Integer uQuantity = MedicationParserUtil.extractOptionalQuantity(updateArgs);
            String uExpiry = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_EXPIRY_DATE);
            String uTag = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_TAG);
            String uDosageForm = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_DOSAGE_FORM);
            String uManufacturer = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_MANUFACTURER);
            String uDirections = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_DIRECTION);
            String uFrequency = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_FREQUENCY);
            String uRoute = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_ROUTE);
            String uMaxDailyDose = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_MAX_DOSAGE);

            ArrayList<String> uWarnings = MedicationParserUtil.extractWarnings(updateArgs);

            return new UpdateCommand(updateIndex, uName, uDosage, uQuantity, uExpiry, uTag,
                    uDosageForm, uManufacturer, uDirections, uFrequency, uRoute, uMaxDailyDose, uWarnings);
        } catch (NumberFormatException e) {
            throw new PharmaTrackerException(
                    "Invalid index! The first argument must be a valid number.");
        }
    }
}
