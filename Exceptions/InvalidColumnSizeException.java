package Exceptions;

// Done.
public class InvalidColumnSizeException extends Exception {
	
	public InvalidColumnSizeException(int expectedSize) {
		super(String.format("Column size must be %d.", expectedSize));
	}

	public InvalidColumnSizeException(String expectedSize) {
		super(String.format("Column size must be %s.", expectedSize));
	}
}