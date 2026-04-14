package seedu.pharmatracker.parser;

import java.util.ArrayList;

import seedu.pharmatracker.command.UpdateCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

/**
 * Parses input arguments and creates a new UpdateCommand object.
 * This class is responsible for extracting the target medication's index
 * and the specific optional attributes the user wishes to modify from the input string.
 */
public class UpdateCommandParser implements Parser<UpdateCommand> {

    /**
     * Parses the given arguments string in the context of the update medication command
     * and returns an UpdateCommand object for execution.
     *
     * @param description The string provided by the user expecting a target index followed by optional update flags.
     * @return An UpdateCommand object instantiated with the target index and extracted updated details.
     * @throws PharmaTrackerException If the input string is invalid.
     */
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

            if (updateArgs.trim().isEmpty()) {
                throw new PharmaTrackerException("No fields provided to update!");
            }

            String uName = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_NAME);
            String uDosage = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_DOSAGE);
            Integer uQuantity = MedicationParserUtil.extractOptionalQuantity(updateArgs);
            String uExpiry = MedicationParserUtil.extractOptionalExpiryDate(updateArgs);
            String uTag = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_TAG);
            String uDosageForm = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_DOSAGE_FORM);
            String uManufacturer = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_MANUFACTURER);
            String uDirections = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_DIRECTION);
            String uFrequency = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_FREQUENCY);
            String uRoute = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_ROUTE);
            String uMaxDailyDose = ParserUtil.extractOptionalFlag(updateArgs, MedicationParserUtil.FLAG_MAX_DOSAGE);

            ArrayList<String> uWarnings = updateArgs.contains(MedicationParserUtil.FLAG_WARNINGS)
                    ? MedicationParserUtil.extractWarnings(updateArgs)
                    : null;

            return new UpdateCommand(updateIndex, uName, uDosage, uQuantity, uExpiry, uTag,
                    uDosageForm, uManufacturer, uDirections, uFrequency, uRoute, uMaxDailyDose, uWarnings);
        } catch (NumberFormatException e) {
            throw new PharmaTrackerException(
                    "Invalid index! The first argument must be a valid number.");
        }
    }
}
