package student;

// -------------------------------------------------------------------------
/**
 * Classifies the Helptype, alongside the default time for a time of request
 * 
 * @author Zayd Pathan
 * @version Nov 11, 2026
 */
public enum HelpType {
    /**
     * For when you are having trouble with your code
     */
    DEBUGGING("Debug", 10),
    /**
     * Specific exam topic you're having problems with
     */
    CONCEPT("Concept", 5),
    /**
     * Eclipse setup, git issues, or build path
     */
    SETUP("Setup", 8),
    /**
     * Other requests
     */
    OTHER("Other", 7);

    private final String label;
    private final int minutes;

    HelpType(String label, int minutes) {
        this.label = label;
        this.minutes = minutes;
    }

    public String getLabel() {
        return label;
    }

    /** @return the default time estimate in minutes */
    public int getMinutes() {
        return minutes;
    }
}
