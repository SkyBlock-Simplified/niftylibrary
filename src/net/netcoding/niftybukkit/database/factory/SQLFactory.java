package net.netcoding.niftybukkit.database.factory;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.factory.callbacks.ResultCallback;
import net.netcoding.niftybukkit.database.factory.callbacks.VoidResultCallback;
import net.netcoding.niftybukkit.util.StringUtil;

/**
 * Factory sql classes to be inherited from when creating a wrapper.
 */
public abstract class SQLFactory {

	private static final List<String> INVALID_SCHEMAS = Arrays.asList("test", "information_schema");
	private final String driver;
	private final boolean driverAvailable;
	private final String url;
	private final Properties properties;
	private String product;
	private String schema;

	/**
	 * Create a new factory instance.
	 * 
	 * @param url  Database connection url.
	 * @param user Username of the database connection.
	 * @param pass Password of the database connection.
	 * @throws SQLException
	 */
	public SQLFactory(String driver, String url, final String user, final String pass) throws SQLException {
		this(driver, url, new Properties() {{ setProperty("user", user); setProperty("password", pass); }});
	}

	/**
	 * Create a new factory instance.
	 * 
	 * @param url        Database connection url.
	 * @param properties Properties of the database connection.
	 * @throws SQLException
	 */
	public SQLFactory(String driver, String url, Properties properties) throws SQLException {
		try {
			Class.forName(driver);
			this.driverAvailable = true;
			this.driver = driver;
		} catch (ClassNotFoundException ex) {
			throw new SQLException(StringUtil.format("The specific driver {0} is not available!", driver));
		}

		this.url = url;
		this.properties = properties;
		this.load();
	}

	private static void assignArgs(PreparedStatement statement, Object... args) throws SQLException {
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
	 * Create a table if it does not exist.
	 * 
	 * @param name  Name of the table.
	 * @param sql Fields and constrains of the table
	 * @return True if the table was created, otherwise false.
	 * @throws SQLException
	 */
	public boolean createTable(String name, String sql) throws SQLException {
		try (Connection connection = this.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				return statement.executeUpdate(StringUtil.format("CREATE TABLE IF NOT EXISTS `{0}`.`{1}` ({2}){3};", this.getSchema(), name, sql, (this.getProduct().equals("MySQL") ? " ENGINE=InnoDB" : ""))) > 0;
			}
		}
	}

	/**
	 * Create a table if it does not exist asynchronously.
	 * 
	 * @param name  Name of the table.
	 * @param sql Table fields and constraints.
	 */
	public void createTableAsync(final String name, final String sql) {
		NiftyBukkit.getPlugin().getServer().getScheduler().runTaskAsynchronously(NiftyBukkit.getPlugin(), new Runnable() {
			@Override
			public void run() {
				try (Connection connection = getConnection()) {
					try (Statement statement = connection.createStatement()) {
						statement.executeUpdate(StringUtil.format("CREATE TABLE IF NOT EXISTS `{0}`.`{1}` ({2}){3};", getSchema(), name, sql, (getProduct().equals("MySQL") ? " ENGINE=InnoDB" : "")));
					}
				} catch (SQLException sqlex) { }
			}
		});
	}

	/**
	 * Gets a connection to the database.
	 * 
	 * @return Connection to the database.
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		if (!this.isDriverAvailable()) throw new SQLException("The driver for this sql instance is unavailable!");
		return DriverManager.getConnection(this.getUrl(), this.getProperties());
	}

	/**
	 * Gets the registered driver class for this DBMS.
	 * 
	 * @return Registered driver class.
	 */
	public final String getDriver() {
		return this.driver;
	}

	public final String getProduct() {
		return this.product;
	}

	/**
	 * Gets the properties used to create a connection for this DBMS.
	 * 
	 * @return Connection property details.
	 */
	protected Properties getProperties() {
		return this.properties;
	}

	/**
	 * Gets the schema for this DBMS.
	 * 
	 * @return Database name currently being used by connections.
	 */
	public final String getSchema() {
		return this.schema;
	}

	/**
	 * Gets the url for this DBMS.
	 * 
	 * @return Url for this DBMS.
	 */
	public String getUrl() {
		return StringUtil.format("{0}?characterEncoding=UTF-8&autoReconnectForPools=true", this.url);
	}

