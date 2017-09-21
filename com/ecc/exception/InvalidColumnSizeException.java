package com.ecc.exception;

public class InvalidColumnSizeException extends Exception {
	public InvalidColumnSizeException(Integer expectedSize) {
		super(String.format("Column size must be %d.", expectedSize));
	}
	        
	public InvalidColumnSizeException(String expectedSize) {
		super(String.format("Column size must be %s.", expectedSize));
	}
}