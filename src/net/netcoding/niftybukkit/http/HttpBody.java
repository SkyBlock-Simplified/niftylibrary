package net.netcoding.niftybukkit.http;

public class HttpBody {

	private String bodyString;

	public HttpBody(String bodyString) {
		this.bodyString = bodyString;
	}

	public byte[] getBytes() {
		return bodyString != null ? bodyString.getBytes() : new byte[0];
	}

	@Override
	public String toString() {
		return this.bodyString;
	}

}