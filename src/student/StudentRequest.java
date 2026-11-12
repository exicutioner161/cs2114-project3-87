package student;

/**
 * A student's request for help during an office-hours session.
 */
public class StudentRequest {
    private final int ticket;
    private final String studentName;
    private final HelpType questionType;
    private StudentStatus status;

    /**
     * Creates a waiting request.
     * @param ticket ticket number assigned to the request
     * @param name student's name
     * @param type kind of help requested
     */
    public StudentRequest(int ticket, String name, HelpType type) {
        if (name == null || name.trim().isEmpty() || type == null) {
            throw new IllegalArgumentException("Name and help type are required.");
        }
        this.ticket = ticket;
        this.studentName = name.trim();
        this.questionType = type;
        this.status = StudentStatus.WAITING;
    }

    /** @return this request's ticket number */
    public int getTicket() {
        return ticket;
    }

    /** @return the trimmed student name */
    public String getName() {
        return studentName;
    }

    /** @return the requested help category */
    public HelpType getHelpType() {
        return questionType;
    }

    /** @return the request's current status */
    public StudentStatus getStatus() {
        return status;
    }

    /**
     * Changes the request status.
     * @param newStatus replacement status
     */
    public void setStatus(StudentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status is required.");
        }
        status = newStatus;
    }
}
