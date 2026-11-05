package main;

public class OfficeHoursApp {
    public static void main(String[] args) {
        // Creates the session, view, and controller, then calls run()
        OfficeHoursSession session = new OfficeHoursSession();
        ConsoleView view = new ConsoleView();
        OfficeHoursController controller = new OfficeHoursController(session, view);
        controller.run();
    }
}
