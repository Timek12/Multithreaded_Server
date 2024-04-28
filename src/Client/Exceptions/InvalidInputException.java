package Client.Exceptions;

public class InvalidInputException extends Exception {
    public InvalidInputException(String s) {
        super(s);
    }
    public InvalidInputException() {
    }

    public String InvalidInputException(String message) {
        return message;
    }
    public String InvalidInputException() {
        return "Invalid input";
    }
}
