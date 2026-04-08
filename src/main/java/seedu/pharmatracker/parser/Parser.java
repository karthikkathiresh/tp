package seedu.pharmatracker.parser;

import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

/**
 * Represents a parser that is able to parse user input into a {@code Command} of type {@code T}.
 * This interface establishes the contract for all specific command parsers in the application.
 *
 * @param <T> The specific type of Command that this parser will return upon successful parsing.
 */
public interface Parser<T extends Command> {

    /**
     * Parses the given user input string and returns an executable command.
     *
     * @param args The string containing the arguments provided by the user.
     * @return A command of type {@code T} ready for execution.
     * @throws PharmaTrackerException If the user input is invalid.
     */
    T parse(String args) throws PharmaTrackerException;
}
