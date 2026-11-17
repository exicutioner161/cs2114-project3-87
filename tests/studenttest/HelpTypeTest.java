package studenttest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import student.HelpType;

public class HelpTypeTest {
     @Test
    public void testValues()
    {
        HelpType[] types = HelpType.values();

        assertEquals(4, types.length);
        assertEquals(HelpType.DEBUGGING, types[0]);
        assertEquals(HelpType.CONCEPT, types[1]);
        assertEquals(HelpType.SETUP, types[2]);
        assertEquals(HelpType.OTHER, types[3]);
    }

    @Test
    public void testGetLabel()
    {
        assertEquals("Other",
            HelpType.OTHER.getLabel());

        assertEquals("Concept",
            HelpType.CONCEPT.getLabel());

        assertEquals("Debug",
            HelpType.DEBUGGING.getLabel());

        assertEquals("Setup",
            HelpType.SETUP.getLabel());
    }

    @Test
    public void testGetMinutes()
    {
        assertEquals(7, HelpType.OTHER.getMinutes());
        assertEquals(5, HelpType.CONCEPT.getMinutes());
        assertEquals(10, HelpType.DEBUGGING.getMinutes());
        assertEquals(8, HelpType.SETUP.getMinutes());
    }

    @Test
    public void testValuesArrayIndependence()
    {
        HelpType[] types = HelpType.values();

        types[0] = HelpType.SETUP;

        assertEquals(HelpType.DEBUGGING,
            HelpType.values()[0]);
    }
}
