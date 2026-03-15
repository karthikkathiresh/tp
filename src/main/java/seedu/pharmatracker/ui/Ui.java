package seedu.pharmatracker.ui;

import java.util.Scanner;

public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private Scanner in;

    public Ui() {
        this.in = new Scanner(System.in);
    }

    public void printWelcomeMessage() {
        String logo =
                "  _____  _                               _______              _\n" +
                        " |  __ \\| |                             |__   __|            | |\n" +
                        " | |__) | |__   __ _ _ __ _ __ ___   __ _  | | _ __ __ _  ___| | _____ _ __\n" +
                        " |  ___/| '_ \\ / _` | '__| '_ ` _ \\ / _` | | || '__/ _` |/ __| |/ / _ \\ '__|\n" +
                        " | |    | | | | (_| | |  | | | | | | (_| | | || | | (_| | (__|   <  __/ |\n" +
                        " |_|    |_| |_|\\__,_|_|  |_| |_| |_|\\__,_| |_||_|  \\__,_|\\___|_|\\_\\___|_|\n";
        System.out.println("Hello from\n" + logo);
        printLineDivider();
        System.out.println("Welcome to Pharma Tracker!");
        System.out.println("What can I do for you today?");
        printLineDivider();
    }

    public void printLineDivider() {
        System.out.println(DIVIDER);
    }

    public String readCommand() {
        System.out.print("Enter command: ");
        return in.nextLine().trim();
    }

    public void printMessage(String message) {
        printLineDivider();
        System.out.println(message);
        printLineDivider();
    }
}
