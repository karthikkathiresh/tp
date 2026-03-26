package seedu.pharmatracker.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.data.CustomerList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitCommandTest {
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
    void execute_exitCommand_printsExitMessage() {
        try {
            new ExitCommand().execute(inventory, ui, customerList);
        } catch (SecurityException e) {
            // expected if SecurityManager blocks System.exit
        }
        String output = outContent.toString();
        assertTrue(output.contains("Exiting Application"));
        assertTrue(output.contains("Stay Healthy!"));
    }
}
