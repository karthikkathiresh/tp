package seedu.pharmatracker.command;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.parser.Parser;
import seedu.pharmatracker.ui.Ui;

public class ViewCommandTest {
    @Test
    public void viewCommand_validIndex_printsDetails() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
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

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ViewCommand(1).execute(inventory, ui);
        System.setOut(System.out);

        String output = outContent.toString();
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

    @Test
    public void viewCommand_noOptionalFields_printsNA() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ViewCommand(1).execute(inventory, ui);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("N/A"));
        assertTrue(output.contains("None"));
    }

    @Test
    public void viewCommand_invalidIndexTooHigh_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ViewCommand(5).execute(inventory, ui);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Invalid index"));
    }

    @Test
    public void viewCommand_invalidIndexZero_printsError() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ViewCommand(0).execute(inventory, ui);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Invalid index"));
    }

    @Test
    public void viewCommand_negativeIndex_printsError() {
        Ui ui = new Ui();
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ViewCommand(-1).execute(inventory, ui);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Invalid index"));
    }

    @Test
    public void viewCommand_emptyInventory_printsEmpty() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ViewCommand(1).execute(inventory, ui);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Inventory is empty"));
    }

    @Test
    public void viewCommand_partialOptionalFields_showsSetFieldsAndNA() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Medication med = new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller");
        med.setDosageForm("Tablet");
        med.setRoute("Oral");
        inventory.addMedication(med);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ViewCommand(1).execute(inventory, ui);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Tablet"));
        assertTrue(output.contains("Oral"));
        assertTrue(output.contains("N/A"));
    }

    @Test
    public void parser_viewCommand_returnsCorrectCommandType() {
        Command c = Parser.parse("view 1");
        assertTrue(c instanceof ViewCommand);
    }

    @Test
    public void parser_viewCommandNoIndex_returnsNull() {
        Command c = Parser.parse("view");
        assertEquals(null, c);
    }

    @Test
    public void parser_viewCommandInvalidIndex_returnsNull() {
        Command c = Parser.parse("view abc");
        assertEquals(null, c);
    }
}
