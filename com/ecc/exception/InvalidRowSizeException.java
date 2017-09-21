package com.ecc.exception;

public class InvalidRowSizeException extends Exception {
	public InvalidRowSizeException(Integer expectedSize) {
		super(String.format("Row size must be %d.", expectedSize));
	}

	public InvalidRowSizeException(String expectedSize) {
		super(String.format("Row size must be %s.", expectedSize));
	}
}