package seedu.pharmatracker.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ExitCommandTest {
    private Inventory inventory;
    private Ui ui;
    private CustomerList customerList;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        customerList = new CustomerList();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void execute_exitCommand_printsExitMessage() {
        ExitCommand exitCommand = new ExitCommand();
        exitCommand.setShouldExit(false);  // Prevent actual exit during testing
        exitCommand.execute(inventory, ui, customerList);
        
        String output = outContent.toString();
        assertAll("Exit messages should be displayed",
            () -> assertTrue(output.contains("Exiting Application")),
            () -> assertTrue(output.contains("Stay Healthy!"))
        );
    }

    @Test
    void execute_exitCommand_commandWordIsExit() {
        assertNotNull(ExitCommand.COMMAND_WORD);
        assertEquals("exit", ExitCommand.COMMAND_WORD);
    }

    @Test
    void execute_exitCommand_outputNotEmpty() {
        ExitCommand exitCommand = new ExitCommand();
        exitCommand.setShouldExit(false);
        exitCommand.execute(inventory, ui, customerList);
        
        assertTrue(outContent.size() > 0, "Output should not be empty");
    }

    @Test
    void execute_exitCommand_outputContainsHealthMessage() {
        ExitCommand exitCommand = new ExitCommand();
        exitCommand.setShouldExit(false);
        exitCommand.execute(inventory, ui, customerList);
        
        String output = outContent.toString();
        int exitIndex = output.indexOf("Exiting Application");
        int healthIndex = output.indexOf("Stay Healthy!");
        
        assertTrue(exitIndex >= 0, "Should contain exit message");
        assertTrue(healthIndex >= 0, "Should contain health message");
        assertTrue(exitIndex < healthIndex,
                "Exit message should appear before health message");
    }

    @Test
    void execute_exitCommand_withEmptyInventory_stillPrintsMessage() {
        ExitCommand exitCommand = new ExitCommand();
        exitCommand.setShouldExit(false);
        Inventory emptyInventory = new Inventory();
        
        exitCommand.execute(emptyInventory, ui, customerList);
        
        String output = outContent.toString();
        assertTrue(output.contains("Exiting Application"));
    }

    @Test
    void setShouldExit_setToFalse_preventsActualExit() {
        ExitCommand exitCommand = new ExitCommand();
        exitCommand.setShouldExit(false);
        
        // This should not terminate the JVM
        exitCommand.execute(inventory, ui, customerList);
        
        // If we reach here, the test passed (JVM didn't exit)
        assertTrue(true);
    }
}
