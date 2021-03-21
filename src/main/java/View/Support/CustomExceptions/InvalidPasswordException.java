package View.Support.CustomExceptions;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String msg){
        super(msg);
    }
}
