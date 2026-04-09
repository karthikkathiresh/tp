package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.DispenseLog;
import seedu.pharmatracker.data.DispenseRecord;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * JUnit tests for {@link DispenseSummaryCommand}.
 *
 * <p>Redirects {@code System.out} to an in-memory stream before each test
 * and restores the original stream after each test.
 */
public class DispenseSummaryCommandTest {

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

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private Inventory inventoryWithRecord(LocalDate date, String medName,
                                          String dosage, int qty, String patient) {
        Inventory inventory = new Inventory();
        DispenseLog log = inventory.getDispenseLog();
        log.addRecord(new DispenseRecord(date, LocalTime.of(9, 0),
                medName, dosage, qty, patient));
        return inventory;
    }

    // -----------------------------------------------------------------------
    // Empty log
    // -----------------------------------------------------------------------

    /**
     * Tests that when no records exist for today the output says so.
     */
    @Test
    public void execute_emptyLog_printsNoRecordsMessage() {
        new DispenseSummaryCommand().execute(new Inventory(), new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("No dispense events recorded"));
    }

    /**
     * Tests that when no records exist for a specific past date the output says so.
     */
    @Test
    public void execute_emptyLogForDate_printsNoRecordsMessage() {
        LocalDate past = LocalDate.of(2026, 1, 1);
        new DispenseSummaryCommand(past).execute(new Inventory(), new Ui(), new CustomerList());
        assertTrue(out.toString().contains("No dispense events recorded"));
    }

    // -----------------------------------------------------------------------
    // Header always printed
    // -----------------------------------------------------------------------

