package seedu.pharmatracker.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class MedicationParserUtilTest {

    @Test
    public void extractName_validInput_returnsName() throws PharmaTrackerException {
        String input = "/n Paracetamol /d 500mg";
        assertEquals("Paracetamol", MedicationParserUtil.extractName(input));
    }

    @Test
    public void extractName_missingNameFlag_throwsPharmaTrackerException() {
        String input = "/d 500mg";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractName(input));
    }

    @Test
    public void extractName_missingDosageFlag_throwsPharmaTrackerException() {
        String input = "/n Paracetamol";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractName(input));
    }

    @Test
    public void extractName_wrongOrder_throwsPharmaTrackerException() {
        String input = "/d 500mg /n Paracetamol";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractName(input));
    }

    @Test
    public void extractName_emptyName_throwsPharmaTrackerException() {
        String input = "/n   /d 500mg";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractName(input));
    }

    @Test
    public void extractDosage_validInput_returnsDosage() throws PharmaTrackerException {
        String input = "/d 500mg /q 50";
        assertEquals("500mg", MedicationParserUtil.extractDosage(input));
    }

    @Test
    public void extractDosage_missingDosageFlag_throwsPharmaTrackerException() {
        String input = "/q 50";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractDosage(input));
    }

    @Test
    public void extractDosage_missingQuantityFlag_throwsPharmaTrackerException() {
        String input = "/d 500mg";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractDosage(input));
    }

    @Test
    public void extractDosage_wrongOrder_throwsPharmaTrackerException() {
        String input = "/q 50 /d 500mg";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractDosage(input));
    }

    @Test
    public void extractDosage_emptyDosage_throwsPharmaTrackerException() {
        String input = "/d   /q 50";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractDosage(input));
    }

    @Test
    public void extractQuantity_validPositiveNumber_returnsQuantity() throws PharmaTrackerException {
        String input = "/q 50 /e 2026-12-31";
        assertEquals(50, MedicationParserUtil.extractQuantity(input));
    }

    @Test
    public void extractQuantity_missingQuantityFlag_throwsPharmaTrackerException() {
        String input = "/e 2026-12-31";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractQuantity(input));
    }

    @Test
    public void extractQuantity_missingExpiryFlag_throwsPharmaTrackerException() {
        String input = "/q 50";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractQuantity(input));
    }

    @Test
    public void extractQuantity_wrongOrder_throwsPharmaTrackerException() {
        String input = "/e 2026-12-31 /q 50";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractQuantity(input));
    }

    @Test
    public void extractQuantity_emptyQuantity_throwsPharmaTrackerException() {
        String input = "/q   /e 2026-12-31";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractQuantity(input));
    }

    @Test
    public void extractQuantity_negativeNumber_throwsPharmaTrackerException() {
        String input = "/q -5 /e 2026-12-31";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractQuantity(input));
    }

    @Test
    public void extractQuantity_nonNumericString_throwsPharmaTrackerException() {
        String input = "/q fifty /e 2026-12-31";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractQuantity(input));
    }

    @Test
    public void extractExpiryDate_validFormatYyyyMmDd_returnsFormattedDate() throws PharmaTrackerException {
        String input = "/e 2026-05-15";
        assertEquals("2026-05-15", MedicationParserUtil.extractExpiryDate(input));
    }

    @Test
    public void extractExpiryDate_validFormatDSlashMSlashYyyy_returnsFormattedDate() throws PharmaTrackerException {
        String input = "/e 15/5/2026";
        assertEquals("2026-05-15", MedicationParserUtil.extractExpiryDate(input));
    }

    @Test
    public void extractExpiryDate_validFormatDDashMDashYyyy_returnsFormattedDate() throws PharmaTrackerException {
        String input = "/e 15-5-2026";
        assertEquals("2026-05-15", MedicationParserUtil.extractExpiryDate(input));
    }

    @Test
    public void extractExpiryDate_missingExpiryFlag_throwsPharmaTrackerException() {
        String input = "random string without flag";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractExpiryDate(input));
    }

    @Test
    public void extractExpiryDate_emptyDate_throwsPharmaTrackerException() {
        String input = "/e   ";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractExpiryDate(input));
    }

    @Test
    public void extractExpiryDate_invalidDateFormat_throwsPharmaTrackerException() {
        String input = "/e 2026.05.15";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractExpiryDate(input));
    }

    @Test
    public void extractWarnings_noWarningFlag_returnsEmptyList() throws PharmaTrackerException {
        String input = "/n Paracetamol /d 500mg";
        ArrayList<String> warnings = MedicationParserUtil.extractWarnings(input);
        assertTrue(warnings.isEmpty());
    }

    @Test
    public void extractWarnings_singleWarning_returnsList() throws PharmaTrackerException {
        String input = "/n Paracetamol /warn May cause drowsiness";
        ArrayList<String> warnings = MedicationParserUtil.extractWarnings(input);
        assertEquals(1, warnings.size());
        assertEquals("May cause drowsiness", warnings.get(0));
    }

    @Test
    public void extractWarnings_multipleWarnings_returnsList() throws PharmaTrackerException {
        String input = "/warn Warning One /n Paracetamol /warn Warning Two";
        ArrayList<String> warnings = MedicationParserUtil.extractWarnings(input);
        assertEquals(2, warnings.size());
        assertEquals("Warning One", warnings.get(0));
        assertEquals("Warning Two", warnings.get(1));
    }

    @Test
    public void extractWarnings_emptyWarning_isIgnored() throws PharmaTrackerException {
        String input = "/warn Warning One /warn   /warn Warning Two";
        ArrayList<String> warnings = MedicationParserUtil.extractWarnings(input);
        assertEquals(2, warnings.size());
        assertEquals("Warning One", warnings.get(0));
        assertEquals("Warning Two", warnings.get(1));
    }

    @Test
    public void extractOptionalQuantity_flagMissing_returnsNull() throws PharmaTrackerException {
        String input = "/n Paracetamol /d 500mg";
        assertNull(MedicationParserUtil.extractOptionalQuantity(input));
    }

    @Test
    public void extractOptionalQuantity_validQuantity_returnsInteger() throws PharmaTrackerException {
        String input = "/q 100";
        assertEquals(100, MedicationParserUtil.extractOptionalQuantity(input));
    }

    @Test
    public void extractOptionalQuantity_negativeQuantity_throwsPharmaTrackerException() {
        String input = "/q -10";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractOptionalQuantity(input));
    }

    @Test
    public void extractOptionalQuantity_nonNumericQuantity_throwsPharmaTrackerException() {
        String input = "/q abc";
        assertThrows(PharmaTrackerException.class, () -> MedicationParserUtil.extractOptionalQuantity(input));
    }
}
