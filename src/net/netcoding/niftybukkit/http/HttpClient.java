package net.netcoding.niftybukkit.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import net.netcoding.niftybukkit.util.StringUtil;

public class HttpClient {

	private final int DEFAULT_TIMEOUT = 3000;

	public String get(URL url, HttpHeader... headers) throws IOException {
		return get(url, DEFAULT_TIMEOUT, Arrays.asList(headers));
	}

	public String get(URL url, int timeout, HttpHeader... headers) throws IOException {
		return get(url, timeout, Arrays.asList(headers));
	}

	public String get(URL url, Proxy proxy, HttpHeader... headers) throws IOException {
		return get(url, DEFAULT_TIMEOUT, proxy, Arrays.asList(headers));
	}

	public String get(URL url, int timeout, Proxy proxy, HttpHeader... headers) throws IOException {
		return get(url, timeout, proxy, Arrays.asList(headers));
	}

	public String get(URL url, List<HttpHeader> headers) throws IOException {
		return get(url, null, headers);
	}

	public String get(URL url, int timeout, List<HttpHeader> headers) throws IOException {
		return get(url, timeout, null, headers);
	}

	public String get(URL url, Proxy proxy, List<HttpHeader> headers) throws IOException {
		return get(url, DEFAULT_TIMEOUT, proxy, headers);
	}

	public String get(URL url, int timeout, Proxy proxy, List<HttpHeader> headers) throws IOException {
		if (proxy == null) proxy = Proxy.NO_PROXY;
		String line;
		StringBuffer response = new StringBuffer();
		HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
		connection.setRequestMethod("GET");

		for (HttpHeader header : headers)
			connection.setRequestProperty(header.getName(), header.getValue());

		connection.setConnectTimeout(timeout);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);

		try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream())) {
			try (BufferedReader reader = new BufferedReader(streamReader)) {
				while (StringUtil.notEmpty(line = reader.readLine())) {
					response.append(line);
					response.append('\r');
				}
			}
		}

		return response.toString();
	}

	public String post(URL url, HttpHeader... headers) throws IOException {
		return post(url, DEFAULT_TIMEOUT, headers);
	}

	public String post(URL url, int timeout, HttpHeader... headers) throws IOException {
		return post(url, null, null, timeout, headers);
	}

	public String post(URL url, HttpBody body, HttpHeader... headers) throws IOException {
		return post(url, null, body, DEFAULT_TIMEOUT, headers);
	}

	public String post(URL url, Proxy proxy, HttpHeader... headers) throws IOException {
		return post(url, proxy, null, DEFAULT_TIMEOUT, headers);
	}

	public String post(URL url, HttpBody body, int timeout, HttpHeader... headers) throws IOException {
		return post(url, null, body, timeout, headers);
	}

	public String post(URL url, Proxy proxy, int timeout, HttpHeader... headers) throws IOException {
		return post(url, proxy, null, timeout, headers);
	}

	public String post(URL url, Proxy proxy, HttpBody body, int timeout, HttpHeader... headers) throws IOException {
		return post(url, proxy, body, timeout, Arrays.asList(headers));
	}

	public String post(URL url, List<HttpHeader> headers) throws IOException {
		return post(url, null, null, DEFAULT_TIMEOUT, headers);
	}

	public String post(URL url, int timeout, List<HttpHeader> headers) throws IOException {
		return post(url, null, null, timeout, headers);
	}

	public String post(URL url, Proxy proxy, List<HttpHeader> headers) throws IOException {
		return post(url, proxy, null, DEFAULT_TIMEOUT, headers);
	}

	public String post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException {
		return post(url, null, body, DEFAULT_TIMEOUT, headers);
	}

	public String post(URL url, Proxy proxy, int timeout, List<HttpHeader> headers) throws IOException {
		return post(url, proxy, null, timeout, headers);
	}

	public String post(URL url, HttpBody body, int timeout, List<HttpHeader> headers) throws IOException {
		return post(url, null, body, timeout, headers);
	}

	public String post(URL url, Proxy proxy, HttpBody body, int timeout, List<HttpHeader> headers) throws IOException {
		if (proxy == null) proxy = Proxy.NO_PROXY;
		String line;
		StringBuffer response = new StringBuffer();
		HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
		connection.setRequestMethod("POST");

		for (HttpHeader header : headers)
			connection.setRequestProperty(header.getName(), header.getValue());

		connection.setConnectTimeout(timeout);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);

		if (body != null) {
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.write(body.getBytes());
		}

		try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream())) {
			try (BufferedReader reader = new BufferedReader(streamReader)) {
				while (StringUtil.notEmpty(line = reader.readLine())) {
					response.append(line);
					response.append('\r');
				}
			}
		}

		return response.toString();
	}

}