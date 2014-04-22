package net.netcoding.niftybukkit.database;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.pooling.ConnectionPool;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class MySQL extends ConnectionPool implements Runnable {

	private final static boolean JDBC_DRIVER_LOADED;
	private final String schema;
	static final transient int DEFAULT_DELAY = 20;
	private final transient List<DatabaseNotification> listeners = Collections.synchronizedList(new ArrayList<DatabaseNotification>());
	private transient BukkitTask task;

	static {
		boolean loaded = false;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			loaded = true;
		} catch (ClassNotFoundException ex) { }

		JDBC_DRIVER_LOADED = loaded;
	}

	public MySQL(String host, String user, String pass, String schema) throws SQLException {
		this(host, 3306, user, pass, schema);
	}

	public MySQL(String host, int port, String user, String pass, String schema) throws SQLException {
		super(StringUtil.format("jdbc:mysql://{0}:{1,number,#}/{2}", host, port, schema), user, pass);
		this.schema = schema;
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

	public boolean createTable(String name, String query) throws SQLException {
		try (Connection connection = this.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				return statement.executeUpdate(StringUtil.format("CREATE TABLE IF NOT EXISTS `{0}` ({1}) ENGINE=InnoDB;", name, query)) > 0;
			}
		}
	}

	public String getSchema() {
		return this.schema;
	}

	public boolean isDriverAvailable() {
		return JDBC_DRIVER_LOADED;
	}

	public <T> T query(String sql, ResultCallback<T> callback, Object... args) throws SQLException {
		try (Connection connection = this.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				assign(statement, args);
				statement.executeQuery();

				if (callback != null)
					return callback.handle(statement.getResultSet());
				else
					return null;
			}
		}
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
			if (this.task != null) {
				this.task.cancel();
				this.task = null;
			}
		}
	}

	public void stopListening() {
		this.stopListening(false);
	}

	public void stopListening(boolean dropTriggers) {
		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}

		for (DatabaseNotification listener : this.listeners)
			listener.stop(dropTriggers);
	}

	public boolean update(String sql, Object... args) throws SQLException {
		try (Connection connection = this.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				assign(statement, args);
				return statement.executeUpdate() > 0;
			}
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