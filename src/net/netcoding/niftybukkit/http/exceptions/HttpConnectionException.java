package net.netcoding.niftybukkit.http.exceptions;

import net.netcoding.niftybukkit.http.HttpStatus;
import net.netcoding.niftybukkit.util.StringUtil;

public class HttpConnectionException extends Exception {

	private final HttpStatus status;

	public HttpConnectionException(HttpStatus status) {
		super(status.getMessage());
		this.status = status;
	}

	public HttpConnectionException(Throwable throwable) {
		this(HttpStatus.UNKNOWN_ERROR, throwable);
	}

	public HttpConnectionException(HttpStatus status, Throwable throwable) {
		super(StringUtil.format("{0}: {1}: {2}", status.getMessage(), throwable.getClass().getName(), throwable.getMessage()), throwable);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

}