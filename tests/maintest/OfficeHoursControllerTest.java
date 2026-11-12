package maintest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import main.ConsoleView;
import main.OfficeHoursController;
import main.OfficeHoursSession;

public class OfficeHoursControllerTest {

    @Test
    public void runStopsAtQuit() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner("5\n"),
            new PrintStream(output));

        new OfficeHoursController(new OfficeHoursSession("42"), view).run();

        assertTrue(output.toString().contains("5. Quit"));
    }

    @Test
    public void invalidMainChoiceIsReportedAndReprompts() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner("not-a-choice\n5\n"),
            new PrintStream(output));

        new OfficeHoursController(new OfficeHoursSession("42"), view).run();

        assertTrue(output.toString().contains("Invalid input! Try again."));
    }

    @Test
    public void endOfInputAtPinPromptReturnsCleanly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner("4\n"),
            new PrintStream(output));

        new OfficeHoursController(new OfficeHoursSession("42"), view).run();

        assertTrue(output.toString().contains("PIN: "));
    }
}
