package exception;

public class MyArrayDataException extends NumberFormatException {

    public MyArrayDataException(String sign, String position) {
        super("sign '" + sign + "' on position [" + position + "] is incorrect");
    }
}
