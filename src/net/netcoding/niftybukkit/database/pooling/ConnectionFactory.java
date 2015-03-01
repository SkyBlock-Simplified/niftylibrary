package net.netcoding.niftybukkit.database.pooling;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.UUID;

import net.netcoding.niftybukkit.util.StringUtil;

public abstract class ConnectionFactory {

	private final String url;
	private final Properties properties;
	private String schema;

	/**
	 * @param url  Database URL.
	 * @param user Username for the database connection.
	 * @param pass Password for the database connection.
	 */
	public ConnectionFactory(String url, String user, String pass) throws SQLException {
		Properties properties = new Properties();
		properties.put("user", user);
		properties.put("password", pass);
		this.properties = properties;
		this.url = url;

		if (!this.isValidConnection())
			throw new SQLException("Unable to connect to database!");

		this._getSchema();
	}

	/**
	 * @param url        Database URL
	 * @param properties Properties of the connection to establish.
	 */
	public ConnectionFactory(String url, Properties properties) throws SQLException {
		this.url = url;
		this.properties = properties;

		if (!this.isValidConnection())
			throw new SQLException("Invalid connection information!");

		this._getSchema();
	}

	protected static void assignArgs(PreparedStatement statement, Object... args) throws SQLException {
		for (int i = 0; i < args.length; i++) {
			int index = i + 1;

			if (args[i] instanceof String)
				statement.setString(index, (String)args[i]);
			else if (args[i] instanceof UUID)
				statement.setString(index, ((UUID)args[i]).toString());
			else if (args[i] instanceof Short)
				statement.setShort(index, (short)args[i]);
			else if (args[i] instanceof Integer)
				statement.setInt(index, (int)args[i]);
			else if (args[i] instanceof Long)
				statement.setLong(index, (long)args[i]);
			else if (args[i] instanceof Float)
				statement.setFloat(index, (float)args[i]);
			else if (args[i] instanceof Double)
				statement.setDouble(index, (double)args[i]);
			else if (args[i] instanceof Blob)
				statement.setBlob(index, (Blob)args[i]);
			else if (args[i] == null)
				statement.setNull(index, Types.NULL);
			else
				statement.setObject(index, args[i]);
		}
	}

	/**
	 * Returns a connection to the database.
	 * 
	 * @return Connection to the database.
	 * @throws SQLException When connection is not available.
	 */
	protected Connection getConnection() throws SQLException {
		return this._getConnection();
	}

	private Connection _getConnection() throws SQLException {
		return DriverManager.getConnection(this.getUrl(), this.getProperties());
	}

	protected Properties getProperties() {
		return this.properties;
	}

	public String getSchema() {
		return this.schema;
	}

	private void _getSchema() throws SQLException {
		try (Connection connection = this._getConnection()) {
			try (ResultSet result = connection.getMetaData().getCatalogs()) {
				while (result.next()) {
					String dbName = result.getString(1);
					if ("mysql".equalsIgnoreCase(dbName)) continue;
					if ("information_schema".equalsIgnoreCase(dbName)) continue;

					if (this.url.endsWith(dbName)) {
						this.schema = dbName;
						break;
					}
				}
			}

			if (StringUtil.isEmpty(this.schema))
				throw new SQLException("Unable to determine schema!");
		}
	}

	public String getUrl() {
		return StringUtil.format("{0}?autoReconnect=true", this.url);
	}

	/**
	 *  Checks if the connection details are valid.
	 *  
	 * @return True if valid connection details, otherwise false.
	 */
	public boolean isValidConnection() {
		try (Connection connection = this._getConnection()) {
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

	/**
	 * Changes the schema currently in use.
	 * 
	 * @param Database name
	 * @return True if correctly set.
	 * @throws SQLException
	 */
	public boolean setSchema(String schema) throws SQLException {
		try (Connection connection = this._getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("USE ?;")) {
				assignArgs(statement, schema);
				return statement.executeUpdate() > 0;
			}
		}
	}

}