	/**
	 * Gets if the given jdbc driver is available.
	 * 
	 * @return True if driver available, otherwise false.
	 */
	public boolean isDriverAvailable() {
		return this.driverAvailable;
	}

	private void load() throws SQLException {
		try (Connection connection = this.getConnection()) {
			this.product = connection.getMetaData().getDatabaseProductName();

			try (ResultSet result = connection.getMetaData().getCatalogs()) {
				while (result.next()) {
					String schema = result.getString(1);
					if (INVALID_SCHEMAS.contains(schema)) continue;

					if (this.url.endsWith(schema)) {
						this.schema = schema;
						break;
					}
				}
			}
		}

		if (StringUtil.isEmpty(this.product))
			throw new SQLException("Unable to determine product name!");

		if (StringUtil.isEmpty(this.schema))
			throw new SQLException("Unable to determine schema!");
	}

	/**
	 * Run SELECT query against the DBMS.
	 * 
	 * @param sql      Query to run.
	 * @param callback Callback to process results with.
	 * @param args     Arguments to pass to the query.
	 * @return Whatever you decide to return in the callback.
	 * @throws SQLException
	 */
	public <T> T query(String sql, ResultCallback<T> callback, Object... args) throws SQLException {
		try (Connection connection = this.getConnection()) {
			return this.query(connection, sql, callback, args);
		}
	}

	/**
	 * Run SELECT query against the DBMS.
	 * 
	 * @param sql      Query to run.
	 * @param callback Callback t process results with.
	 * @param args     Arguments to pass to the query.
	 * @return Whatever you decide to return in the callback.
	 * @throws SQLException
	 */
	public void query(String sql, VoidResultCallback callback, Object... args) throws SQLException {
		try (Connection connection = this.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				assignArgs(statement, args);
				statement.executeQuery();

				if (callback != null) {
					try (ResultSet result = statement.getResultSet()) {
						callback.handle(result);
					}
				}
			}
		}
	}

	/**
	 * Run SELECT query against the DBMS asynchronously.
	 * 
	 * @param sql      Query to run.
	 * @param callback Callback to process results with.
	 * @param args     Arguments to pass to the query.
	 */
	public void queryAsync(final String sql, final VoidResultCallback callback, final Object... args) {
		NiftyBukkit.getPlugin().getServer().getScheduler().runTaskAsynchronously(NiftyBukkit.getPlugin(), new Runnable() {
			@Override
			public void run() {
				try {
					query(sql, callback, args);
				} catch (SQLException sqlex) { }
			}
		});
	}

	protected final <T> T query(Connection connection, String sql, ResultCallback<T> callback, Object... args) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			assignArgs(statement, args);
			statement.executeQuery();

			if (callback != null) {
				try (ResultSet result = statement.getResultSet()) {
					return callback.handle(result);
				}
			}
		}

		return null;
	}

	/**
	 * Changes the schema currently in use.
	 * 
	 * @param Database name
	 * @return True if correctly set.
	 * @throws SQLException
	 */
	public boolean setSchema(String schema) throws SQLException {
		try (Connection connection = this.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("USE ?;")) {
				assignArgs(statement, schema);
				return statement.executeUpdate() > 0;
			}
		}
	}

	/**
	 * Run INSERT, UPDATE or DELETE query against this DBMS.
	 * 
	 * @param sql  Query to run.
	 * @param args Arguments to pass to the query.
	 * @return True if query was successful, otherwise false.
	 * @throws SQLException
	 */
	public boolean update(String sql, Object... args) throws SQLException {
		try (Connection connection = this.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				assignArgs(statement, args);
				return statement.executeUpdate() > 0;
			}
		}
	}

	/**
	 * Run INSERT, UPDATE or DELETE query against this DBMS asynchronously.
	 * 
	 * @param sql  Query to run.
	 * @param args Arguments to pass to the query.
	 */
	public void updateAsync(final String sql, final Object... args) {
		NiftyBukkit.getPlugin().getServer().getScheduler().runTaskAsynchronously(NiftyBukkit.getPlugin(), new Runnable() {
			@Override
			public void run() {
				try (Connection connection = getConnection()) {
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						assignArgs(statement, args);
						statement.executeUpdate();
					}
				} catch (SQLException sqlex) { }
			}
		});
	}

}