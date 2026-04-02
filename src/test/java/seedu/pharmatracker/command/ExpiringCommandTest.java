package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link ExpiringCommand}.
 *
 * <p>Redirects {@code System.out} to an in-memory stream before each test
 * to allow assertion of printed output, and restores the original stream after.
 *
 * <p>Dates are anchored relative to today to keep tests time-independent:
 * <ul>
 *   <li>Expired: expiry date set well in the past (2000-01-01)</li>
 *   <li>Expiring soon: expiry date set 7 days from now (always within default 30-day window)</li>
 *   <li>Safe: expiry date set far in the future (2099-12-31)</li>
 * </ul>
 */
//@@author yihernggggg
public class ExpiringCommandTest {

    // Static variables declared before instance variables (checkstyle: declaration order)
    // Reference dates computed once at load time — tests never go stale
    private static final String EXPIRY_PAST = "2000-01-01";
    private static final String EXPIRY_SLASH_PAST = "01/01/2000";
    private static final String EXPIRY_SOON_ISO =
            LocalDate.now().plusDays(7).toString();
    private static final String EXPIRY_SOON_SLASH =
            LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    private static final String EXPIRY_FAR = "2099-12-31";

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream original = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(original);
    }

    // -------------------------------------------------------------------------
    // Null / assertion guard tests
    // -------------------------------------------------------------------------

    /**
     * Tests that passing a null inventory throws an AssertionError.
     */
    @Test
    public void execute_nullInventory_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ExpiringCommand().execute(null, new Ui(), new CustomerList()));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ExpiringCommand().execute(new Inventory(), null, new CustomerList()));
    }

    // -------------------------------------------------------------------------
    // Empty inventory
    // -------------------------------------------------------------------------

    /**
     * Tests that an empty inventory produces output that does not mention any medication.
     */
    @Test
    public void execute_emptyInventory_noMedicationMentioned() {
        new ExpiringCommand().execute(new Inventory(), new Ui(), new CustomerList());
        String output = out.toString();
        // Two separate assertions — assertFalse(a || b) would pass even if one name appeared
        assertFalse(output.contains("Aspirin"));
        assertFalse(output.contains("Paracetamol"));
    }

    // -------------------------------------------------------------------------
    // Expired medications
    // -------------------------------------------------------------------------

    /**
     * Tests that a medication with a past expiry date (ISO yyyy-MM-dd format)
     * is shown in the output.
     */
    @Test
    public void execute_expiredMedicationIsoFormat_appearsInOutput() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, EXPIRY_PAST, "pain"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Aspirin"));
    }

    /**
     * Tests that a medication with a past expiry date in dd/MM/yyyy format is
     * correctly identified as expired and shown in the output.
     */
    @Test
    public void execute_expiredMedicationSlashFormat_appearsInOutput() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 50, EXPIRY_SLASH_PAST, "pain"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Ibuprofen"));
    }

    // -------------------------------------------------------------------------
    // Expiring soon medications
    // -------------------------------------------------------------------------

    /**
     * Tests that a medication expiring within the default 30-day window
     * (ISO format) appears in the output.
     */
    @Test
    public void execute_expiringWithinDefaultWindow_isoFormat() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, EXPIRY_SOON_ISO, "fever"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Paracetamol"));
    }

    /**
     * Tests that a medication expiring within the default 30-day window
     * (dd/MM/yyyy format) appears in the output.
     */
    @Test
    public void execute_expiringWithinDefaultWindow_slashFormat() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Cetirizine", "10mg", 30, EXPIRY_SOON_SLASH, "allergy"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Cetirizine"));
    }

    /**
     * Tests that a medication expiring within the custom window appears in the output.
     */
    @Test
    public void execute_expiringWithinCustomWindow_appearsInOutput() {
        Inventory inventory = new Inventory();
        // Expires in 7 days — within a 14-day custom window
        inventory.addMedication(new Medication("Metformin", "500mg", 60, EXPIRY_SOON_ISO, "diabetes"));
        new ExpiringCommand(14).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Metformin"));
    }

    /**
     * Tests that a medication expiring beyond the custom window does NOT appear.
     */
    @Test
    public void execute_notExpiring_doesNotAppearInOutput() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Omeprazole", "20mg", 90, EXPIRY_FAR, "gastric"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("Omeprazole"));
    }

    /**
     * Tests that a medication expiring in 7 days is NOT shown when the custom
     * window is only 3 days.
     */
    @Test
    public void execute_expiringOutsideCustomWindow_doesNotAppearInOutput() {
        Inventory inventory = new Inventory();
        // Expires in 7 days, but window is 3 days — should NOT appear
        inventory.addMedication(new Medication("Atorvastatin", "10mg", 45, EXPIRY_SOON_ISO, "cholesterol"));
        new ExpiringCommand(3).execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("Atorvastatin"));
    }

    // -------------------------------------------------------------------------
    // Mixed inventory
    // -------------------------------------------------------------------------

    /**
     * Tests that when the inventory contains both expired and safe medications,
     * only the expired one appears in the output.
     */
    @Test
    public void execute_mixedInventory_onlyExpiredShown() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, EXPIRY_PAST, "pain"));
        inventory.addMedication(new Medication("Omeprazole", "20mg", 90, EXPIRY_FAR, "gastric"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Aspirin"));
        assertFalse(output.contains("Omeprazole"));
    }

    /**
     * Tests that when inventory contains both expired and soon-to-expire medications,
     * both appear in the output.
     */
    @Test
    public void execute_expiredAndExpiringSoon_bothShown() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, EXPIRY_PAST, "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, EXPIRY_SOON_ISO, "fever"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("Paracetamol"));
    }

    /**
     * Tests that an inventory with no expiring or expired medications
     * does not show any medication names.
     */
    @Test
    public void execute_allSafeMedications_noneShown() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Omeprazole", "20mg", 90, EXPIRY_FAR, "gastric"));
        inventory.addMedication(new Medication("Metformin", "500mg", 60, EXPIRY_FAR, "diabetes"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertFalse(output.contains("Omeprazole"));
        assertFalse(output.contains("Metformin"));
    }

    // -------------------------------------------------------------------------
    // Date format handling
    // -------------------------------------------------------------------------

    /**
     * Tests that a medication with an unparseable expiry date string
     * is silently skipped and does not cause a crash.
     */
    @Test
    public void execute_malformedExpiryDate_skippedGracefully() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("BadDate", "100mg", 10, "not-a-date", "pain"));
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, EXPIRY_PAST, "pain"));
        // Should not throw; BadDate should be skipped; Aspirin should still appear
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertFalse(output.contains("BadDate"));
        assertTrue(output.contains("Aspirin"));
    }

    /**
     * Tests that a medication with a null expiry date is silently skipped.
     */
    @Test
    public void execute_nullExpiryDate_skippedGracefully() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("NullDate", "100mg", 10, null, "pain"));
        inventory.addMedication(new Medication("Aspirin", "100mg", 10, EXPIRY_PAST, "pain"));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertFalse(output.contains("NullDate"));
        assertTrue(output.contains("Aspirin"));
    }

    // -------------------------------------------------------------------------
    // Default vs custom constructor
    // -------------------------------------------------------------------------

    /**
     * Tests that the no-arg constructor (30-day default) and the
     * explicit 30-day constructor produce the same output.
     */
    @Test
    public void execute_defaultAndExplicit30Days_sameOutput() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, EXPIRY_SOON_ISO, "fever"));

        ByteArrayOutputStream outDefault = new ByteArrayOutputStream();
        ByteArrayOutputStream outExplicit = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outDefault));
        new ExpiringCommand().execute(inventory, new Ui(), new CustomerList());

        System.setOut(new PrintStream(outExplicit));
        new ExpiringCommand(30).execute(inventory, new Ui(), new CustomerList());

        System.setOut(original);
        assertEquals(outDefault.toString(), outExplicit.toString());
    }
}
