package projectutils;

// -------------------------------------------------------------------------
/**
 *  LinkedQueue class based on class implementation. Classic FIFO structure
 *  @param <T>
 * 
 *  @author Rahul Rajendran
 *  @version Nov 5, 2026
 */
public class LinkedQueue<T> {
    private Node<T> firstNode;
    private Node<T> lastNode;
    private int size;
    
    public LinkedQueue() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }
    /**
     * @return current size of the queue
     */
    public int size() {
        return size;
    }
    /**
     * @return true if the queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    // ----------------------------------------------------------
    /**
     * Adds entry to the back
     * @param data to be added to the queue
     * @throws IllegalArgumentException if data is null
     */
    public void enqueue(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }
        Node<T> newNode = new Node<T>(data);
        if (isEmpty()) {
            firstNode = newNode;
        }
        else {
            lastNode.next = newNode;
        }
        lastNode = newNode;
        size++;
    }
    
    // ----------------------------------------------------------
    /**
     * removes and returns first item in the queue
     * @return the first item in the queue
     * @throws EmptyQueueException if empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty");
        }
        T data = firstNode.data;
        firstNode = firstNode.next;
        size--;
        if (firstNode == null) {
            lastNode = null;
        }
        return data;
    }
    
    
    // ----------------------------------------------------------
    /**
     * returns first item in the queue, throws exception if empty
     * @return the first item in the queue
     * @throws EmptyQueueException if empty
     */
    public T getFront() {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty");
        }
        return firstNode.data;
    }

    /**
     * Returns the first item without removing it.
     * @return the first item in the queue
     * @throws EmptyQueueException if the queue is empty
     */
    public T peek() {
        return getFront();
    }
    
    // ----------------------------------------------------------
    /**
     * Converts queue into an array, front to back
     * @return array of the queues items in order, empty array if empty
     */
    public Object[] toArray() {
        Object[] newArray = new Object[size];
        Node<T> current = firstNode;
        int index = 0;
        while (current != null) {
            newArray[index] = current.data;
            current = current.next;
            index++;
        }
        return newArray;
    }
    
    // ----------------------------------------------------------
    /**
     * Clears the queue and removes all elements
     */
    public void clear() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }
    /**
     * Returns the queue in string format front to back ex: "[A, B, C]"
     * @return string version of the queue
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        Node<T> current = firstNode;
        while (current != null) {
            builder.append(current.data);
            if (current.next != null) {
                builder.append(", ");
            }
            current = current.next;
        }
        builder.append("]");
        return builder.toString();
        
    }
    
    private static class Node<T> {
        private T data;
        private Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }

    }
}
