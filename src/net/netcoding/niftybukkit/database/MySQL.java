package net.netcoding.niftybukkit.database;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.netcoding.niftybukkit.NiftyBukkit;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class MySQL implements Runnable {

	private final String username;
	private final String schema;
	private final String password;
	private final int port;
	private final String hostname;
	static final transient int DEFAULT_DELAY = 10;
	private final transient List<DatabaseNotification> listeners = Collections.synchronizedList(new ArrayList<DatabaseNotification>());
	private transient BukkitTask task;

	public MySQL(String host, String schema, String user, String pass) {
		this(host, 3306, schema, user, pass);
	}

	public MySQL(String host, int port, String schema, String user, String pass) {
		this.hostname = host;
		this.port = port;
		this.schema = schema;
		this.username = user;
		this.password = pass;
	}

	public List<DatabaseNotification> addDatabaseListener(String table, DatabaseListener notifier) throws SQLException, Exception {
		return this.addDatabaseListener(table, notifier, DEFAULT_DELAY, false);
	}

	public List<DatabaseNotification> addDatabaseListener(String table, DatabaseListener notifier, long delay) throws SQLException, Exception {
		return this.addDatabaseListener(table, notifier, delay, false);
	}

	public List<DatabaseNotification> addDatabaseListener(String table, DatabaseListener notifier, boolean overwrite) throws SQLException, Exception {
		return this.addDatabaseListener(table, notifier, DEFAULT_DELAY, overwrite);
	}

	public List<DatabaseNotification> addDatabaseListener(String table, DatabaseListener notifier, long delay, boolean overwrite) throws SQLException, Exception {
		return this.addDatabaseListener(table, Arrays.asList(TriggerEvent.DELETE, TriggerEvent.INSERT, TriggerEvent.UPDATE), notifier, delay, overwrite);
	}

	public DatabaseNotification addDatabaseListener(String table, TriggerEvent event, DatabaseListener notifier) throws SQLException, Exception {
		return this.addDatabaseListener(table, event, notifier, DEFAULT_DELAY, false);
	}

	public DatabaseNotification addDatabaseListener(String table, TriggerEvent event, DatabaseListener notifier, long delay) throws SQLException, Exception {
		return this.addDatabaseListener(table, event, notifier, delay, false);
	}

	public DatabaseNotification addDatabaseListener(String table, TriggerEvent event, DatabaseListener notifier, boolean overwrite) throws SQLException, Exception {
		return this.addDatabaseListener(table, event, notifier, DEFAULT_DELAY, overwrite);
	}

	public DatabaseNotification addDatabaseListener(String table, TriggerEvent event, DatabaseListener notifier, long delay, boolean overwrite) throws SQLException, Exception {
		return this.addDatabaseListener(table, Arrays.asList(event), notifier, delay, overwrite).get(0);
	}

	private List<DatabaseNotification> addDatabaseListener(String table, List<TriggerEvent> events, DatabaseListener notifier, long delay, boolean overwrite) throws SQLException, Exception {
		List<DatabaseNotification> newListeners = new ArrayList<DatabaseNotification>();

		for (TriggerEvent evt : events) {
			DatabaseNotification listener = new DatabaseNotification(this, table, evt, notifier, delay, overwrite);
			newListeners.add(listener);
			this.listeners.add(listener);
		}

		if (this.task == null) this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(NiftyBukkit.getPlugin(), this, 0, delay);
		return newListeners;
	}

	private static void assign(PreparedStatement statement, Object... args) throws SQLException {
		for (int i = 0; i < args.length; i++) {
			int index = i + 1;

			if (args[i] instanceof String)
				statement.setString(index, (String)args[i]);
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
				statement.setObject(index, Types.OTHER);
		}
	}

	public static void closeConnections(Connection connection, Statement statement, ResultSet resultset) {
		if (resultset != null) try { resultset.close(); } catch (SQLException ex) { }
		if (statement != null) try { statement.close(); } catch (SQLException ex) { }
		if (connection != null) try { connection.close(); } catch (SQLException ex) { }
	}

	public boolean createTable(String name, String query) throws SQLException, Exception {
		Connection con = null;
		Statement st   = null;

		try {
			con = getConnection();
			st = con.createStatement();
			return st.executeUpdate(String.format("CREATE TABLE IF NOT EXISTS `%1$s` (%2$s);", name, query)) > 0;
		} catch (Exception ex) {
			throw ex;
		} finally {
			closeConnections(con, st, null);
		}
	}

	public Connection getConnection() throws SQLException, ClassNotFoundException, Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(String.format("jdbc:mysql://%1$s:%2$s/%3$s?autoReconnect=true", this.hostname, this.port, this.schema), this.username, this.password);
		} catch (SQLException ex) {
			throw ex;
		} catch (ClassNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public String getHostname() {
		return this.hostname;
	}

	public String getPassword() {
		return this.password;
	}

	public int getPort() {
		return this.port;
	}

	public String getSchema() {
		return this.schema;
	}

	public String getUsername() {
		return this.username;
	}

	private Object internalQuery(String sql, boolean getKeys, ResultSetCallback callback, Object... args) throws SQLException, Exception {
		Connection connection       = null;
		PreparedStatement statement = null;
		ResultSet result            = null;

		try {
			connection = getConnection();
			statement  = connection.prepareStatement(sql);
			assign(statement, args);
			statement.executeQuery();

			if (getKeys)
				result = statement.getGeneratedKeys();
			else
				result = statement.getResultSet();

			if (callback != null) return callback.handleResult(result);
		} catch (Exception ex) {
			throw ex;
		} finally {
			closeConnections(connection, statement, result);
		}

		return null;
	}

	private void internalQueryNR(String sql, boolean getKeys, ResultSetCallbackNR callback, Object... args) throws SQLException, Exception {
		Connection connection       = null;
		PreparedStatement statement = null;
		ResultSet result            = null;

		try {
			connection = getConnection();
			statement  = connection.prepareStatement(sql);
			assign(statement, args);
			statement.executeQuery();

			if (getKeys)
				result = statement.getGeneratedKeys();
			else
				result = statement.getResultSet();

			if (callback != null) callback.handleResult(result);
		} catch (Exception ex) {
			throw ex;
		} finally {
			closeConnections(connection, statement, result);
		}
	}

	public Object query(String sql, ResultSetCallback callback, Object... args) throws SQLException, Exception {
		return this.internalQuery(sql, false, callback, args);
	}

	public Object query(String sql, boolean getKeys, ResultSetCallback callback, Object... args) throws SQLException, Exception {
		return this.internalQuery(sql, getKeys, callback, args);
	}

	public void query(String sql, Object... args) throws SQLException, Exception {
		this.internalQueryNR(sql, false, null, args);
	}

	public void query(String sql, boolean getKeys, Object... args) throws SQLException, Exception {
		this.internalQueryNR(sql, getKeys, null, args);
	}

	public void query(String sql, ResultSetCallbackNR callback, Object... args) throws SQLException, Exception {
		this.internalQueryNR(sql, false, callback, args);
	}

	public void query(String sql, boolean getKeys, ResultSetCallbackNR callback, Object... args) throws SQLException, Exception {
		this.internalQueryNR(sql, getKeys, callback, args);
	}

	public void removeListener(String table) {
		this.removeListener(table, null, false);
	}

	public void removeListener(String table, boolean dropTriggers) {
		this.removeListener(table, null, dropTriggers);
	}

	public void removeListener(String table, TriggerEvent event) {
		this.removeListener(table, event, false);
	}

	public void removeListener(String table, TriggerEvent event, boolean dropTriggers) {
		for (DatabaseNotification listener : this.listeners) {
			if (listener.getTable() == table && (event == null || listener.getEvent() == event))
				listener.stop(dropTriggers);
		}

		if (this.listeners.size() == 0) {
			this.task.cancel();
			this.task = null;
		}
	}

	public void stopListening() {
		this.stopListening(false);
	}

	public void stopListening(boolean dropTriggers) {
		this.task.cancel();
		this.task = null;

		for (DatabaseNotification listener : this.listeners)
			listener.stop(dropTriggers);
	}

	public boolean testConnection() {
		Connection connection = null;

		try {
			connection = getConnection();
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			closeConnections(connection, null, null);
		}
	}

	public boolean update(String sql, Object... args) throws SQLException, Exception {
		Connection connection       = null;
		PreparedStatement statement = null;

		try {
			connection = getConnection();
			statement  = connection.prepareStatement(sql);
			assign(statement, args);
			return statement.executeUpdate() > 0;
		} catch (Exception ex) {
			throw ex;
		} finally {
			closeConnections(connection, statement, null);
		}
	}

	@Override
	public void run() {
		if (this.task == null) return;

		for (DatabaseNotification notification : this.listeners) {
			if (notification.query()) {
				if (!notification.isStopped())
					notification.sendNotification();
				else
					this.listeners.remove(notification);
			}
		}
	}

}