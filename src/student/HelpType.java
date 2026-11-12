package student;

public enum HelpType {
    QUICK_QUESTION("Quick Question", 5),
    CONCEPT("Concept", 10),
    DEBUGGING("Debugging", 15),
    SETUP("Setup", 5);


    SETUP("Setup", 5);

    private final String label;
    private final int minutes;
    /**
     * 
     * @param Label tells what kind of question u have
     * @param minutes how long it would take
     */
    HelpType(String label, int minutes)
    {
        this.label = label;
        this.minutes = minutes;
    }

    public String getLabel()
    {
        return label;
    }

    public int getMinutes()
    {
        return minutes;
    }
}
}
