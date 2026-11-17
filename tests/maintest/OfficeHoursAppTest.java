package maintest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import main.ConsoleView;
import main.OfficeHoursApp;

// -------------------------------------------------------------------------
/**
 * Tests the startup PIN prompt in OfficeHoursApp. Each test feeds fixed
 * input through ConsoleView's test constructor and checks the output, so
 * no keyboard is needed.
 *
 * @author Rahul Rajendran
 * @version Nov 16, 2026
 */
public class OfficeHoursAppTest {

    /** Executes the app's implicit no-argument constructor. */
    @Test
    public void appCanBeConstructed() {
        assertNotNull(new OfficeHoursApp());
    }

    /**
     * The application entry point uses the default console view and can quit
     * immediately when its standard input contains the quit choice.
     */
    @Test
    public void mainUsesDefaultConsoleView() {
        java.io.InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream("9876\n5\n".getBytes()));
            System.setOut(new PrintStream(output));

            OfficeHoursApp.main(new String[0]);

            assertTrue(output.toString().contains("PIN set. Office hours are open."));
            assertTrue(output.toString().contains("5. Quit"));
        }
        finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
    }

    /**
     * Runs the app on the given input and returns everything it printed.
     */
    private String run(String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner(input),
            new PrintStream(output));
        OfficeHoursApp.start(view);
        return output.toString();
    }

    /** Counts how many times target appears in text. */
    private int count(String text, String target) {
        int total = 0;
        int index = text.indexOf(target);
        while (index >= 0) {
            total++;
            index = text.indexOf(target, index + target.length());
        }
        return total;
    }

    /**
     * A valid PIN opens the session and shows the main menu.
     */
    @Test
    public void validPinOpensSession() {
        String out = run("9876\n5\n");

        assertEquals(1, count(out, OfficeHoursApp.PIN_PROMPT));
        assertTrue(out.contains("PIN set. Office hours are open."));
        assertTrue(out.contains("5. Quit"));
    }

    /**
     * The PIN set at startup is the one the instructor menu accepts.
     */
    @Test
    public void startupPinUnlocksInstructorMenu() {
        // set PIN, open instructor menu, enter PIN, show line, quit
        String out = run("9876\n4\n9876\n3\n5\n");

        assertFalse(out.contains("Access denied."));
        assertTrue(out.contains("The line is empty."));
    }

    /**
     * The old hard-coded PIN no longer works once a different PIN is set.
     */
    @Test
    public void oldDefaultPinIsRejected() {
        String out = run("9876\n4\n1234\n5\n");

        assertTrue(out.contains("Access denied."));
    }

    /**
     * Spaces around the PIN are trimmed, so the trimmed PIN unlocks.
     */
    @Test
    public void pinIsTrimmed() {
        String out = run("  9876  \n4\n9876\n3\n5\n");

        assertFalse(out.contains("Access denied."));
        assertTrue(out.contains("The line is empty."));
    }

    /**
     * Bad input: an empty PIN re-prompts instead of crashing.
     */
    @Test
    public void emptyPinReprompts() {
        String out = run("\n9876\n5\n");

        assertEquals(1, count(out, OfficeHoursApp.BLANK_PIN_MESSAGE));
        assertEquals(2, count(out, OfficeHoursApp.PIN_PROMPT));
        assertTrue(out.contains("5. Quit"));
    }

    /**
     * Bad input: a spaces-only PIN re-prompts, every time it is entered.
     */
    @Test
    public void spacesOnlyPinRepromptsEachTime() {
        String out = run("   \n\t\n9876\n5\n");

        assertEquals(2, count(out, OfficeHoursApp.BLANK_PIN_MESSAGE));
        assertEquals(3, count(out, OfficeHoursApp.PIN_PROMPT));
    }

    /**
     * Bad input: input ending at the PIN prompt exits cleanly and never
     * opens the session.
     */
    @Test
    public void endOfInputAtPinPromptExits() {
        String out = run("");

        assertTrue(out.contains(OfficeHoursApp.PIN_PROMPT));
        assertFalse(out.contains("5. Quit"));
    }

    /**
     * Bad input: input ending after blank PINs also exits cleanly.
     */
    @Test
    public void endOfInputAfterBlankPinExits() {
        String out = run("\n");

        assertTrue(out.contains(OfficeHoursApp.BLANK_PIN_MESSAGE));
        assertFalse(out.contains("5. Quit"));
    }

    /**
     * Bad input: starting the app with no view is rejected.
     */
    @Test
    public void nullViewIsRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> OfficeHoursApp.start(null));
    }
}
