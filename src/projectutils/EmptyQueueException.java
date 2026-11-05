package projectutils;
public class EmptyQueueException extends RuntimeException
{
    //~ Fields ................................................................

    //~ Constructors ..........................................................
    public EmptyQueueException() {
        super();
    }

    public EmptyQueueException(String message) {
        super(message);
    }
    //~Public  Methods ........................................................

}
