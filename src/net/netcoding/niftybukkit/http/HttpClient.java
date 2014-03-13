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

public class HttpClient {

	public String post(URL url, HttpHeader... headers) throws IOException {
		return post(url, null, null, headers);
	}

	public String post(URL url, HttpBody body, HttpHeader... headers) throws IOException {
		return post(url, null, body, headers);
	}

	public String post(URL url, Proxy proxy, HttpBody body, HttpHeader... headers) throws IOException {
		return post(url, proxy, body, Arrays.asList(headers));
	}

	public String post(URL url, List<HttpHeader> headers) throws IOException {
		return post(url, null, null, headers);
	}

	public String post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException {
		return post(url, null, body, headers);
	}

	public String post(URL url, Proxy proxy, HttpBody body, List<HttpHeader> headers) throws IOException {
		if (proxy == null) proxy = Proxy.NO_PROXY;
		String line;
		StringBuffer response = new StringBuffer();
		HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
		connection.setRequestMethod("POST");

		for (HttpHeader header : headers)
			connection.setRequestProperty(header.getName(), header.getValue());

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		if (body != null) {
			try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
				writer.write(body.getBytes());
			}
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			while ((line = reader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}

			reader.close();
		}

		return response.toString();
	}

}