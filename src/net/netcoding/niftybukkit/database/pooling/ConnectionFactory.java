package net.netcoding.niftybukkit.database.pooling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.netcoding.niftybukkit.util.StringUtil;

public class ConnectionFactory {

	private final String url;
	private final Properties properties;

	/**
	 * @param url  Database URL.
	 * @param user Username for the database connection.
	 * @param pass Password for the database connection.
	 */
	public ConnectionFactory(String url, String user, String pass) {
		Properties properties = new Properties();
		properties.put("user", user);
		properties.put("password", pass);
		this.properties = properties;
		this.url = url;
	}

	/**
	 * @param url        Database URL
	 * @param properties Properties of the connection to establish.
	 */
	public ConnectionFactory(String url, Properties properties) {
		this.url = url;
		this.properties = properties;
	}

	/**
	 * Returns a connection to the database.
	 *
	 * @return Connection to the database.
	 * @throws SQLException When connection is not available.
	 */
	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.getUrl(), this.getProperties());
	}

	protected Properties getProperties() {
		return this.properties;
	}

	public String getUrl() {
		return StringUtil.format("{0}?autoReconnect=true", this.url);
	}

	public boolean isValidConnection() {
		try (Connection connection = this.getConnection()) {
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

}