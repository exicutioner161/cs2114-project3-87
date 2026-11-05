package student;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author zzhon
 *  @version Nov 5, 2026
 */
public class StudentRequest {
    private int ticket;
    private String studentName;
    private HelpType questionType;
    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param ticket
     * @param name
     * @param type
     */
    public void studentRequest(int ticket, String name, HelpType type) {
        this.ticket = ticket;
        this.studentName = name;
        this.questionType = type;
            
        
        
    }
    public int getTicket() {
        return ticket;
    }
    public String getName()
    {
        return studentName;
    }

}
