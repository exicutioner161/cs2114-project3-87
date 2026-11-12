package main;

// -------------------------------------------------------------------------
/**
 * Main entry point for the application
 * 
 * @author Brian Tran
 * @version Nov 11, 2026
 */
public class OfficeHoursApp {
    public static void main(String[] args) {
        // Creates the session, view, and controller, then calls run()
        OfficeHoursSession session = new OfficeHoursSession();
        ConsoleView view = new ConsoleView();
        OfficeHoursController controller = new OfficeHoursController(session, view);
        controller.run();
    }
}
