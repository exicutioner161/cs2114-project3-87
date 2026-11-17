package main;

// -------------------------------------------------------------------------
/**
 * Main entry point for the application. Asks the instructor to set the
 * session PIN, then hands the session and view to the controller.
 *
 * @author Brian Tran
 * @author Rahul Rajendran (startup PIN prompt)
 * @version Nov 16, 2026
 */
public class OfficeHoursApp {
    /** Prompt shown when the app starts. */
    public static final String PIN_PROMPT = "Set instructor PIN: ";
    /** Shown when the instructor enters a blank PIN. */
    public static final String BLANK_PIN_MESSAGE =
        "The PIN can't be blank. Enter at least one non-space character.";

    // ----------------------------------------------------------
    /**
     * Starts the app on the keyboard and screen.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        start(new ConsoleView());
    }

    // ----------------------------------------------------------
    /**
     * Asks for the session PIN, then runs the main menu. If input ends at
     * the PIN prompt, the app exits without opening the session.
     *
     * @param view view used for all input and output
     * @throws IllegalArgumentException if view is null
     */
    public static void start(ConsoleView view) {
        if (view == null) {
            throw new IllegalArgumentException("View is required.");
        }
        String pin = readPin(view);
        if (pin == null) {
            return;
        }
        OfficeHoursSession session = new OfficeHoursSession(pin);
        view.showMessage("PIN set. Office hours are open.");
        new OfficeHoursController(session, view).run();
    }

    // ----------------------------------------------------------
    /**
     * Reads a PIN, re-prompting until it is not blank. Surrounding spaces
     * are removed so " 1234 " and "1234" are the same PIN.
     *
     * @param view view to read from
     * @return the trimmed PIN, or null if input ended
     */
    private static String readPin(ConsoleView view) {
        while (true) {
            String raw = view.readLine(PIN_PROMPT);
            if (raw == null) {
                return null;
            }
            if (!raw.trim().isEmpty()) {
                return raw.trim();
            }
            view.showMessage(BLANK_PIN_MESSAGE);
        }
    }
}
