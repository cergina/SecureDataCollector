package View.Support.CustomExceptions;

/**
 * Custom exception for status code 401
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String msg){
        super(msg);
    }
}
