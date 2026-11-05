package main;

import java.util.Scanner;

public class OfficeHoursController {
    private final OfficeHoursSession session;
    private final ConsoleView view;

    public OfficeHoursController(OfficeHoursSession session, ConsoleView view) {
        this.session = session;
        this.view = view;
    }

    public void run() {
        try (Scanner input = new Scanner(System.in)) {
            // Runs the main menu loop until input ends
        }
    }
}
