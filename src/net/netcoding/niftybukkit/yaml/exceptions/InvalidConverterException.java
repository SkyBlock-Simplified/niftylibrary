package net.netcoding.niftybukkit.yaml.exceptions;

@SuppressWarnings("serial")
public class InvalidConverterException extends Exception {

	public InvalidConverterException() { }

	public InvalidConverterException(String message) {
		super(message);
	}

	public InvalidConverterException(Throwable cause) {
		super(cause);
	}

	public InvalidConverterException(String message, Throwable cause) {
		super(message, cause);
	}

}