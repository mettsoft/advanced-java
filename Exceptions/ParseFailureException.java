package Exceptions;

// DONE!
public class ParseFailureException extends Exception {
	public ParseFailureException(String pattern) {
		super(String.format("Parsing failed! Conform to this pattern: %s.", pattern));
	}
}