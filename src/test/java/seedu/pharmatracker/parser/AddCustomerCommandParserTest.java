package seedu.pharmatracker.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import seedu.pharmatracker.command.AddCustomerCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class AddCustomerCommandParserTest {

    private AddCustomerCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new AddCustomerCommandParser();
    }

    @Test
    public void parse_validMandatoryFieldsOnly_returnsAddCustomerCommand() {
        String input = "/id C001 /n John Doe /p 91234567";

        assertDoesNotThrow(() -> {
            AddCustomerCommand command = parser.parse(input);
            assertNotNull(command);
        });
    }

    @Test
    public void parse_allValidFieldsPresent_returnsAddCustomerCommand() {
        String input = "/id C002 /n Jane Smith /p 87654321 /addr 123 Clementi Road /allergy Peanuts, Penicillin";

        assertDoesNotThrow(() -> {
            AddCustomerCommand command = parser.parse(input);
            assertNotNull(command);
        });
    }

    @Test
    public void parse_missingIdFlag_throwsPharmaTrackerException() {
        String input = "/n John Doe /p 91234567";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_missingNameFlag_throwsPharmaTrackerException() {
        String input = "/id C001 /p 91234567";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_missingPhoneFlag_throwsPharmaTrackerException() {
        String input = "/id C001 /n John Doe /addr 123 Clementi Road";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_flagsInWrongOrder_throwsPharmaTrackerException() {
        String input = "/n John Doe /id C001 /p 91234567";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_emptyNameField_throwsPharmaTrackerException() {
        String input = "/id C001 /n /p 91234567";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_invalidPhoneFormat_throwsPharmaTrackerException() {
        String input = "/id C001 /n John Doe /p 71234567";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_emptyAddressFlag_throwsPharmaTrackerException() {
        String input = "/id C001 /n John Doe /p 91234567 /addr ";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_emptyDescription_throwsPharmaTrackerException() {
        String input = "   ";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }
}