package maintest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import main.ConsoleView;
import main.OfficeHoursController;
import main.OfficeHoursSession;
import student.HelpType;
import student.StudentRequest;
import student.StudentStatus;

/**
 * Tests console choices and their effects on the session.
 * Additional D4 tests drafted with Codex.
 * Gao personally reviewed the tests and confirmed that he understands them.
 */
public class OfficeHoursControllerTest {

    /**
     * Runs the controller with test input instead of keyboard input.
     * Each newline in input acts like pressing Enter.
     *
     * @param session the model used by the controller
     * @param input the choices and answers typed by the test
     * @return the messages printed by the controller
     */
    private String runInput(OfficeHoursSession session, String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // Scanner reads our test answers. PrintStream saves screen messages.
        ConsoleView view = new ConsoleView(new Scanner(input),
            new PrintStream(output));
        OfficeHoursController controller = new OfficeHoursController(session, view);
        controller.run();
        return output.toString();
    }


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

    @Test
    public void quitDoesNotReadLaterChoices() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        runInput(session, " 5 \n1\nAna\n1\n");
        assertTrue(session.isLineEmpty());
    }

    @Test
    public void textMainChoiceAllowsNextValidChoice() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "hello\n1\nAna\n1\n5\n");
        assertTrue(output.contains("Invalid input! Try again."));
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals("Ana", session.getWaitingStudents()[0].getName());
    }

    @Test
    public void outOfRangeMainChoiceAllowsNextValidChoice() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "9\n1\nAna\n1\n5\n");
        assertTrue(output.contains("Invalid input! Try again."));
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals("Ana", session.getWaitingStudents()[0].getName());
    }

    @Test
    public void blankMainChoiceAllowsNextValidChoice() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "\n1\nAna\n1\n5\n");
        assertTrue(output.contains("Invalid input! Try again."));
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals("Ana", session.getWaitingStudents()[0].getName());
    }

    @Test
    public void joinAddsStudentAndDisplaysTicket() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "1\n Ana \n2\n5\n");
        StudentRequest request = session.getWaitingStudents()[0];
        assertEquals("Ana", request.getName());
        assertEquals(HelpType.CONCEPT, request.getHelpType());
        assertEquals(StudentStatus.WAITING, request.getStatus());
        assertTrue(output.contains("Your ticket is " + request.getTicket() + "."));
        assertTrue(output.contains("1. " + request.getTicket() + " Ana"));
    }

    @Test
    public void blankNameIsRejectedAndNextJoinWorks() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "1\n   \n1\n1\nBen\n1\n5\n");
        assertTrue(output.contains("Name and help type are required."));
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals("Ben", session.getWaitingStudents()[0].getName());
    }

    @Test
    public void invalidHelpTypesRepromptWithoutAddingExtraStudents() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "1\nAna\nabc\n0\n99\n\n3\n5\n");
        assertTrue(output.contains("Invalid help type."));
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(HelpType.SETUP, session.getWaitingStudents()[0].getHelpType());
    }

    @Test
    public void positionDisplaysModelPosition() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        session.joinLine("Ana", HelpType.CONCEPT);
        int ticket = session.joinLine("Ben", HelpType.OTHER);
        String output = runInput(session, "2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Your position is 2."));
        assertEquals(2, session.getWaitingStudents().length);
    }

    @Test
    public void cancelRemovesOnlyChosenWaitingStudent() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        int next = session.joinLine("Ben", HelpType.OTHER);
        String output = runInput(session, "3\n" + ticket + "\n5\n");
        assertTrue(output.contains("Request cancelled."));
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(next, session.getWaitingStudents()[0].getTicket());
        assertEquals(1, session.getPosition(next));
    }

    @Test
    public void positionTextTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "2\nabc\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Invalid ticket."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void positionOverflowTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "2\n2147483648\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Invalid ticket."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void positionUnknownTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "2\n0\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Ticket is not waiting."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void positionNegativeTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "2\n-1\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Ticket is not waiting."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void cancelTextTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "3\nabc\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Invalid ticket."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void cancelOverflowTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "3\n2147483648\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Invalid ticket."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void cancelUnknownTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "3\n0\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Ticket is not waiting."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void cancelNegativeTicketDoesNotChangeLine() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "3\n-1\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Ticket is not waiting."));
        assertTrue(output.contains("Your position is 1."));
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void cancelledTicketCannotBeCancelledOrLocatedAgain() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ana", HelpType.CONCEPT);
        session.cancel(ticket);
        String output = runInput(session, "3\n" + ticket + "\n2\n" + ticket + "\n5\n");
        assertTrue(output.contains("Ticket is not waiting."));
        assertFalse(output.contains("Request cancelled."));
        assertFalse(output.contains("Your position is"));
        assertTrue(session.isLineEmpty());
    }

    @Test
    public void wrongPinDoesNotOpenInstructorMenu() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        session.joinLine("Ana", HelpType.CONCEPT);
        String output = runInput(session, "4\n1234\n5\n");
        assertTrue(output.contains("Access denied."));
        assertFalse(output.contains("1. Call next"));
        assertNull(session.getCurrentStudent());
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void invalidInstructorTextReturnsToMainMenu() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "4\n42\nabc\n1\nAna\n1\n5\n");
        assertTrue(output.contains("Invalid input! Try again."));
        assertEquals(1, session.getWaitingStudents().length);
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void invalidInstructorOutOfRangeReturnsToMainMenu() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "4\n42\n9\n1\nAna\n1\n5\n");
        assertTrue(output.contains("Invalid input! Try again."));
        assertEquals(1, session.getWaitingStudents().length);
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void invalidInstructorBlankReturnsToMainMenu() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "4\n42\n\n1\nAna\n1\n5\n");
        assertTrue(output.contains("Invalid input! Try again."));
        assertEquals(1, session.getWaitingStudents().length);
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void instructorCanCallCompleteAndCallNext() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Ana", HelpType.CONCEPT);
        int second = session.joinLine("Ben", HelpType.OTHER);
        // Keep Ana so we can check her status after she leaves the line.
        StudentRequest ana = session.getWaitingStudents()[0];
        String output = runInput(session, "4\n42\n1\n4\n42\n2\n4\n42\n1\n5\n");
        assertTrue(output.contains("Ticket " + first + ": Ana"));
        assertTrue(output.contains("Ticket " + second + ": Ben"));
        assertEquals(StudentStatus.SERVED, ana.getStatus());
        assertEquals(second, session.getCurrentStudent().getTicket());
        assertEquals(StudentStatus.BEING_HELPED, session.getCurrentStudent().getStatus());
        assertTrue(session.isLineEmpty());
    }

    @Test
    public void callNextWhileBusyKeepsCurrentAndWaitingStudents() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Ana", HelpType.CONCEPT);
        int second = session.joinLine("Ben", HelpType.OTHER);
        session.callNext();
        String output = runInput(session, "4\n42\n1\n5\n");
        assertTrue(output.contains("Complete the current student first."));
        assertEquals(first, session.getCurrentStudent().getTicket());
        assertEquals(second, session.getWaitingStudents()[0].getTicket());
    }

    @Test
    public void emptyInstructorActionsReportErrorsAndAllowRecovery() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "4\n42\n1\n4\n42\n2\n1\nAna\n1\n5\n");
        assertTrue(output.contains("The line is empty."));
        assertTrue(output.contains("No student is being helped."));
        assertNull(session.getCurrentStudent());
        assertEquals(1, session.getWaitingStudents().length);
    }

    @Test
    public void showLineUsesWaitingOrderAndSkipsCancelledStudents() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Ana", HelpType.CONCEPT);
        int cancelled = session.joinLine("Ben", HelpType.OTHER);
        int last = session.joinLine("Cam", HelpType.SETUP);
        session.cancel(cancelled);
        String output = runInput(session, "4\n42\n3\n5\n");
        String firstRow = "1. " + first + " Ana";
        String lastRow = "2. " + last + " Cam";
        assertTrue(output.contains(firstRow));
        assertTrue(output.contains(lastRow));
        // A smaller index means the first student was printed earlier.
        assertTrue(output.indexOf(firstRow) < output.indexOf(lastRow));
        assertFalse(output.contains("Ben"));
        assertEquals(2, session.getWaitingStudents().length);
    }

    @Test
    public void showEmptyLineAndBackDoNotChangeSession() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        String output = runInput(session, "4\n42\n3\n4\n42\n4\n5\n");
        assertTrue(output.contains("The line is empty."));
        assertFalse(output.contains("Invalid input!"));
        assertTrue(session.isLineEmpty());
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void endOfInputAtMainKeepsSessionUnchanged() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ben", HelpType.OTHER);
        runInput(session, "");
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(ticket, session.getWaitingStudents()[0].getTicket());
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void endOfInputAtNameKeepsSessionUnchanged() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ben", HelpType.OTHER);
        runInput(session, "1\n");
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(ticket, session.getWaitingStudents()[0].getTicket());
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void endOfInputAtHelpTypeKeepsSessionUnchanged() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ben", HelpType.OTHER);
        runInput(session, "1\nAna\n");
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(ticket, session.getWaitingStudents()[0].getTicket());
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void endOfInputAtPositionTicketKeepsSessionUnchanged() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ben", HelpType.OTHER);
        runInput(session, "2\n");
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(ticket, session.getWaitingStudents()[0].getTicket());
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void endOfInputAtCancelTicketKeepsSessionUnchanged() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ben", HelpType.OTHER);
        runInput(session, "3\n");
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(ticket, session.getWaitingStudents()[0].getTicket());
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void endOfInputAtInstructorChoiceKeepsSessionUnchanged() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Ben", HelpType.OTHER);
        runInput(session, "4\n42\n");
        assertEquals(1, session.getWaitingStudents().length);
        assertEquals(ticket, session.getWaitingStudents()[0].getTicket());
        assertNull(session.getCurrentStudent());
    }
}
