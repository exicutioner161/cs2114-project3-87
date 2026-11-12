package maintest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.OfficeHoursSession;
import student.HelpType;
import student.StudentStatus;

public class OfficeHoursSessionTest {

    @Test
    public void cancelledStudentsAreSkipped() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        int first = session.joinLine("Alex", HelpType.CONCEPT);
        session.joinLine("Blair", HelpType.DEBUGGING);
        session.cancel(first);

        assertEquals("Blair", session.callNext().getName());
        assertEquals(StudentStatus.BEING_HELPED,
            session.getCurrentStudent().getStatus());
    }

    @Test
    public void completionAllowsTheNextStudentToBeCalled() {
        OfficeHoursSession session = new OfficeHoursSession("42");
        session.joinLine("Alex", HelpType.CONCEPT);
        session.joinLine("Blair", HelpType.SETUP);

        session.callNext();
        assertEquals(StudentStatus.SERVED, session.completeCurrent().getStatus());
        assertEquals("Blair", session.callNext().getName());
    }

    @Test
    public void invalidPinAndEmptyLineAreHandled() {
        OfficeHoursSession session = new OfficeHoursSession("42");

        assertTrue(!session.checkPin("wrong"));
        assertThrows(IllegalStateException.class, session::callNext);
    }
}
