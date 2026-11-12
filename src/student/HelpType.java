package student;

// -------------------------------------------------------------------------
/**
 *  Classifies the Helptype, alongside the default time for a time of request
 * 
 *  @author Zayd Pathan
 *  @version Nov 11, 2026
 */
public enum HelpType {
    /**
     * For when you are having trouble with your code
     */
    DEBUGGING(10),
    /**
     * Specific exam topic you're having problems with
     */
    CONCEPT(5),
    /**
     * Eclipse setup, git issues, or build path
     */
    SETUP(8),
    /**
     * Other requests
     */
    OTHER(7);
    
    
    private final int defaultMinutes;
    /**
     * creates a HelpType with default minutes assigned
     */
    HelpType(int defaultMinutes) {
        this.defaultMinutes = defaultMinutes;
    }
    /**
     * returns the default minutes for that HelpType
     */
    public int getDefaultMinutes() {
        return defaultMinutes;
    }

    private final String label;
    private final int minutes;

    HelpType(String label, int minutes) {
        this.label = label;
        this.minutes = minutes;
    }

    /** @return the user-facing category label */
    public String getLabel() {
        return label;
    }

    /** @return the default time estimate in minutes */
    public int getMinutes() {
        return minutes;
    }
}
