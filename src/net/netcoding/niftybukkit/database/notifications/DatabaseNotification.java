package net.netcoding.niftybukkit.database.notifications;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.factory.AsyncResultCallback;
import net.netcoding.niftybukkit.database.factory.ResultCallback;
import net.netcoding.niftybukkit.database.factory.SQLFactory;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.StringUtil;

/**
 * An sql listener used to check for updates to its associated table and notify plugins.
 */
public class DatabaseNotification extends BukkitHelper {

	private TriggerEvent event;
	private final transient DatabaseListener listener;
	private final List<String> primaryColumnNames = new ArrayList<String>();
	private int previousId = 0;
	private final transient SQLFactory sql;
	private volatile boolean stopped = false;
	private final String table;

	DatabaseNotification(SQLFactory sql, String table, DatabaseListener listener, long delay, boolean overwrite) throws SQLException {
		super(NiftyBukkit.getPlugin());
		if (listener == null) throw new IllegalArgumentException("DatabaseListener cannot be null!");
		this.sql = sql;
		this.table = table;
		this.pulse();
		this.listener = listener;
		this.loadPrimaryKeys();

		if (!this.triggersExist() || overwrite) {
			for (TriggerEvent event : TriggerEvent.values()) {
				this.dropTrigger(event);
				this.createTrigger(event);
			}
		}
	}

	private void createTrigger(TriggerEvent event) throws SQLException {
		try {
			if (this.primaryColumnNames.size() > 0) {
				String primaryKeys = StringUtil.implode(",", this.primaryColumnNames);
				String trigger = StringUtil.format("CREATE TRIGGER `{0}`.`{1}` AFTER {2} ON `{3}` FOR EACH ROW INSERT INTO `{0}`.`{4}` (`schema`, `table`, `action`, `time`, `keys`, `old`, `new`) VALUES (''{0}'', ''{3}'', ''{2}'', UNIX_TIMESTAMP(), ''{5}'', ",
						this.getSchema(), this.getName(event), event.toUppercase(), this.getTable(), SQLNotifications.ACTIVITY_TABLE, primaryKeys);
				String _old = null;
				String _new = null;
				if (!TriggerEvent.INSERT.equals(event)) _old = StringUtil.format("CONCAT(OLD.`{0}`)", StringUtil.implode("`, ',', OLD.`", this.primaryColumnNames));
				if (!TriggerEvent.DELETE.equals(event)) _new = StringUtil.format("CONCAT(NEW.`{0}`)", StringUtil.implode("`, ',', NEW.`", this.primaryColumnNames));
				this.sql.updateAsync(String.format(trigger + "%s, %s);", _old, _new));
			} else
				throw new Exception(StringUtil.format("The table `{0}`.`{1}` has no primary key columns to keep track of!", this.getSchema(), this.getTable()));
		} catch (Exception ex) {
			this.getLog().console(ex);
		}
	}

	private void dropTrigger(TriggerEvent event) {
		try {
			this.sql.update(StringUtil.format("DROP TRIGGER IF EXISTS `{0}`;", this.getName(event)));
		} catch (Exception ex) { }
	}

