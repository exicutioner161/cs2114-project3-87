package studenttest;
//package student;

import static org.junit.Assert.*;
import org.junit.Test;

public class StudentStatusTest
{
    @Test
    public void testValues()
    {
        StudentStatus[] statuses = StudentStatus.values();

        assertEquals(4, statuses.length);
        assertEquals(StudentStatus.WAITING, statuses[0]);
        assertEquals(StudentStatus.BEING_HELPED, statuses[1]);
        assertEquals(StudentStatus.COMPLETED, statuses[2]);
        assertEquals(StudentStatus.CANCELLED, statuses[3]);
    }

    @Test
    public void testValueOf()
    {
        assertEquals(StudentStatus.WAITING,
            StudentStatus.valueOf("WAITING"));

        assertEquals(StudentStatus.BEING_HELPED,
            StudentStatus.valueOf("BEING_HELPED"));

        assertEquals(StudentStatus.COMPLETED,
            StudentStatus.valueOf("COMPLETED"));

        assertEquals(StudentStatus.CANCELLED,
            StudentStatus.valueOf("CANCELLED"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueOf()
    {
        StudentStatus.valueOf("INVALID");
    }
}