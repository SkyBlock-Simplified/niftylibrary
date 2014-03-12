package net.netcoding.niftybukkit.yaml.exceptions;

@SuppressWarnings("serial")
public class InvalidConfigurationException extends Exception {

	public InvalidConfigurationException() { }

	public InvalidConfigurationException(String message) {
		super(message);
	}

	public InvalidConfigurationException(Throwable cause) {
		super(cause);
	}

	public InvalidConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}