	/**
	 * Gets the primary keys and associated deleted data of the current notification.
	 * 
	 * @return Map of primary keys and associated deleted data.
	 * @throws SQLException If you attempt to retrieve deleted data when inserting a record.
	 */
	public HashMap<String, Object> getDeletedData() throws SQLException {
		if (this.getEvent().equals(TriggerEvent.INSERT)) throw new SQLException("Cannot retrieve an inserted record!");
		final HashMap<String, Object> deleted = new HashMap<String, Object>();

		this.sql.query(StringUtil.format("SELECT `old` FROM `{0}` WHERE `schema` = ? AND `table` = ? AND `action` = ? AND `id` = ?;", SQLNotifications.ACTIVITY_TABLE), new ResultCallback<Void>() {
			@Override
			public Void handle(ResultSet result) throws SQLException {
				if (result.next()) {
					String[] _old = result.getString("old").split(",");
					int keyCount = primaryColumnNames.size();
					for (int i = 0; i < keyCount; i++) deleted.put(primaryColumnNames.get(i), _old[i]);
				}

				return null;
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toUppercase(), this.previousId);

		return deleted;
	}

	/**
	 * Gets the event of the current notification.
	 * 
	 * @return Event type of the current notification.
	 */
	public TriggerEvent getEvent() {
		return this.event;
	}

	private String getName(TriggerEvent event) {
		return StringUtil.format("on{0}{1}", this.getTable(), event.toUppercase());
	}

	/**
	 * Gets the schema of the current notification.
	 * 
	 * @return Database name of the current notification.
	 */
	public String getSchema() {
		return this.sql.getSchema();
	}

	/**
	 * Gets the table of the current notification.
	 * 
	 * @return Table name of the current notification.
	 */
	public String getTable() {
		return this.table;
	}

	/**
	 * Gets the updated data of the current notification.
	 * 
	 * @param callback Callback class to handle retrieved data.
	 * @throws SQLException If you attempt to retrieve updated data when deleting a record.
	 */
	public void getUpdatedRow(final AsyncResultCallback callback) throws SQLException {
		if (this.getEvent().equals(TriggerEvent.DELETE)) throw new SQLException("Cannot retrieve a deleted record!");

		this.sql.queryAsync(StringUtil.format("SELECT `new` FROM `{0}` WHERE `schema` = ? AND `table` = ? AND `action` = ? AND `id` = ?;", SQLNotifications.ACTIVITY_TABLE), new AsyncResultCallback() {
			@Override
			public void handle(ResultSet result) throws SQLException {
				if (result.next()) {
					List<String> whereClause = new ArrayList<String>();
					int keyCount = primaryColumnNames.size();
					String[] _new = result.getString("new").split(",");

					if (keyCount != 0) {
						for (int i = 0; i < keyCount; i++) whereClause.add(StringUtil.format("SUBSTRING_INDEX(SUBSTRING_INDEX(`{0}`, '','', {1}), '','', -1) = ?", primaryColumnNames.get(i), (i + 1)));
						sql.queryAsync(StringUtil.format("SELECT * FROM `{0}` WHERE {1};", getTable(), StringUtil.implode(" AND ", whereClause)), callback, (Object[])_new);
					}
				}
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toUppercase(), this.previousId);
	}

	/**
	 * Gets if the current notification has stopped.
	 * 
	 * @return True if has stopped, otherwise false.
	 */
	public synchronized boolean isStopped() {
		return this.stopped;
	}

	private void loadPrimaryKeys() throws SQLException {
		this.primaryColumnNames.clear();
		this.primaryColumnNames.addAll(this.sql.query("SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA` = ? AND `TABLE_NAME` = ? AND `COLUMN_KEY` = 'PRI';", new ResultCallback<List<String>>() {
			@Override
			public List<String> handle(ResultSet result) throws SQLException {
				List<String> priKeyNames = new ArrayList<>();
				while (result.next()) priKeyNames.add(result.getString("COLUMN_NAME"));
				return priKeyNames;
			}
		}, this.getSchema(), this.getTable()));
	}

	boolean pulse() {
		if (this.isStopped()) return false;

		try {
			return this.sql.query(StringUtil.format("SELECT `id`, `action` FROM `{0}` WHERE `table` = ? AND `action` IN (''INSERT'', ''UPDATE'', ''DELETE'') AND `id` > ? ORDER BY `id` {1}SC LIMIT 1;", SQLNotifications.ACTIVITY_TABLE, (this.previousId == 0 ? "DE" : "A")), new ResultCallback<Boolean>() {
				@Override
				public Boolean handle(ResultSet result) throws SQLException {
					if (result.next()) {
						int last = result.getInt("id");

						if (last > previousId) {
							previousId = last;
							event = TriggerEvent.fromString(result.getString("action"));
							return true;
						}
					}

					return false;
				}
			}, this.getTable(), this.previousId);
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

	/**
	 * Stops this class from listening for further notifications.
	 */
	public synchronized void stop() {
		this.stop(false);
	}

	/**
	 * Stops this class from listening for further notifications, and optionally delete the trigger from the database.
	 * 
	 * @param dropTriggers True to delete the triggers, otherwise false.
	 */
	public synchronized void stop(boolean dropTriggers) {
		this.stopped = true;

		if (dropTriggers) {
			for (TriggerEvent event : TriggerEvent.values())
				this.dropTrigger(event);
		}
	}

	private boolean triggersExist() {
		try {
			return this.sql.query("SELECT `TRIGGER_NAME` FROM `INFORMATION_SCHEMA`.`TRIGGERS` WHERE `TRIGGER_SCHEMA` = ? AND `TRIGGER_NAME` IN (?, ?, ?);", new ResultCallback<Boolean>() {
				@Override
				public Boolean handle(ResultSet result) throws SQLException {
					int count = 0;
					while (result.next()) count++;
					return count == 3;
				}
			}, this.getSchema(), this.getName(TriggerEvent.DELETE), this.getName(TriggerEvent.INSERT), this.getName(TriggerEvent.UPDATE));
		} catch (Exception ex) {
			this.getLog().console(ex);
		}

		return false;
	}

}