package seedu.pharmatracker.parser;

import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class ParserUtil {
    /**
     * Finds the index of the next flag appearing in the description string after a specified index.
     * This is used to determine the end bound of a flag's associated value.
     *
     * @param description The raw string containing command arguments.
     * @param afterIndex  The index to start searching from.
     * @return The index of the next occurring flag, or the length of the string if no more flags exist.
     * @throws PharmaTrackerException If the search index provided is invalid.
     */
    static int findNextFlagIndex(String description, int afterIndex) throws PharmaTrackerException {
        if (afterIndex < 0 || afterIndex > description.length()) {
            throw new PharmaTrackerException("Error parsing command flags: Invalid search index.");
        }

        int earliest = description.length();
        for (String flag : MedicationParserUtil.ALL_FLAGS) {
            int idx = description.indexOf(flag, afterIndex);
            if (idx != -1 && idx < earliest) {
                earliest = idx;
            }
        }
        return earliest;
    }

    /**
     * Extracts the string value associated with a generic optional flag.
     *
     * @param description The raw string containing command arguments.
     * @param flag        The specific flag to search for.
     * @return The extracted string value, or an empty string if the flag is not present.
     * @throws PharmaTrackerException If the flag is present but has no accompanying value.
     */
    static String extractFlag(String description, String flag) throws PharmaTrackerException {
        int flagIndex = description.indexOf(flag);
        if (flagIndex == -1) {
            return "";
        }

        int valueStart = flagIndex + flag.length();
        if (valueStart >= description.length()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }

        int valueEnd = findNextFlagIndex(description, valueStart);
        String extractedValue = description.substring(valueStart, valueEnd).trim();

        if (extractedValue.isEmpty()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }

        return extractedValue;
    }

    /**
     * Extracts the string value associated with an optional flag.
     * Unlike extractFlag, this returns null if the flag is not present.
     *
     * @param description The raw string containing command arguments.
     * @param flag        The specific flag to search for.
     * @return The extracted string value, or null if the flag is not present.
     * @throws PharmaTrackerException If the flag is present but has no accompanying value.
     */
    static String extractOptionalFlag(String description, String flag) throws PharmaTrackerException {
        int flagIndex = description.indexOf(flag);
        if (flagIndex == -1) {
            return null;
        }

        int valueStart = flagIndex + flag.length();
        if (valueStart >= description.length()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }

        int valueEnd = findNextFlagIndex(description, valueStart);
        String extractedValue = description.substring(valueStart, valueEnd).trim();

        if (extractedValue.isEmpty()) {
            throw new PharmaTrackerException("Value for '" + flag + "' cannot be empty!");
        }

        return extractedValue;
    }

}
