package studenttest;
//package student;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import student.StudentStatus;

public class StudentStatusTest
{
    @Test
    public void testValues()
    {
        StudentStatus[] statuses = StudentStatus.values();

        assertEquals(4, statuses.length);
        assertEquals(StudentStatus.WAITING, statuses[0]);
        assertEquals(StudentStatus.BEING_HELPED, statuses[1]);
        assertEquals(StudentStatus.SERVED, statuses[2]);
        assertEquals(StudentStatus.CANCELLED, statuses[3]);
    }

    @Test
    public void testValueOf()
    {
        assertEquals(StudentStatus.WAITING,
            StudentStatus.valueOf("WAITING"));

        assertEquals(StudentStatus.BEING_HELPED,
            StudentStatus.valueOf("BEING_HELPED"));

        assertEquals(StudentStatus.SERVED,
            StudentStatus.valueOf("SERVED"));

        assertEquals(StudentStatus.CANCELLED,
            StudentStatus.valueOf("CANCELLED"));
    }

    @Test
    public void testInvalidValueOf()
    {
        assertThrows(IllegalArgumentException.class,
            () -> StudentStatus.valueOf("INVALID"));
    }
}