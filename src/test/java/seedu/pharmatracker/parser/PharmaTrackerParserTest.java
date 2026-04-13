package seedu.pharmatracker.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.pharmatracker.command.AcknowledgeAlertCommand;
import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.FindCommand;
import seedu.pharmatracker.command.LoginCommand;
import seedu.pharmatracker.command.RegisterCommand;
import seedu.pharmatracker.command.SetThresholdCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class PharmaTrackerParserTest {


    @Test
    public void parser_findCommand_returnsCorrectCommandType() throws PharmaTrackerException {
        Command c = PharmaTrackerParser.parse("find Paracetamol");
        assertTrue(c instanceof FindCommand);
    }

    @Test
    public void parser_findCommandNoKeyword_throwsException() {
        assertThrows(PharmaTrackerException.class, () -> PharmaTrackerParser.parse("find"));
    }

    @Test
    public void parser_registerCommand_returnsCorrectCommandType() throws PharmaTrackerException {
        Command c = PharmaTrackerParser.parse("register alice /p Strong1!");
        assertTrue(c instanceof RegisterCommand);
    }

    @Test
    public void parser_loginCommand_returnsCorrectCommandType() throws PharmaTrackerException {
        Command c = PharmaTrackerParser.parse("login alice /p Strong1!");
        assertTrue(c instanceof LoginCommand);
    }

    @Test
    public void parser_setThresholdCommand_returnsCorrectCommandType() throws PharmaTrackerException {
        Command c = PharmaTrackerParser.parse("set-threshold 1 /threshold 25");
        assertTrue(c instanceof SetThresholdCommand);
    }

    @Test
    public void parser_ackAlertCommand_returnsCorrectCommandType() throws PharmaTrackerException {
        Command c = PharmaTrackerParser.parse("ack-alert 2");
        assertTrue(c instanceof AcknowledgeAlertCommand);
    }

    @Test
    public void parser_invalidRegisterFormat_throwsException() {
        assertThrows(PharmaTrackerException.class, () -> PharmaTrackerParser.parse("register alice"));
    }
}
