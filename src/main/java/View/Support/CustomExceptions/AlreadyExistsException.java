package View.Support.CustomExceptions;

/**
 * Custom exception for status code 409
 */
public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String msg){
        super(msg);
    }
}
