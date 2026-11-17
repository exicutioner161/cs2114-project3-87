package maintest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.OfficeHoursSession;
import student.HelpType;
import student.StudentRequest;
import student.StudentStatus;

public class OfficeHoursSessionTest {

    @Test
    public void constructorAcceptsPinAndRejectsMissingPins() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        assertTrue(session.checkPin("42"));
        assertThrows(IllegalArgumentException.class,
            () -> new OfficeHoursSession(null));
        assertThrows(IllegalArgumentException.class,
            () -> new OfficeHoursSession("   "));
    }

    @Test
    public void joinLineCreatesWaitingRequestAndAssignsTicketsInOrder() {
        OfficeHoursSession session = new OfficeHoursSession("42");

        assertEquals(1, session.joinLine("Alex", HelpType.CONCEPT));
        assertEquals(2, session.joinLine("Blair", HelpType.DEBUGGING));
        assertEquals(2, session.getWaitingStudents().length);
        assertEquals("Alex", session.getWaitingStudents()[0].getName());
        assertEquals(StudentStatus.WAITING,
            session.getWaitingStudents()[0].getStatus());
    }

    @Test
    public void joinLineRejectsMissingNameOrHelpType() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        assertThrows(IllegalArgumentException.class,
            () -> session.joinLine(null, HelpType.CONCEPT));
        assertThrows(IllegalArgumentException.class,
            () -> session.joinLine("   ", HelpType.SETUP));
        assertThrows(IllegalArgumentException.class,
            () -> session.joinLine("Alex", null));
        assertTrue(session.isLineEmpty());
    }

    @Test
    public void getPositionReturnsWaitingPosition() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Alex", HelpType.CONCEPT);
        int second = session.joinLine("Blair", HelpType.DEBUGGING);

        assertEquals(1, session.getPosition(first));
        assertEquals(2, session.getPosition(second));
    }

    @Test
    public void getPositionRejectsUnknownCancelledOrNegativeTickets() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Alex", HelpType.CONCEPT);
        session.cancel(ticket);

        assertThrows(IllegalArgumentException.class,
            () -> session.getPosition(ticket));
        assertThrows(IllegalArgumentException.class,
            () -> session.getPosition(0));
        assertThrows(IllegalArgumentException.class,
            () -> session.getPosition(-1));
    }

    /** Counts a waiting request after cancelled entries are skipped. */
    @Test
    public void getPositionSkipsCancelledEntries() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int cancelled = session.joinLine("Alex", HelpType.CONCEPT);
        int waiting = session.joinLine("Blair", HelpType.DEBUGGING);
        session.cancel(cancelled);

        assertEquals(1, session.getPosition(waiting));
    }

    @Test
    public void cancelMarksWaitingRequestCancelled() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int ticket = session.joinLine("Alex", HelpType.CONCEPT);
        StudentRequest request = session.getWaitingStudents()[0];

        session.cancel(ticket);

        assertTrue(session.isLineEmpty());
        assertEquals(StudentStatus.CANCELLED, request.getStatus());
    }

    @Test
    public void cancelRejectsUnknownCancelledAndCurrentRequests() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int cancelled = session.joinLine("Alex", HelpType.CONCEPT);
        int current = session.joinLine("Blair", HelpType.DEBUGGING);
        session.cancel(cancelled);

        assertThrows(IllegalArgumentException.class,
            () -> session.cancel(cancelled));
        assertThrows(IllegalArgumentException.class,
            () -> session.cancel(999));
        session.callNext();
        assertThrows(IllegalArgumentException.class,
            () -> session.cancel(current));
    }

    @Test
    public void checkPinRequiresAnExactMatchingNonNullPin() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        assertTrue(session.checkPin("42"));
        assertFalse(session.checkPin(" 42 "));
        assertFalse(session.checkPin("123"));
        assertFalse(session.checkPin(null));
    }

    @Test
    public void callNextSetsCurrentAndSkippedCancelledStudentsAreRemoved() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Alex", HelpType.CONCEPT);
        int second = session.joinLine("Blair", HelpType.DEBUGGING);
        session.cancel(first);

        StudentRequest called = session.callNext();

        assertEquals(second, called.getTicket());
        assertSame(called, session.getCurrentStudent());
        assertEquals(StudentStatus.BEING_HELPED, called.getStatus());
    }

    @Test
    public void callNextRejectsBusyAndEmptySessions() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        session.joinLine("Alex", HelpType.CONCEPT);
        session.callNext();
        IllegalStateException busy = assertThrows(
            IllegalStateException.class, session::callNext);
        assertEquals("Complete the current student first.", busy.getMessage());

        session.completeCurrent();
        IllegalStateException empty = assertThrows(
            IllegalStateException.class, session::callNext);
        assertEquals("The line is empty.", empty.getMessage());
    }

    /** Removes every cancelled entry before reporting an empty line. */
    @Test
    public void callNextRejectsWhenAllStudentsAreCancelled() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Alex", HelpType.CONCEPT);
        int second = session.joinLine("Blair", HelpType.DEBUGGING);
        session.cancel(first);
        session.cancel(second);

        IllegalStateException error = assertThrows(
            IllegalStateException.class, session::callNext);

        assertEquals("The line is empty.", error.getMessage());
        assertTrue(session.isLineEmpty());
    }

    @Test
    public void completeCurrentServesAndClearsCurrentRequest() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        session.joinLine("Alex", HelpType.CONCEPT);
        StudentRequest called = session.callNext();

        StudentRequest completed = session.completeCurrent();

        assertSame(called, completed);
        assertEquals(StudentStatus.SERVED, completed.getStatus());
        assertNull(session.getCurrentStudent());
    }

    @Test
    public void completeCurrentRejectsIdleSession() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        IllegalStateException error = assertThrows(
            IllegalStateException.class, session::completeCurrent);
        assertEquals("No student is being helped.", error.getMessage());
    }

    @Test
    public void getCurrentStudentReturnsNullOrTheHelpedRequest() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        assertNull(session.getCurrentStudent());
        session.joinLine("Alex", HelpType.CONCEPT);
        StudentRequest called = session.callNext();
        assertSame(called, session.getCurrentStudent());
    }

    @Test
    public void getWaitingStudentsReturnsArrivalOrderAndExcludesCancelled() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Alex", HelpType.CONCEPT);
        int second = session.joinLine("Blair", HelpType.DEBUGGING);
        session.cancel(first);

        StudentRequest[] waiting = session.getWaitingStudents();

        assertEquals(1, waiting.length);
        assertEquals(second, waiting[0].getTicket());
        assertEquals("Blair", waiting[0].getName());
    }

    @Test
    public void isLineEmptyTracksWaitingRequests() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        assertTrue(session.isLineEmpty());
        session.joinLine("Alex", HelpType.CONCEPT);
        assertFalse(session.isLineEmpty());
        session.callNext();
        assertTrue(session.isLineEmpty());
        session.completeCurrent();
        assertTrue(session.isLineEmpty());
    }

    @Test
    public void requeueCurrentMovesSameRequestToBackAsWaiting() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        session.joinLine("Alex", HelpType.CONCEPT);
        session.joinLine("Blair", HelpType.DEBUGGING);
        StudentRequest alex = session.callNext();

        StudentRequest requeued = session.requeueCurrent();

        assertSame(alex, requeued);
        assertNull(session.getCurrentStudent());
        assertEquals(StudentStatus.WAITING, requeued.getStatus());
        StudentRequest[] waiting = session.getWaitingStudents();
        assertEquals(2, waiting.length);
        assertEquals("Blair", waiting[0].getName());
        assertEquals("Alex", waiting[1].getName());
        assertEquals(alex.getTicket(), waiting[1].getTicket());
        assertEquals("Blair", session.callNext().getName());
    }

    @Test
    public void requeueCurrentRejectsIdleSession() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        IllegalStateException error = assertThrows(
            IllegalStateException.class, session::requeueCurrent);
        assertEquals("No student is being helped.", error.getMessage());
    }

    @Test
    public void cancelledStudentsAreSkipped() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Alex", HelpType.CONCEPT);
        session.joinLine("Blair", HelpType.DEBUGGING);
        session.cancel(first);
        assertEquals("Blair", session.callNext().getName());
    }
}
