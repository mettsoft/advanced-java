package Exceptions;

// DONE
public class SearchKeywordNotFoundException extends Exception {
	public SearchKeywordNotFoundException(String keyword) {
		super(String.format("'%s' was not found!", keyword));
	}
}
