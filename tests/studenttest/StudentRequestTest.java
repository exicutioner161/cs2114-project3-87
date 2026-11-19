package studenttest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import student.*;


public class StudentRequestTest {

    @Test
    public void testConstructor() {
        StudentRequest request = new StudentRequest(
            1, "Zayd", HelpType.DEBUGGING);

        assertEquals(1, request.getTicket());
        assertEquals("Zayd", request.getName());
        assertEquals(HelpType.DEBUGGING,
            request.getHelpType());
        assertEquals(StudentStatus.WAITING,
            request.getStatus());
    }

    @Test
    public void testGetTicket() {
        StudentRequest request = new StudentRequest(
            5, "John", HelpType.CONCEPT);

        assertEquals(5, request.getTicket());
    }

    @Test
    public void testGetName() {
        StudentRequest request = new StudentRequest(
            2, "Alex", HelpType.SETUP);

        assertEquals("Alex", request.getName());
    }

    @Test
    public void testGetHelpType() {
        StudentRequest request = new StudentRequest(
            3, "Sam", HelpType.OTHER);

        assertEquals(HelpType.OTHER,
            request.getHelpType());
    }

    @Test
    public void testSetStatus() {
        StudentRequest request = new StudentRequest(
            4, "Zayd", HelpType.DEBUGGING);

        request.setStatus(StudentStatus.BEING_HELPED);

        assertEquals(StudentStatus.BEING_HELPED,
            request.getStatus());
    }

    @Test
    public void testSetStatusServed() {
        StudentRequest request = new StudentRequest(
            6, "Alex", HelpType.CONCEPT);

        request.setStatus(StudentStatus.SERVED);

        assertEquals(StudentStatus.SERVED,
            request.getStatus());
    }

    @Test
    public void testSetStatusCancelled() {
        StudentRequest request = new StudentRequest(
            7, "John", HelpType.SETUP);

        request.setStatus(StudentStatus.CANCELLED);

        assertEquals(StudentStatus.CANCELLED,
            request.getStatus());
    }

    @Test
    public void testNullName() {
        assertThrows(IllegalArgumentException.class,
            () -> new StudentRequest(1, null, HelpType.DEBUGGING));
    }

    @Test
    public void testNullHelpType() {
        assertThrows(IllegalArgumentException.class,
            () -> new StudentRequest(1, "Zayd", null));
    }

    @Test
    public void testNullStatus() {
        StudentRequest request = new StudentRequest(
            1, "Zayd", HelpType.DEBUGGING);

        assertThrows(IllegalArgumentException.class,
            () -> request.setStatus(null));
    }
}

