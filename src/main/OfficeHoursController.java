package main;

import student.HelpType;
import student.StudentRequest;

// -------------------------------------------------------------------------
/**
 * Converts console input into session operations and displays their results.
 * 
 * @author Brian Tran
 * @version Nov 11, 2026
 */
public class OfficeHoursController {
    private static final String MENU = """
            1. Join the line
            2. Check position
            3. Cancel request
            4. Instructor menu
            5. Quit""";
    private final OfficeHoursSession session;
    private final ConsoleView view;

    /**
     * Connects a session to a console view.
     * 
     * @param session model to operate on
     * @param view    view used for input and output
     */
    public OfficeHoursController(OfficeHoursSession session, ConsoleView view) {
        if (session == null || view == null) {
            throw new IllegalArgumentException("Session and view are required.");
        }
        this.session = session;
        this.view = view;
    }

    /** Runs the main menu until the input stream ends or the user quits. */
    public void run() {
        while (true) {
            view.showMenu(MENU);
            String choice = view.readLine("Choice: ");
            if (choice == null || "5".equals(choice.trim())) {
                return;
            }
            if (!handleMainChoice(choice)) {
                invalidInputMessage();
            }
        }
    }

    private boolean handleMainChoice(String rawChoice) {
        int choice;
        try {
            choice = Integer.parseInt(rawChoice.trim());
        } catch (NumberFormatException error) {
            return false;
        }
        try {
            switch (choice) {
                case 1 -> join();
                case 2 -> showPosition();
                case 3 -> cancel();
                case 4 -> instructorMenu();
                default -> {
                    return false;
                }
            }
            return true;
        } catch (IllegalArgumentException error) {
            view.showMessage(error.getMessage());
            return true;
        }
    }

    private void join() {
        String name;
        while (true) {
            name = view.readLine("Name: ");
            if (name == null) {
                return;
            }
            if (!name.trim().isEmpty()) {
                break;
            }
            view.showMessage("Name can't be blank. Enter a name.");
        }
        HelpType type = readHelpType();
        if (type == null) {
            return;
        }
        try {
            int ticket = session.joinLine(name, type);
            view.showMessage("Your ticket is " + ticket + ".");
        } catch (IllegalArgumentException error) {
            view.showMessage(error.getMessage());
        }
    }

    private HelpType readHelpType() {
        HelpType[] types = HelpType.values();
        while (true) {
            view.showMessage("Select help type:");
            for (int i = 0; i < types.length; i++) {
                view.showMessage((i + 1) + ". " + types[i].getLabel());
            }
            String raw = view.readLine("Choice: ");
            if (raw == null) {
                return null;
            }
            try {
                int choice = Integer.parseInt(raw.trim());
                if (choice >= 1 && choice <= types.length) {
                    return types[choice - 1];
                }
            } catch (NumberFormatException error) {
                // The same message applies to non-numeric and out-of-range input.
            }
            view.showMessage("Invalid help type.");
        }
    }

    private void showPosition() {
        while (true) {
            Integer ticket = readTicket();
            if (ticket == null) {
                return;
            }
            try {
                view.showPosition("Your position is "
                        + session.getPosition(ticket) + ".");
                return;
            } catch (IllegalArgumentException error) {
                view.showMessage(error.getMessage() + " Enter a waiting ticket.");
            }
        }
    }

    private void cancel() {
        while (true) {
            Integer ticket = readTicket();
            if (ticket == null) {
                return;
            }
            try {
                session.cancel(ticket);
                view.showMessage("Request cancelled.");
                return;
            } catch (IllegalArgumentException error) {
                view.showMessage(error.getMessage() + " Enter a waiting ticket.");
            }
        }
    }

    private Integer readTicket() {
        while (true) {
            String raw = view.readLine("Ticket: ");
            if (raw == null) {
                return null;
            }
            try {
                int ticket = Integer.parseInt(raw.trim());
                if (ticket > 0) {
                    return ticket;
                }
            } catch (NumberFormatException error) {
                // Invalid ticket messages below cover all bad formats.
            }
            view.showMessage("Invalid ticket. Enter a positive ticket number.");
        }
    }

    private void instructorMenu() {
        String attempt = view.readLine("PIN: ");
        if (attempt == null) {
            return;
        }
        if (!session.checkPin(attempt)) {
            view.showMessage("Access denied.");
            return;
        }
        while (true) {
            int action = getInstructorInput();
            if (action == -1 || action == 5) {
                return;
            }
            executeInstructorAction(action);
        }
    }

    private int getInstructorInput() {
        while (true) {
            String choice = view.readLine("""
                    1. Call next
                    2. Complete current
                    3. Show line
                    4. Requeue current
                    5. Back
                    ------------------
                    """);
            if (choice == null) {
                return -1;
            }
            try {
                int action = Integer.parseInt(choice.trim());
                return action;
            } catch (NumberFormatException error) {
                invalidInputMessage();
            }
        }
    }

    private void executeInstructorAction(int action) {
        try {
            switch (action) {
                case 1 -> view.showMessage(format(session.callNext()));
                case 2 -> view.showMessage(format(session.completeCurrent()));
                case 3 -> showLine();
                case 4 -> view.showMessage(format(session.requeueCurrent()));
                default -> invalidInputMessage();
            }
        } catch (IllegalStateException error) {
            view.showMessage(error.getMessage());
        }
    }

    private void showLine() {
        StudentRequest[] waiting = session.getWaitingStudents();
        if (waiting.length == 0) {
            view.showLine("The line is empty.");
            return;
        }
        for (int i = 0; i < waiting.length; i++) {
            view.showLine((i + 1) + ". " + waiting[i].getTicket() + " "
                    + waiting[i].getName());
        }
    }

    private String format(StudentRequest request) {
        return "Ticket " + request.getTicket() + ": " + request.getName();
    }

    private void invalidInputMessage() {
        view.showMessage("Invalid input! Try again.");
    }
}
