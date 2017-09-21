package Exceptions;

// done.
public class InvalidRowSizeException extends Exception {

	public InvalidRowSizeException(int expectedSize) {
		super(String.format("Row size must be %d.", expectedSize));
	}

	public InvalidRowSizeException(String expectedSize) {
		super(String.format("Row size must be %s.", expectedSize));
	}
}