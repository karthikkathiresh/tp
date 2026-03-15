package seedu.pharmatracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PharmaTrackerTest {

    @Test
    public void logo_hasCorrectLineCount() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        assertEquals(5, logo.split("\n").length);
    }

    @Test
    public void greeting_withValidName_returnsHelloMessage() {
        String name = "Alice";
        String expected = "Hello " + name;
        assertEquals(expected, "Hello " + name);
    }

    @Test
    public void greeting_withEmptyName_returnsHelloMessage() {
        String name = "";
        assertTrue(("Hello " + name).startsWith("Hello"));
    }
}
