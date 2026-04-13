package seedu.pharmatracker.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class CustomerParserUtilTest {

    @Test
    public void extractCustomerID_validInput_returnsId() throws PharmaTrackerException {
        String input = "/id C001 /n John Doe";
        assertEquals("C001", CustomerParserUtil.extractCustomerID(input));
    }

    @Test
    public void extractCustomerID_missingIdFlag_throwsPharmaTrackerException() {
        String input = "/n John Doe";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerID(input));
    }

    @Test
    public void extractCustomerID_missingNameFlag_throwsPharmaTrackerException() {
        String input = "/id C001";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerID(input));
    }

    @Test
    public void extractCustomerID_wrongOrder_throwsPharmaTrackerException() {
        String input = "/n John Doe /id C001";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerID(input));
    }

    @Test
    public void extractCustomerID_emptyId_throwsPharmaTrackerException() {
        String input = "/id   /n John Doe";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerID(input));
    }

    @Test
    public void extractCustomerName_validInput_returnsName() throws PharmaTrackerException {
        String input = "/n Jane Smith /p 91234567";
        assertEquals("Jane Smith", CustomerParserUtil.extractCustomerName(input));
    }

    @Test
    public void extractCustomerName_missingNameFlag_throwsPharmaTrackerException() {
        String input = "/id C001 /p 91234567";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerName(input));
    }

    @Test
    public void extractCustomerName_wrongOrder_throwsPharmaTrackerException() {
        String input = "/p 91234567 /n Jane Smith";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerName(input));
    }

    @Test
    public void extractCustomerName_emptyName_throwsPharmaTrackerException() {
        String input = "/n   /p 91234567";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerName(input));
    }

    @Test
    public void extractCustomerPhone_validNumberAtEnd_returnsPhone() throws PharmaTrackerException {
        String input = "/p 91234567";
        assertEquals("91234567", CustomerParserUtil.extractCustomerPhone(input));
    }

    @Test
    public void extractCustomerPhone_validNumberBeforeAddress_returnsPhone() throws PharmaTrackerException {
        String input = "/p 87654321 /addr 123 Clementi Road";
        assertEquals("87654321", CustomerParserUtil.extractCustomerPhone(input));
    }

    @Test
    public void extractCustomerPhone_validNumberBeforeAllergy_returnsPhone() throws PharmaTrackerException {
        String input = "/p 98765432 /allergy Peanuts";
        assertEquals("98765432", CustomerParserUtil.extractCustomerPhone(input));
    }

    @Test
    public void extractCustomerPhone_invalidStartingDigit_throwsPharmaTrackerException() {
        String input = "/p 71234567 /addr 123";
        PharmaTrackerException thrown = assertThrows(PharmaTrackerException.class,
                () -> CustomerParserUtil.extractCustomerPhone(input));
        assertTrue(thrown.getMessage().contains("starts with either '8' or '9'"));
    }

    @Test
    public void extractCustomerPhone_emptyPhone_throwsPharmaTrackerException() {
        String input = "/p   /addr 123 Clementi Road";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerPhone(input));
    }

    @Test
    public void extractCustomerAllergies_noFlag_returnsEmptyList() throws PharmaTrackerException {
        String input = "/id C001 /n John Doe";
        ArrayList<String> allergies = CustomerParserUtil.extractCustomerAllergies(input);
        assertTrue(allergies.isEmpty());
    }

    @Test
    public void extractCustomerAllergies_validSingleAllergy_returnsList() throws PharmaTrackerException {
        String input = "/allergy Penicillin";
        ArrayList<String> allergies = CustomerParserUtil.extractCustomerAllergies(input);
        assertEquals(1, allergies.size());
        assertEquals("penicillin", allergies.get(0)); // Checks the lowercase conversion
    }

    @Test
    public void extractCustomerAllergies_validMultipleAllergies_returnsList() throws PharmaTrackerException {
        String input = "/allergy Peanuts, Shellfish , Dust";
        ArrayList<String> allergies = CustomerParserUtil.extractCustomerAllergies(input);
        assertEquals(3, allergies.size());
        assertEquals("peanuts", allergies.get(0));
        assertEquals("shellfish", allergies.get(1)); // Checks trimming
        assertEquals("dust", allergies.get(2));
    }

    @Test
    public void extractCustomerAllergies_emptyAllergyFlag_throwsPharmaTrackerException() {
        String input = "/allergy   ";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerAllergies(input));
    }

    @Test
    public void extractCustomerAllergies_trailingCommas_ignoresEmptyTokens() throws PharmaTrackerException {
        String input = "/allergy Peanuts, ,  ";
        ArrayList<String> allergies = CustomerParserUtil.extractCustomerAllergies(input);
        assertEquals(1, allergies.size());
        assertEquals("peanuts", allergies.get(0));
    }

    @Test
    public void extractCustomerAddress_noFlag_returnsEmptyString() throws PharmaTrackerException {
        String input = "/id C001 /n John Doe";
        assertEquals("", CustomerParserUtil.extractCustomerAddress(input));
    }

    @Test
    public void extractCustomerAddress_validAddressAtEnd_returnsAddress() throws PharmaTrackerException {
        String input = "/addr 123 Clementi Road";
        assertEquals("123 Clementi Road", CustomerParserUtil.extractCustomerAddress(input));
    }

    @Test
    public void extractCustomerAddress_validAddressBeforeAllergy_returnsAddress() throws PharmaTrackerException {
        String input = "/addr 456 Kent Ridge /allergy peanuts";
        assertEquals("456 Kent Ridge", CustomerParserUtil.extractCustomerAddress(input));
    }

    @Test
    public void extractCustomerAddress_emptyAddressFlag_throwsPharmaTrackerException() {
        String input = "/addr   ";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerAddress(input));
    }

    @Test
    public void extractCustomerAddress_emptyAddressBeforeAllergy_throwsPharmaTrackerException() {
        String input = "/addr   /allergy peanuts";
        assertThrows(PharmaTrackerException.class, () -> CustomerParserUtil.extractCustomerAddress(input));
    }

    @Test
    public void extractCustomerUpdateFlag_flagAbsent_returnsNull() throws PharmaTrackerException {
        String input = "/p 91234567";
        assertNull(CustomerParserUtil.extractCustomerUpdateFlag(input, CustomerParserUtil.FLAG_ADDRESS));
    }

    @Test
    public void extractCustomerUpdateFlag_validValueAtEnd_returnsValue() throws PharmaTrackerException {
        String input = "/p 91234567 /addr 123 Clementi Road";
        assertEquals("123 Clementi Road",
                CustomerParserUtil.extractCustomerUpdateFlag(input, CustomerParserUtil.FLAG_ADDRESS));
    }

    @Test
    public void extractCustomerUpdateFlag_validValueBeforeNextFlag_returnsValue() throws PharmaTrackerException {
        String input = "/n Jane Doe /p 91234567";
        assertEquals("Jane Doe",
                CustomerParserUtil.extractCustomerUpdateFlag(input, CustomerParserUtil.FLAG_NAME));
    }

    @Test
    public void extractCustomerUpdateFlag_flagPresentButEmpty_throwsPharmaTrackerException() {
        // Flag is at the very end with no value
        String input = "/n Jane Doe /p";
        assertThrows(PharmaTrackerException.class,
                () -> CustomerParserUtil.extractCustomerUpdateFlag(input, CustomerParserUtil.FLAG_PHONE));
    }

    @Test
    public void extractCustomerUpdateFlag_flagPresentEmptyBeforeNextFlag_throwsPharmaTrackerException() {
        // Flag is followed immediately by another flag
        String input = "/n /p 91234567";
        assertThrows(PharmaTrackerException.class,
                () -> CustomerParserUtil.extractCustomerUpdateFlag(input, CustomerParserUtil.FLAG_NAME));
    }
}
