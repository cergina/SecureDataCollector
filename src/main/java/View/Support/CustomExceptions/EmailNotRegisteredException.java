package View.Support.CustomExceptions;

public class EmailNotRegisteredException extends Exception{
    public EmailNotRegisteredException(String msg){
        super(msg);
    }
}
