package projectutilstest;

import projectutils.EmptyQueueException;
import projectutils.LinkedQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// -------------------------------------------------------------------------
/**
 *  Test class for LinkedQueue
 * 
 *  @author Rahul Rajendran
 *  @version Nov 5, 2026
 */
public class LinkedQueueTest {
    private LinkedQueue<String> queue;
    
    /**
     * creates empty queue before each test
     */
    @BeforeEach
    public void setUp() {
        queue = new LinkedQueue<>();
    }
    /**
     * tests isEmpty when full
     */
    @Test
    public void testIsEmptyFull() {
        queue.enqueue("A");
        assertFalse(queue.isEmpty());
    }
    /**
     * tests isEmpty
     */
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
    }
    /**
     * tests toArray when queue is empty
     */
    @Test
    public void testToArrayEmpty() {
        Object[] test = queue.toArray();
        assertEquals(0, test.length);
    }
    /**
     * tests toArray on a queue with entries
     */
    @Test
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
    @Test
    public void testToStringEmpty() {
        assertEquals("[]", queue.toString());
    }
    /**
     * tests toString on a queue with one entry
     */
    @Test
    public void testToStringOne() {
        queue.enqueue("A");
        assertEquals("[A]", queue.toString());
    }
    /**
     * tests toString on multiple entries
     */
    @Test
    public void testToString() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        assertEquals("[A, B, C]", queue.toString());
    }
    /**
     * tests clear
     */
    @Test
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
    @Test
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
    @Test
    public void testGetFrontOne() {
        queue.enqueue("A");
        assertEquals("A", queue.getFront());
    }
    /**
     * tests getFront on queue with multiple entries
     */
    @Test
    public void testGetFront() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        assertEquals("A", queue.getFront());
    }
    
    /**
     * tests enqueue on empty queue
     */
    @Test
    public void testEnqueueEmpty() {
        queue.enqueue("A");
        assertEquals(1, queue.size());
        assertEquals("A", queue.getFront());
    }
    /**
     * tests enqueue keeps right order when multiple entries are added
     */
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    /**
     * tests enqueue on bad input
     */
    @Test
    public void testEnqueueNull() {
        Exception exception = null;
        try {
            queue.enqueue(null);
        }
        catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
}
