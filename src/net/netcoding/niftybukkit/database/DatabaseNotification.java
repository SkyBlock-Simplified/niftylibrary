package net.netcoding.niftybukkit.database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.netcoding.niftybukkit.util.StringUtil;

public class DatabaseNotification {

	public static final String ACTIVITY_TABLE = "ndb_activity";
	private final transient TriggerEvent event;
	private transient int recent;
	private transient boolean stopped;
	private final transient String name;
	private final transient MySQL mysql;
	private final transient DatabaseListener listener;
	private final transient List<String> primaryColumnNames = new ArrayList<String>();
	private final transient String table;

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener) throws SQLException {
		this(mysql, table, event, listener, MySQL.DEFAULT_DELAY, false);
	}

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener, long delay) throws SQLException {
		this(mysql, table, event, listener, delay, false);
	}

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener, boolean overwrite) throws SQLException {
		this(mysql, table, event, listener, MySQL.DEFAULT_DELAY, overwrite);
	}

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener, long delay, boolean overwrite) throws SQLException {
		createLogTable(mysql);
		if (listener == null) throw new IllegalArgumentException("DatabaseListener cannot be null!");
		this.mysql    = mysql;
		this.table    = table;
		this.event    = event;
		this.name     = String.format("on%s%s", this.table, this.event.toUppercase());
		this.query(); // Load latest activity to avoid errors
		this.listener = listener;
		this.loadPrimaryKeys();

		if (!this.triggerExists() || overwrite) {
			this.dropTrigger();
			this.createTrigger();
		}
	}

	public boolean isStopped() {
		return this.stopped;
	}

	private void loadPrimaryKeys() throws SQLException {
		this.mysql.query("SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA` = ? AND `TABLE_NAME` = ? AND `COLUMN_KEY` = 'PRI';", new ResultCallback<Void>() {
			@Override
			public Void handle(ResultSet result) throws SQLException {
				while (result.next()) primaryColumnNames.add(result.getString("COLUMN_NAME"));
				return null;
			}
		}, this.getSchema(), this.getTable());
	}

	private static void createLogTable(MySQL mysql) throws SQLException {
		mysql.createTable(ACTIVITY_TABLE, "`id` INT AUTO_INCREMENT PRIMARY KEY, `schema` VARCHAR(255) NOT NULL, `table` VARCHAR(255) NOT NULL, `action` ENUM('insert', 'delete', 'update') NOT NULL, `time` INT NOT NULL, `keys` VARCHAR(255), `old` VARCHAR(255), `new` VARCHAR(255)");
	}

	private void createTrigger() throws SQLException {
		try {

			if (this.primaryColumnNames.size() > 0) {
				String primaryKeys = StringUtil.implode(",", this.primaryColumnNames);
				String trigger = String.format("CREATE TRIGGER `%s`.`%s` AFTER %s ON `%s` FOR EACH ROW INSERT INTO `%s`.`%s` (`schema`, `table`, `action`, `time`, `keys`, `old`, `new`) VALUES ('%s', '%s', '%s', UNIX_TIMESTAMP(), '%s', ",
						this.getSchema(), this.getName(), this.getEvent().toUppercase(), this.getTable(), this.getSchema(), ACTIVITY_TABLE, this.getSchema(), this.getTable(), this.getEvent().toLowercase(), primaryKeys);
				String _old = null;
				String _new = null;
				if (this.getEvent() != TriggerEvent.INSERT) _old = String.format("CONCAT(OLD.`%s`)", StringUtil.implode("`, ',', OLD.`", this.primaryColumnNames));
				if (this.getEvent() != TriggerEvent.DELETE) _new = String.format("CONCAT(NEW.`%s`)", StringUtil.implode("`, ',', NEW.`", this.primaryColumnNames));
				this.mysql.update(String.format(trigger + "%s, %s);", _old, _new));
			} else
				throw new Exception(String.format("The table `%s`.`%s` has no primary key columns to keep track of!", this.getSchema(), this.getTable()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void dropTrigger() {
		try {
			this.mysql.update(String.format("DROP TRIGGER IF EXISTS `%s`;", this.name));
		} catch (Exception ex) { }
	}

	public TriggerEvent getEvent() {
		return this.event;
	}

	public Date getLastUpdate() {
		return new Date(this.recent);
	}

	public HashMap<String, Object> getDeletedData() throws SQLException {
		if (this.getEvent() != TriggerEvent.DELETE) throw new SQLException("Can only retrieve deleted information!");
		final HashMap<String, Object> deleted = new HashMap<String, Object>();

		this.mysql.query(String.format("SELECT `old` FROM `%s` WHERE `schema` = ? AND `table` = ? AND `action` = ? AND `time` = ?;", ACTIVITY_TABLE), new ResultCallback<Void>() {
			@Override
			public Void handle(ResultSet result) throws SQLException {
				if (result.next()) {
					String[] _old = result.getString("old").split(",");
					int keyCount = primaryColumnNames.size();
					for (int i = 0; i < keyCount; i++) deleted.put(primaryColumnNames.get(i), _old[i]);
				}

				return null;
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toLowercase(), this.recent);

		return deleted;
	}

	public <T> void getUpdatedRow(final ResultCallback<T> resultCallback) throws SQLException {
		if (this.getEvent() == TriggerEvent.DELETE) throw new SQLException("Cannot retrieve a deleted record!");

		this.mysql.query(String.format("SELECT `new` FROM `%s` WHERE `schema` = ? AND `table` = ? AND `action` = ? AND `time` = ?;", ACTIVITY_TABLE), new ResultCallback<Void>() {
			@Override
			public Void handle(ResultSet result) throws SQLException {
				if (result.next()) {
					List<String> whereClause = new ArrayList<String>();
					int keyCount = primaryColumnNames.size();
					String[] _new = result.getString("new").split(",");

					if (keyCount != 0) {
						for (int i = 0; i < keyCount; i++) whereClause.add(String.format("SUBSTRING_INDEX(SUBSTRING_INDEX(`%s`, ',', %s), ',', -1) = ?", primaryColumnNames.get(i), (i + 1)));
						mysql.query(String.format("SELECT * FROM `%s` WHERE %s;", getTable(), StringUtil.implode(" AND ", whereClause)), resultCallback, (Object[])_new);
					}
				}

				return null;
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toLowercase(), this.recent);
	}

	public String getName() {
		return this.name;
	}

	public String getSchema() {
		return this.mysql.getSchema();
	}

	public String getTable() {
		return this.table;
	}

	boolean query() {
		try {
			return this.mysql.query(String.format("SELECT `time` FROM `%s` WHERE `table` = ? AND `action` = ? AND `time` > ? ORDER BY `time` DESC;", ACTIVITY_TABLE), new ResultCallback<Boolean>() {
				@Override
				public Boolean handle(ResultSet result) throws SQLException {
					if (result.next()) {
						int last = result.getInt("time");

						if (last != recent) {
							recent = last;
							return true;
						}
					}

					return false;
				}
			}, this.table, this.event.toLowercase(), this.recent);
		} catch (SQLException ex) {
			ex.printStackTrace();
			this.stop();
		}

		return false;
	}

	void sendNotification() {
		this.listener.onDatabaseNotification(this);
	}

	public void stop() {
		this.stop(false);
	}

	public void stop(boolean dropTrigger) {
		this.stopped = true;
		if (dropTrigger) this.dropTrigger();
	}

	private boolean triggerExists() {
		try {
			return this.mysql.query("SELECT `TRIGGER_NAME` FROM `INFORMATION_SCHEMA`.`TRIGGERS` WHERE `TRIGGER_SCHEMA` = ? AND `TRIGGER_NAME` = ?;", new ResultCallback<Boolean>() {
				@Override
				public Boolean handle(ResultSet result) throws SQLException {
					return result.next();
				}
			}, this.getSchema(), this.getName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

}