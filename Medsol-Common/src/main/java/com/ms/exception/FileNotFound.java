package com.ms.exception;



public class FileNotFound extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileNotFound(String message) {
		super(message);
	}

	public FileNotFound(String message, Throwable cause) {
		super(message, cause);
	}
}
