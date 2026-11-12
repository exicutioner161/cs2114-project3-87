package main;

import java.util.ArrayList;
import java.util.List;

import projectutils.LinkedQueue;
import student.HelpType;
import student.StudentRequest;
import student.StudentStatus;

/**
 * Owns the office-hours waiting line and enforces its state transitions.
 */
public class OfficeHoursSession {
    private final String pin;
    private final LinkedQueue<StudentRequest> line;
    private StudentRequest currentStudent;
    private int nextTicket;

    /**
     * Starts an empty session protected by a PIN.
     * @param sessionPin instructor PIN
     */
    public OfficeHoursSession(String sessionPin) {
        if (sessionPin == null || sessionPin.trim().isEmpty()) {
            throw new IllegalArgumentException("PIN is required.");
        }
        pin = sessionPin;
        line = new LinkedQueue<>();
        nextTicket = 1;
    }

    /** Creates a session with a default PIN for the application entry point. */
    public OfficeHoursSession() {
        this("1234");
    }

    /**
     * Adds a student to the end of the line.
     * @param name student's name
     * @param type requested help category
     * @return the newly assigned ticket
     */
    public int joinLine(String name, HelpType type) {
        StudentRequest request = new StudentRequest(nextTicket, name, type);
        line.enqueue(request);
        return nextTicket++;
    }

    /**
     * Gets a waiting student's one-based position.
     * @param ticket ticket to locate
     * @return position in the waiting line
     */
    public int getPosition(int ticket) {
        int position = 0;
        for (StudentRequest request : requests()) {
            if (request.getStatus() == StudentStatus.WAITING) {
                position++;
                if (request.getTicket() == ticket) {
                    return position;
                }
            }
        }
        throw new IllegalArgumentException("Ticket is not waiting.");
    }

    /**
     * Cancels a waiting request without removing its queue entry.
     * @param ticket ticket to cancel
     */
    public void cancel(int ticket) {
        StudentRequest request = find(ticket);
        if (request == null || request.getStatus() != StudentStatus.WAITING) {
            throw new IllegalArgumentException("Ticket is not waiting.");
        }
        request.setStatus(StudentStatus.CANCELLED);
    }

    /** @return whether the supplied PIN matches */
    public boolean checkPin(String attempt) {
        return attempt != null && pin.equals(attempt);
    }

    /** @return the next waiting request */
    public StudentRequest callNext() {
        if (currentStudent != null) {
            throw new IllegalStateException("Complete the current student first.");
        }
        while (!line.isEmpty() && line.getFront().getStatus() == StudentStatus.CANCELLED) {
            line.dequeue();
        }
        if (line.isEmpty()) {
            throw new IllegalStateException("The line is empty.");
        }
        currentStudent = line.dequeue();
        currentStudent.setStatus(StudentStatus.BEING_HELPED);
        return currentStudent;
    }

    /** Completes and removes the current request. */
    public StudentRequest completeCurrent() {
        if (currentStudent == null) {
            throw new IllegalStateException("No student is being helped.");
        }
        currentStudent.setStatus(StudentStatus.COMPLETED);
        StudentRequest completed = currentStudent;
        currentStudent = null;
        return completed;
    }

    /** @return the current request, or null when the instructor is idle */
    public StudentRequest getCurrentStudent() {
        return currentStudent;
    }

    /** @return waiting requests in arrival order */
    public StudentRequest[] getWaitingStudents() {
        List<StudentRequest> waiting = new ArrayList<>();
        for (StudentRequest request : requests()) {
            if (request.getStatus() == StudentStatus.WAITING) {
                waiting.add(request);
            }
        }
        return waiting.toArray(new StudentRequest[0]);
    }

    /** @return true when no request is waiting */
    public boolean isLineEmpty() {
        return getWaitingStudents().length == 0;
    }

    private StudentRequest[] requests() {
        Object[] entries = line.toArray();
        StudentRequest[] result = new StudentRequest[entries.length];
        for (int i = 0; i < entries.length; i++) {
            result[i] = (StudentRequest) entries[i];
        }
        return result;
    }

    private StudentRequest find(int ticket) {
        for (StudentRequest request : requests()) {
            if (request.getTicket() == ticket) {
                return request;
            }
        }
        return null;
    }
}
