package maintest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import main.ConsoleView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/** Tests the view without a model, controller, or keyboard. */
public class ConsoleViewTest {
    @Test
    public void testReadsWholeName() {
        ConsoleView view = new ConsoleView(new Scanner("Gao Zhaojin\n"), System.out);
        assertEquals("Gao Zhaojin", view.readLine());
        assertNull(view.readLine());
    }

    @Test
    public void testKeepsRawInputForController() {
        ConsoleView view = new ConsoleView(new Scanner("  Gao  \nabc\n"), System.out);
        assertEquals("  Gao  ", view.readLine());
        assertEquals("abc", view.readLine());
    }

    @Test
    public void testBlankLineIsNotEndOfInput() {
        ConsoleView view = new ConsoleView(new Scanner("\nGao\n"), System.out);
        assertEquals("", view.readLine());
        assertEquals("Gao", view.readLine());
        assertNull(view.readLine());
    }

    @Test
    public void testEmptyInputReturnsNull() {
        ConsoleView view = new ConsoleView(new Scanner(""), System.out);
        assertNull(view.readLine());
        assertNull(view.readLine());
    }

    @Test
    public void testReadsLastLineWithoutNewline() {
        ConsoleView view = new ConsoleView(new Scanner("Gao"), System.out);
        assertEquals("Gao", view.readLine());
        assertNull(view.readLine());
    }

    @Test
    public void testShowsPromptBeforeReading() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner("Gao\n"), new PrintStream(output));
        assertEquals("Gao", view.readLine("Name: "));
        assertEquals("Name: ", output.toString());
    }

    @Test
    public void testShowsControllerTextInOrder() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner(""), new PrintStream(output));
        view.showMenu("1. Join");
        view.showLine("1. Gao");
        view.showPosition("Your position is 1.");
        view.showMessage("Done.");
        String newline = System.lineSeparator();
        assertEquals("1. Join" + newline + "1. Gao" + newline
            + "Your position is 1." + newline + "Done." + newline, output.toString());
    }

    @Test
    public void testRejectsMissingInput() {
        try {
            new ConsoleView(null, System.out);
            fail("Expected an IllegalArgumentException.");
        }
        catch (IllegalArgumentException error) {
            assertEquals("Input and output are required.", error.getMessage());
        }
    }

    @Test
    public void testRejectsMissingOutput() {
        try {
            new ConsoleView(new Scanner(""), null);
            fail("Expected an IllegalArgumentException.");
        }
        catch (IllegalArgumentException error) {
            assertEquals("Input and output are required.", error.getMessage());
        }
    }
}
