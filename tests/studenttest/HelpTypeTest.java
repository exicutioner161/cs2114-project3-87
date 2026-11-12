package studenttest;

import static org.junit.Assert.*;
import org.junit.Test;

public class HelpTypeTest {
     @Test
    public void testValues()
    {
        HelpType[] types = HelpType.values();

        assertEquals(4, types.length);
        assertEquals(HelpType.QUICK_QUESTION, types[0]);
        assertEquals(HelpType.CONCEPT, types[1]);
        assertEquals(HelpType.DEBUGGING, types[2]);
        assertEquals(HelpType.SETUP, types[3]);
    }

    @Test
    public void testGetLabel()
    {
        assertEquals("Quick Question",
            HelpType.QUICK_QUESTION.getLabel());

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
        assertEquals(5, HelpType.QUICK_QUESTION.getMinutes());
        assertEquals(10, HelpType.CONCEPT.getMinutes());
        assertEquals(15, HelpType.DEBUGGING.getMinutes());
        assertEquals(5, HelpType.SETUP.getMinutes());
    }

    @Test
    public void testValuesArrayIndependence()
    {
        HelpType[] types = HelpType.values();

        types[0] = HelpType.SETUP;

        assertEquals(HelpType.QUICK_QUESTION,
            HelpType.values()[0]);
    }
}
