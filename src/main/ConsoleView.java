package main;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Reads text and shows text. This class does not use the model.
 * Drafted with Codex for Zhaojin Gao's model/view boundary work.
 */
public class ConsoleView {
    private final Scanner in;
    private final PrintStream out;

    /** Creates a view for the keyboard and screen. */
    public ConsoleView() {
        this(new Scanner(System.in), System.out);
    }

    /** Allows tests to use text input and captured output. */
    public ConsoleView(Scanner input, PrintStream output) {
        if (input == null || output == null) {
            throw new IllegalArgumentException("Input and output are required.");
        }
        in = input;
        out = output;
    }

    /** Returns the next raw line, or null when input ends. */
    public String readLine() {
        if (in.hasNextLine()) {
            return in.nextLine();
        }
        return null;
    }

    /** Shows a prompt and reads one line. */
    public String readLine(String prompt) {
        out.print(prompt);
        out.flush();
        return readLine();
    }

    /** Shows text prepared by the controller. */
    public void showMessage(String message) {
        out.println(message);
    }

    /** The controller provides the menu, so options stay in one place. */
    public void showMenu(String menu) {
        showMessage(menu);
    }

    /** The controller converts the waiting line to text. */
    public void showLine(String lineText) {
        showMessage(lineText);
    }

    /** The controller gets the position from the model and formats it. */
    public void showPosition(String positionText) {
        showMessage(positionText);
    }
}
