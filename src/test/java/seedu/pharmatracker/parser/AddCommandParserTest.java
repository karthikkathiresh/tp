package seedu.pharmatracker.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import seedu.pharmatracker.command.AddCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class AddCommandParserTest {

    private AddCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new AddCommandParser();
    }

    @Test
    public void parse_validMandatoryFieldsOnly_returnsAddCommand() {
        String input = "/n Paracetamol /d 500mg /q 50 /e 2026-12-31";

        assertDoesNotThrow(() -> {
            AddCommand command = parser.parse(input);
            assertNotNull(command);
        });
    }

    @Test
    public void parse_allValidFieldsPresent_returnsAddCommand() {
        String input = "/n Amoxicillin /d 250mg /q 50 /e 2026-06-01 /t antibiotic "
                + "/df Capsule /mfr Pfizer /dir Take after meals /freq Twice daily "
                + "/route Oral /max 1000mg /warn May cause drowsiness /warn Avoid alcohol";

        assertDoesNotThrow(() -> {
            AddCommand command = parser.parse(input);
            assertNotNull(command);
        });
    }

    @Test
    public void parse_missingNameFlag_throwsPharmaTrackerException() {
        String input = "/d 500mg /q 50 /e 2026-12-31";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_missingDosageFlag_throwsPharmaTrackerException() {
        String input = "/n Paracetamol /q 50 /e 2026-12-31";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_missingQuantityFlag_throwsPharmaTrackerException() {
        String input = "/n Paracetamol /d 500mg /e 2026-12-31";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_missingExpiryDateFlag_throwsPharmaTrackerException() {
        String input = "/n Paracetamol /d 500mg /q 50";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_invalidQuantityFormat_throwsPharmaTrackerException() {
        String input = "/n Paracetamol /d 500mg /q abc /e 2026-12-31";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_emptyDescription_throwsPharmaTrackerException() {
        String input = "   ";

        assertThrows(PharmaTrackerException.class, () -> parser.parse(input));
    }
}
