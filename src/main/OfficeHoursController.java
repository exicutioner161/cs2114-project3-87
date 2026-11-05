package main;

public class OfficeHoursController {
    private final OfficeHoursSession session;
    private final ConsoleView view;

    public OfficeHoursController(OfficeHoursSession session, ConsoleView view) {
        checkForNull(session, view);
        this.session = session;
        this.view = view;
    }

    private void checkForNull(OfficeHoursSession session, ConsoleView view) {
        if (session == null && view == null) {
            throw new IllegalArgumentException("Session and view are both null!");
        }
        if (session == null) {
            throw new IllegalArgumentException("Session is null!");
        }
        if (view == null) {
            throw new IllegalArgumentException("View is null!");
        }
    }

    public void run() {
        // Runs the main menu loop until input ends

    }
}
