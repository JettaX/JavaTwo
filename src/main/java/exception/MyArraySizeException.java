package exception;

public class MyArraySizeException extends RuntimeException {

    public MyArraySizeException() {
        super("Size of array must be 4x4");
    }


}
