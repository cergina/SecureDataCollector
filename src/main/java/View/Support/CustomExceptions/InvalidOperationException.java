package View.Support.CustomExceptions;

/**
 * Custom exception for status code 400
 */
public class InvalidOperationException extends Exception {
    public InvalidOperationException(String msg){
        super(msg);
    }
}
