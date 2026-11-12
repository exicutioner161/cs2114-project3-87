package student;

// -------------------------------------------------------------------------
/**
 *  The state of the student while they are waiting in office hours
 * 
 *  @author Zayd Pathan
 *  @version Nov 11, 2026
 */
public enum StudentStatus {
    /**
     * In the line being helped
     */
    WAITING,
    /**
     * Currently being helped
     */
    BEING_HELPED,
    /**
     * Done being helped
     */
    SERVED,
    /**
     * Cancelled
     */
    CANCELLED
}
