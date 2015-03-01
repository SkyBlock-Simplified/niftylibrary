package net.netcoding.niftybukkit.database.notifications;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.MySQL;
import net.netcoding.niftybukkit.database.factory.SQLFactory;
import net.netcoding.niftybukkit.database.factory.ResultCallback;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.StringUtil;

public class DatabaseNotification extends BukkitHelper {

	public static final String ACTIVITY_TABLE = "niftybukkit_activity";
	public static final int DEFAULT_DELAY = 20;
	private final TriggerEvent event;
	private transient int recent;
	private transient boolean stopped;
	private final String name;
	private final transient SQLFactory mysql;
	private final transient DatabaseListener listener;
	private final transient List<String> primaryColumnNames = new ArrayList<String>();
	private final transient String table;

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener) throws SQLException {
		this(mysql, table, event, listener, DEFAULT_DELAY, false);
	}

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener, long delay) throws SQLException {
		this(mysql, table, event, listener, delay, false);
	}

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener, boolean overwrite) throws SQLException {
		this(mysql, table, event, listener, DEFAULT_DELAY, overwrite);
	}

	public DatabaseNotification(MySQL mysql, String table, TriggerEvent event, DatabaseListener listener, long delay, boolean overwrite) throws SQLException {
		super(NiftyBukkit.getPlugin());
		if (listener == null) throw new IllegalArgumentException("DatabaseListener cannot be null!");
		createLogTable(mysql);
		createPurgeEvent(mysql);
		this.mysql = mysql;
		this.table = table;
		this.event = event;
		this.name = StringUtil.format("on{0}{1}", this.table, this.event.toUppercase());
		this.pulse();
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
		this.primaryColumnNames.clear();
		this.primaryColumnNames.addAll(this.mysql.query("SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA` = ? AND `TABLE_NAME` = ? AND `COLUMN_KEY` = 'PRI';", new ResultCallback<List<String>>() {
			@Override
			public List<String> handle(ResultSet result) throws SQLException {
				List<String> priKeyNames = new ArrayList<>();
				while (result.next()) priKeyNames.add(result.getString("COLUMN_NAME"));
				return priKeyNames;
			}
		}, this.getSchema(), this.getTable()));
	}

	private static void createLogTable(MySQL mysql) throws SQLException {
		mysql.createTable(ACTIVITY_TABLE, "`id` INT AUTO_INCREMENT PRIMARY KEY, `schema` VARCHAR(255) NOT NULL, `table` VARCHAR(255) NOT NULL, `action` ENUM('insert', 'delete', 'update') NOT NULL, `time` INT NOT NULL, `keys` VARCHAR(255), `old` VARCHAR(255), `new` VARCHAR(255)");
	}

	private static void createPurgeEvent(MySQL mysql) throws SQLException {
		mysql.update("CREATE EVENT IF NOT EXISTS `purgeNiftyNotifications` ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 DAY DO DELETE LOW_PRIORITY FROM `niftybukkit_activity` WHERE `time` < UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 7 DAY));");
	}

	private void createTrigger() throws SQLException {
		try {
			if (this.primaryColumnNames.size() > 0) {
				String primaryKeys = StringUtil.implode(",", this.primaryColumnNames);
				String trigger = StringUtil.format("CREATE TRIGGER `{0}`.`{1}` AFTER {2} ON `{3}` FOR EACH ROW INSERT INTO `{0}`.`{4}` (`schema`, `table`, `action`, `time`, `keys`, `old`, `new`) VALUES (''{0}'', ''{3}'', ''{2}'', UNIX_TIMESTAMP(), ''{5}'', ",
						this.getSchema(), this.getName(), this.getEvent().toUppercase(), this.getTable(), ACTIVITY_TABLE, primaryKeys);
				String _old = null;
				String _new = null;
				if (this.getEvent() != TriggerEvent.INSERT) _old = StringUtil.format("CONCAT(OLD.`{0}`)", StringUtil.implode("`, ',', OLD.`", this.primaryColumnNames));
				if (this.getEvent() != TriggerEvent.DELETE) _new = StringUtil.format("CONCAT(NEW.`{0}`)", StringUtil.implode("`, ',', NEW.`", this.primaryColumnNames));
				this.mysql.update(String.format(trigger + "%s, %s);", _old, _new));
			} else
				throw new Exception(StringUtil.format("The table `{0}`.`{1}` has no primary key columns to keep track of!", this.getSchema(), this.getTable()));
		} catch (Exception ex) {
			this.getLog().console(ex);
		}
	}

	private void dropTrigger() {
		try {
			this.mysql.update(StringUtil.format("DROP TRIGGER IF EXISTS `{0}`;", this.name));
		} catch (Exception ex) { }
	}

	public TriggerEvent getEvent() {
		return this.event;
	}

	public Date getLastUpdate() {
		return new Date(this.recent);
	}

	public HashMap<String, Object> getDeletedData() throws SQLException {
		if (this.getEvent().equals(TriggerEvent.INSERT)) throw new SQLException("Cannot retrieve an inserted record!");
		final HashMap<String, Object> deleted = new HashMap<String, Object>();

		this.mysql.query(StringUtil.format("SELECT `old` FROM `{0}` WHERE `schema` = ? AND `table` = ? AND `action` = ? AND `time` = ?;", ACTIVITY_TABLE), new ResultCallback<Void>() {
			@Override
			public Void handle(ResultSet result) throws SQLException {
				if (result.next()) {
					String[] _old = result.getString("old").split(",");
					int keyCount = primaryColumnNames.size();
					for (int i = 0; i < keyCount; i++) deleted.put(primaryColumnNames.get(i), _old[i]);
				}

				return null;
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toUppercase(), this.recent);

		return deleted;
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

	public <T> void getUpdatedRow(final ResultCallback<T> resultCallback) throws SQLException {
		if (this.getEvent().equals(TriggerEvent.DELETE)) throw new SQLException("Cannot retrieve a deleted record!");

		this.mysql.query(StringUtil.format("SELECT `new` FROM `{0}` WHERE `schema` = ? AND `table` = ? AND `action` = ? AND `time` = ?;", ACTIVITY_TABLE), new ResultCallback<Void>() {
			@Override
			public Void handle(ResultSet result) throws SQLException {
				if (result.next()) {
					List<String> whereClause = new ArrayList<String>();
					int keyCount = primaryColumnNames.size();
					String[] _new = result.getString("new").split(",");

					if (keyCount != 0) {
						for (int i = 0; i < keyCount; i++) whereClause.add(StringUtil.format("SUBSTRING_INDEX(SUBSTRING_INDEX(`{0}`, '','', {1}), '','', -1) = ?", primaryColumnNames.get(i), (i + 1)));
						mysql.query(StringUtil.format("SELECT * FROM `{0}` WHERE {1};", getTable(), StringUtil.implode(" AND ", whereClause)), resultCallback, (Object[])_new);
					}
				}

				return null;
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toUppercase(), this.recent);
	}

	boolean pulse() {
		if (this.isStopped()) return false;

		try {
			return this.mysql.query(StringUtil.format("SELECT `time` FROM `{0}` WHERE `table` = ? AND `action` = ? AND `time` > ? ORDER BY `time` DESC LIMIT 1;", ACTIVITY_TABLE), new ResultCallback<Boolean>() {
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
			}, this.getTable(), this.getEvent().toUppercase(), this.recent);
		} catch (SQLException ex) {
			this.getLog().console(ex);
			this.stop();
		}

		return false;
	}

	void sendNotification() {
		try {
			this.listener.onDatabaseNotification(this);
		} catch (SQLException ex) {
			this.getLog().console(ex);
		}
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
			this.getLog().console(ex);
		}

		return false;
	}

}