package projectutilstest;
import student.TestCase;
import projectutils.EmptyQueueException;
import projectutils.LinkedQueue;

// -------------------------------------------------------------------------
/**
 *  Test class for LinkedQueue
 * 
 *  @author Rahul Rajendran
 *  @version Nov 5, 2026
 */
public class LinkedQueueTest extends TestCase {
    private LinkedQueue<String> queue;
    
    /**
     * creates empty queue before each test
     */
    public void setUp() {
        queue = new LinkedQueue<String>();
    }
    /**
     * tests isEmpty when full
     */
    public void testIsEmptyFull() {
        queue.enqueue("A");
        assertFalse(queue.isEmpty());
    }
    /**
     * tests isEmpty
     */
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
    }
    /**
     * tests toArray when queue is empty
     */
    public void testToArrayEmpty() {
        Object[] test = queue.toArray();
        assertEquals(0, test.length);
    }
    /**
     * tests toArray on a queue with entries
     */
    public void testToArray() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        Object[] test = queue.toArray();
        assertEquals(3, test.length);
        assertEquals("A", test[0]);
        assertEquals("B", test[1]);
        assertEquals("C", test[2]);
    }
    /**
     * tests toString on empty queue
     */
    public void testToStringEmpty() {
        assertEquals("[]", queue.toString());
    }
    /**
     * tests toString on a queue with one entry
     */
    public void testToStringOne() {
        queue.enqueue("A");
        assertEquals("[A]", queue.toString());
    }
    /**
     * tests toString on multiple entries
     */
    public void testToString() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        assertEquals("[A, B, C]", queue.toString());
    }
    /**
     * tests clear
     */
    public void testClear() {
        queue.enqueue("A");
        queue.enqueue("A");
        queue.enqueue("A");
        queue.clear();
        assertTrue(queue.isEmpty());
    }
    /**
     * tests getFront on empty queue
     */
    public void testGetFrontEmpty() {
        Exception exception = null;
        try {
            queue.getFront();
        }
        catch (EmptyQueueException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    /**
     * tests getFront on queue with one entry
     */
    public void testGetFrontOne() {
        queue.enqueue("A");
        assertEquals("A", queue.getFront());
    }
    /**
     * tests getFront on queue with multiple entries
     */
    public void testGetFront() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        assertEquals("A", queue.getFront());
    }
    
    /**
     * tests enqueue on empty queue
     */
    public void testEnqueueEmpty() {
        queue.enqueue("A");
        assertEquals(1, queue.size());
        assertEquals("A", queue.getFront());
    }
    /**
     * tests enqueue keeps right order when multiple entries are added
     */
    public void testEnqueueMultiple() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        assertEquals(3, queue.size());
        assertEquals("A", queue.getFront());
    }
    /**
     * tests dequeue on empty queue
     */
    public void testDequeueEmpty() {
        Exception exception = null;
        try {
            queue.dequeue();
        }
        catch (EmptyQueueException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    /**
     * tests dequeue on queue with one entry
     */
    public void testDequeueOne() {
        queue.enqueue("A");
        String dum = queue.dequeue();
        assertEquals("A", dum);
        assertTrue(queue.isEmpty());
    }
    /**
     * tests dequeue on queue with multiple entries and that they're held
     * in correct order
     */
    public void testDequeueFull() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        String dum = queue.dequeue();
        assertEquals("A", dum);
        assertEquals("B", queue.getFront());
    }
    /**
     * tests multiple dequeues on queue with multiple entries
     */
    public void testDequeueMultiple() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        String dum = queue.dequeue();
        String dumb = queue.dequeue();
        assertEquals(1, queue.size());
        assertEquals("A", dum);
        assertEquals("B", dumb);
        assertEquals("C", queue.getFront());
    }
}
