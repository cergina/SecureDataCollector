package View.Support.CustomExceptions;

public class BadVerificationCodeException extends Exception {
    public BadVerificationCodeException(String msg){
        super(msg);
    }
}
