package seedu.pharmatracker.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelpCommandTest {
    private Inventory inventory;
    private Ui ui;
    private CustomerList customerList;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        customerList = new CustomerList();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void execute_helpCommand_printsAllFeatures() {
        new HelpCommand().execute(inventory, ui, customerList);
        String output = outContent.toString();
        assertTrue(output.contains("Add Medication"));
        assertTrue(output.contains("Delete Medication"));
        assertTrue(output.contains("Dispense Medication"));
        assertTrue(output.contains("List Inventory"));
        assertTrue(output.contains("Find / Search Medication"));
        assertTrue(output.contains("View Medication Details"));
        assertTrue(output.contains("Sort by Expiry Date"));
        assertTrue(output.contains("Print Description Label"));
        assertTrue(output.contains("Viewing Help"));
        assertTrue(output.contains("Exiting the Program"));
    }

    @Test
    void execute_helpCommand_printsFormats() {
        new HelpCommand().execute(inventory, ui, customerList);
        String output = outContent.toString();
        assertTrue(output.contains("/n NAME"));
        assertTrue(output.contains("delete INDEX"));
        assertTrue(output.contains("dispense INDEX"));
        assertTrue(output.contains("find KEYWORD"));
        assertTrue(output.contains("view INDEX"));
    }
}
