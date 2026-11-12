package studenttest;

import static org.junit.Assert.*;
import org.junit.Test;
import student.HelpType;

public class HelpTypeTest {
     @Test
    public void testValues()
    {
        HelpType[] types = HelpType.values();

        assertEquals(4, types.length);
        assertEquals(HelpType.OTHER, types[0]);
        assertEquals(HelpType.CONCEPT, types[1]);
        assertEquals(HelpType.DEBUGGING, types[2]);
        assertEquals(HelpType.SETUP, types[3]);
    }

    @Test
    public void testGetLabel()
    {
        assertEquals("Quick Question",
            HelpType.OTHER.getLabel());

        assertEquals("Concept",
            HelpType.CONCEPT.getLabel());

        assertEquals("Debugging",
            HelpType.DEBUGGING.getLabel());

        assertEquals("Setup",
            HelpType.SETUP.getLabel());
    }

    @Test
    public void testGetMinutes()
    {
        assertEquals(5, HelpType.OTHER.getMinutes());
        assertEquals(10, HelpType.CONCEPT.getMinutes());
        assertEquals(15, HelpType.DEBUGGING.getMinutes());
        assertEquals(5, HelpType.SETUP.getMinutes());
    }

    @Test
    public void testValuesArrayIndependence()
    {
        HelpType[] types = HelpType.values();

        types[0] = HelpType.SETUP;

        assertEquals(HelpType.OTHER,
            HelpType.values()[0]);
    }
}
