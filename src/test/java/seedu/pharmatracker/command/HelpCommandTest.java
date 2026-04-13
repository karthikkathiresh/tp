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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;

public class HelpCommandTest {
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
    void execute_helpCommand_doesNotThrow() {
        assertDoesNotThrow(() -> new HelpCommand().execute(inventory, ui, customerList));
    }

    @Test
    void execute_helpCommand_printsAllMedicationFeatures() {
        new HelpCommand().execute(inventory, ui, customerList);
        String output = outContent.toString();
        assertAll("Medication features should be displayed",
            () -> assertTrue(output.contains("Add Medication")),
            () -> assertTrue(output.contains("Delete Medication")),
            () -> assertTrue(output.contains("Dispense Medication")),
            () -> assertTrue(output.contains("List Inventory")),
            () -> assertTrue(output.contains("Find / Search Medication")),
            () -> assertTrue(output.contains("View Medication Details")),
            () -> assertTrue(output.contains("Sort by Expiry Date")),
            () -> assertTrue(output.contains("Print Description Label")),
            () -> assertTrue(output.contains("Update Medication")),
            () -> assertTrue(output.contains("Restock Medication")),
            () -> assertTrue(output.contains("Low Stock Alert")),
            () -> assertTrue(output.contains("Expiring Medications"))
        );
    }

    @Test
    void execute_helpCommand_printsAllCustomerFeatures() {
        new HelpCommand().execute(inventory, ui, customerList);
        String output = outContent.toString();
        assertAll("Customer features should be displayed",
            () -> assertTrue(output.contains("Add Customer")),
            () -> assertTrue(output.contains("Delete Customer")),
            () -> assertTrue(output.contains("Update Customer")),
            () -> assertTrue(output.contains("List Customers")),
            () -> assertTrue(output.contains("Find Customer")),
            () -> assertTrue(output.contains("View Customer"))
        );
    }

    @Test
    void execute_helpCommand_printsFormats() {
        new HelpCommand().execute(inventory, ui, customerList);
        String output = outContent.toString();
        assertAll("Command formats should be displayed",
            () -> assertTrue(output.contains("/n NAME")),
            () -> assertTrue(output.contains("delete INDEX")),
            () -> assertTrue(output.contains("dispense INDEX /q QUANTITY")),
            () -> assertTrue(output.contains("/c CUSTOMER_INDEX")),
            () -> assertTrue(output.contains("find KEYWORD")),
            () -> assertTrue(output.contains("view INDEX")),
            () -> assertTrue(output.contains("add-customer /id CUSTOMER_ID /n NAME /p PHONE")),
            () -> assertTrue(output.contains("delete-customer INDEX")),
            () -> assertTrue(output.contains("update-customer INDEX [/n NAME] [/p PHONE]")),
            () -> assertTrue(output.contains("list-customers")),
            () -> assertTrue(output.contains("find-customer KEYWORD")),
            () -> assertTrue(output.contains("view-customer INDEX")),
            () -> assertTrue(output.contains("restock INDEX /q QUANTITY")),
            () -> assertTrue(output.contains("lowstock")),
            () -> assertTrue(output.contains("expiring")),
            () -> assertTrue(output.contains("label INDEX"))
        );
    }

    @Test
    void execute_helpCommand_printsGeneralCommands() {
        new HelpCommand().execute(inventory, ui, customerList);
        String output = outContent.toString();
        assertAll("General commands should be displayed",
            () -> assertTrue(output.contains("Viewing Help")),
            () -> assertTrue(output.contains("Exiting the Program")),
            () -> assertTrue(output.contains("help")),
            () -> assertTrue(output.contains("exit"))
        );
    }

    @Test
    void execute_helpCommand_emptyInventory() {
        Inventory emptyInventory = new Inventory();
        new HelpCommand().execute(emptyInventory, ui, customerList);
        String output = outContent.toString();
        assertTrue(output.contains("PharmaTracker"));
    }

    @Test
    void execute_helpCommand_emptyCustomerList() {
        CustomerList emptyCustomerList = new CustomerList();
        new HelpCommand().execute(inventory, ui, emptyCustomerList);
        String output = outContent.toString();
        assertTrue(output.contains("PharmaTracker"));
    }
}
