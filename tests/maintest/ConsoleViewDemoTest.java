package maintest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import main.ConsoleViewDemo;
import student.TestCase;

/** Checks the view demo. Drafted with Codex at Gao's request. */
public class ConsoleViewDemoTest extends TestCase {
    private InputStream oldInput;
    private PrintStream oldOutput;
    private ByteArrayOutputStream output;

    public void setUp() {
        oldInput = System.in;
        oldOutput = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    public void tearDown() {
        System.setIn(oldInput);
        System.setOut(oldOutput);
    }

    public void testNameThenQuit() {
        System.setIn(new ByteArrayInputStream("Gao\nquit\nIgnored\n".getBytes()));
        ConsoleViewDemo.main(new String[0]);
        assertTrue(output.toString().contains("You typed: Gao"));
        assertTrue(output.toString().contains("Demo ended."));
        assertFalse(output.toString().contains("You typed: quit"));
        assertFalse(output.toString().contains("Ignored"));
    }

    public void testEndOfInput() {
        System.setIn(new ByteArrayInputStream(new byte[0]));
        ConsoleViewDemo.main(new String[0]);
        assertTrue(output.toString().contains("Demo ended."));
        assertFalse(output.toString().contains("You typed:"));
    }

    public void testNameThenEndOfInput() {
        System.setIn(new ByteArrayInputStream("Gao".getBytes()));
        ConsoleViewDemo.main(new String[0]);
        assertTrue(output.toString().contains("You typed: Gao"));
        assertTrue(output.toString().contains("Demo ended."));
    }
}
