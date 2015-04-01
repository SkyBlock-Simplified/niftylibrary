package net.netcoding.niftybukkit.http.exceptions;

import net.netcoding.niftybukkit.http.HttpCode;
import net.netcoding.niftybukkit.util.StringUtil;

public class HttpConnectionException extends Exception {

	private final HttpCode code;

	public HttpConnectionException(HttpCode code) {
		super(code.getMessage());
		this.code = code;
	}

	public HttpConnectionException(Throwable throwable) {
		super(StringUtil.format("{0}: {1}: {2}", HttpCode.UNKNOWN.getMessage(), throwable.getClass().getName(), throwable.getMessage()), throwable);
		this.code = HttpCode.UNKNOWN;
	}

	public HttpCode getHttpCode() {
		return this.code;
	}

}