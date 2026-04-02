package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.parser.Parser;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewCommandTest {

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
                new ViewCommand(1).execute(null, new Ui(), new CustomerList()));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));
        assertThrows(AssertionError.class, () ->
                new ViewCommand(1).execute(inventory, null, new CustomerList()));
    }

    // -------------------------------------------------------------------------
    // Valid index
    // -------------------------------------------------------------------------

    /**
     * Tests that a valid index prints all set optional fields correctly.
     */
    @Test
    public void execute_validIndex_printsDetails() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Ibuprofen", "400mg", 50, "2026-06-15", "painkiller");
        med.setDosageForm("Tablet");
        med.setManufacturer("PharmaCorp Ltd.");
        med.setDirections("Take 1 tablet by mouth");
        med.setFrequency("Every 6-8 hours as needed");
        med.setRoute("Oral");
        med.setMaxDailyDose("3200mg");
        med.addWarning("Take with food or milk");
        med.addWarning("May cause drowsiness");
        inventory.addMedication(med);
        new ViewCommand(1).execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Drug Name:"));
        assertTrue(output.contains("Ibuprofen"));
        assertTrue(output.contains("400mg"));
        assertTrue(output.contains("Tablet"));
        assertTrue(output.contains("PharmaCorp Ltd."));
        assertTrue(output.contains("Take 1 tablet by mouth"));
        assertTrue(output.contains("Every 6-8 hours as needed"));
        assertTrue(output.contains("Oral"));
        assertTrue(output.contains("3200mg"));
        assertTrue(output.contains("Take with food or milk"));
        assertTrue(output.contains("May cause drowsiness"));
    }

    /**
     * Tests that a medication with no optional fields set prints N/A and None placeholders.
     */
    @Test
    public void execute_noOptionalFields_printsNA() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));
        new ViewCommand(1).execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("N/A"));
        assertTrue(output.contains("None"));
    }

    /**
     * Tests that a medication with only some optional fields set shows those fields
     * and N/A for the rest.
     */
    @Test
    public void execute_partialOptionalFields_showsSetFieldsAndNA() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller");
        med.setDosageForm("Tablet");
        med.setRoute("Oral");
        inventory.addMedication(med);
        new ViewCommand(1).execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Tablet"));
        assertTrue(output.contains("Oral"));
        assertTrue(output.contains("N/A"));
    }

    // -------------------------------------------------------------------------
    // Invalid index
    // -------------------------------------------------------------------------

    /**
     * Tests that an index beyond the list size prints an invalid index error.
     */
    @Test
    public void execute_invalidIndexTooHigh_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));
        new ViewCommand(5).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid index"));
    }

    /**
     * Tests that an index of zero prints an invalid index error.
     */
    @Test
    public void execute_invalidIndexZero_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));
        new ViewCommand(0).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid index"));
    }

    /**
     * Tests that a negative index prints an invalid index error.
     */
    @Test
    public void execute_negativeIndex_printsError() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));
        new ViewCommand(-1).execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Invalid index"));
    }

    /**
     * Tests that viewing from an empty inventory prints the empty message.
     */
    @Test
    public void execute_emptyInventory_printsEmpty() {
        new ViewCommand(1).execute(new Inventory(), new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Inventory is empty"));
    }

    // -------------------------------------------------------------------------
    // Parser tests
    // -------------------------------------------------------------------------

    /**
     * Tests that "view 1" parses to a ViewCommand instance.
     */
    @Test
    public void parser_viewCommand_returnsCorrectCommandType() throws PharmaTrackerException {
        Command c = Parser.parse("view 1");
        assertTrue(c instanceof ViewCommand);
    }

    /**
     * Tests that "view" with no index returns null.
     */
    @Test
    public void parser_viewCommandNoIndex_returnsNull() throws PharmaTrackerException {
        Command c = Parser.parse("view");
        assertEquals(null, c);
    }

    /**
     * Tests that "view abc" with a non-integer index returns null.
     */
    @Test
    public void parser_viewCommandInvalidIndex_returnsNull() throws PharmaTrackerException {
        Command c = Parser.parse("view abc");
        assertEquals(null, c);
    }
}
