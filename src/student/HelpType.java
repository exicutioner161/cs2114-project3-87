package student;

/** The supported categories of office-hours help. */
public enum HelpType {
    QUICK_QUESTION("Quick Question", 5),
    CONCEPT("Concept", 10),
    DEBUGGING("Debugging", 15),
    SETUP("Setup", 10);

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