    /**
     * Tests that the output always contains a header with the target date.
     */
    @Test
    public void execute_anyLog_printsDateHeader() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        new DispenseSummaryCommand(date).execute(new Inventory(), new Ui(), new CustomerList());
        assertTrue(out.toString().contains("2026-04-09"));
    }

    // -----------------------------------------------------------------------
    // Records present for the target date
    // -----------------------------------------------------------------------

    /**
     * Tests that a record on the target date is included in the output.
     */
    @Test
    public void execute_recordOnTargetDate_printsRecord() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        Inventory inventory = inventoryWithRecord(date, "Paracetamol", "500mg", 2, "");
        new DispenseSummaryCommand(date).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Paracetamol"));
    }

    /**
     * Tests that the output contains the medication dosage.
     */
    @Test
    public void execute_recordOnTargetDate_printsDosage() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        Inventory inventory = inventoryWithRecord(date, "Ibuprofen", "200mg", 1, "");
        new DispenseSummaryCommand(date).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("200mg"));
    }

    /**
     * Tests that the output contains the dispensed quantity.
     */
    @Test
    public void execute_recordOnTargetDate_printsQuantity() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        Inventory inventory = inventoryWithRecord(date, "Aspirin", "100mg", 5, "");
        new DispenseSummaryCommand(date).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("5"));
    }

    /**
     * Tests that when a patient name is linked it appears in the output.
     */
    @Test
    public void execute_recordWithPatient_printsPatientName() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        Inventory inventory = inventoryWithRecord(date, "Paracetamol", "500mg", 2, "Alice Tan");
        new DispenseSummaryCommand(date).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Alice Tan"));
    }

    /**
     * Tests that when no patient is linked the patient label is absent.
     */
    @Test
    public void execute_recordWithoutPatient_doesNotPrintPatientLabel() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        Inventory inventory = inventoryWithRecord(date, "Paracetamol", "500mg", 2, "");
        new DispenseSummaryCommand(date).execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("Patient:"));
    }

    // -----------------------------------------------------------------------
    // Totals line
    // -----------------------------------------------------------------------

    /**
     * Tests that the totals line reflects the correct number of events.
     */
    @Test
    public void execute_multipleRecords_printsTotalEventCount() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        Inventory inventory = new Inventory();
        DispenseLog log = inventory.getDispenseLog();
        log.addRecord(new DispenseRecord(date, LocalTime.of(9, 0), "Med A", "10mg", 2, ""));
        log.addRecord(new DispenseRecord(date, LocalTime.of(10, 0), "Med B", "20mg", 3, ""));
        new DispenseSummaryCommand(date).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("2 dispense event(s)"));
    }

    /**
     * Tests that the totals line reflects the correct total units dispensed.
     */
    @Test
    public void execute_multipleRecords_printsTotalUnits() {
        LocalDate date = LocalDate.of(2026, 4, 9);
        Inventory inventory = new Inventory();
        DispenseLog log = inventory.getDispenseLog();
        log.addRecord(new DispenseRecord(date, LocalTime.of(9, 0), "Med A", "10mg", 2, ""));
        log.addRecord(new DispenseRecord(date, LocalTime.of(10, 0), "Med B", "20mg", 3, ""));
        new DispenseSummaryCommand(date).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("5 unit(s) dispensed"));
    }

    // -----------------------------------------------------------------------
    // Date isolation — records on other dates must not appear
    // -----------------------------------------------------------------------

    /**
     * Tests that records on a different date are not shown when querying a specific date.
     */
    @Test
    public void execute_recordOnDifferentDate_notShown() {
        LocalDate target = LocalDate.of(2026, 4, 9);
        LocalDate other  = LocalDate.of(2026, 4, 8);
        Inventory inventory = inventoryWithRecord(other, "Aspirin", "100mg", 1, "");
        new DispenseSummaryCommand(target).execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("Aspirin"));
        assertTrue(out.toString().contains("No dispense events recorded"));
    }

    /**
     * Tests that only records for the target date are shown when mixed dates exist in the log.
     */
    @Test
    public void execute_mixedDates_onlyTargetDateShown() {
        LocalDate target = LocalDate.of(2026, 4, 9);
        LocalDate other  = LocalDate.of(2026, 4, 8);
        Inventory inventory = new Inventory();
        DispenseLog log = inventory.getDispenseLog();
        log.addRecord(new DispenseRecord(target, LocalTime.of(9, 0), "Paracetamol", "500mg", 2, ""));
        log.addRecord(new DispenseRecord(other,  LocalTime.of(8, 0), "Ibuprofen",   "200mg", 1, ""));
        new DispenseSummaryCommand(target).execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Paracetamol"));
        assertFalse(output.contains("Ibuprofen"));
    }

    // -----------------------------------------------------------------------
    // Integration: DispenseCommand populates the log
    // -----------------------------------------------------------------------

    /**
     * Tests that executing a DispenseCommand adds exactly one record to the dispense log.
     */
    @Test
    public void dispenseCommand_execute_addsRecordToLog() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2027-12-31", ""));
        new DispenseCommand(1, 5).execute(inventory, new Ui(), new CustomerList());
        // One record should now be in the log for today
        int count = inventory.getDispenseLog().getRecordsByDate(LocalDate.now()).size();
        assertTrue(count == 1);
    }

    /**
     * Tests that two consecutive DispenseCommands add two records to today's log.
     */
    @Test
    public void dispenseCommand_executeTwice_addsTwoRecordsToLog() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-06-01", ""));
        new DispenseCommand(1, 5).execute(inventory, new Ui(), new CustomerList());
        new DispenseCommand(1, 3).execute(inventory, new Ui(), new CustomerList());
        int count = inventory.getDispenseLog().getRecordsByDate(LocalDate.now()).size();
        assertTrue(count == 2);
    }

    /**
     * Tests that a failed DispenseCommand (insufficient stock) does NOT add a record.
     */
    @Test
    public void dispenseCommand_failedDispense_doesNotAddRecord() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 5, "2027-06-01", ""));
        new DispenseCommand(1, 100).execute(inventory, new Ui(), new CustomerList()); // will fail
        int count = inventory.getDispenseLog().getRecordsByDate(LocalDate.now()).size();
        assertTrue(count == 0);
    }

    /**
     * Tests that a DispenseCommand linked to a customer records the patient name in the log.
     */
    @Test
    public void dispenseCommand_withCustomer_recordContainsPatientName() {
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2027-12-31", ""));
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new DispenseCommand(1, 5, 1).execute(inventory, new Ui(), customerList);
        DispenseRecord record = inventory.getDispenseLog().getRecordsByDate(LocalDate.now()).get(0);
        assertTrue(record.getCustomerName().contains("John Tan"));
    }

    /**
     * Tests end-to-end: DispenseCommand records to log, DispenseSummaryCommand shows it.
     */
    @Test
    public void endToEnd_dispenseAndSummary_showsDispensedMedication() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2027-12-31", ""));
        new DispenseCommand(1, 10).execute(inventory, new Ui(), new CustomerList());
        out.reset();
        new DispenseSummaryCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Paracetamol"));
    }
}
