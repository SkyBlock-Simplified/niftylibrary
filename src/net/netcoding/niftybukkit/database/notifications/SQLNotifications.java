package net.netcoding.niftybukkit.database.notifications;

import java.sql.SQLException;
import java.util.Properties;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.pooling.SQLPooling;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

import org.bukkit.Bukkit;

/**
 * Adds support for notifications through an sql server instance.
 */
public abstract class SQLNotifications extends SQLPooling implements Runnable {

	static final String ACTIVITY_TABLE = "niftybukkit_activity";
	private static final int DEFAULT_DELAY = 10;
	private final transient ConcurrentSet<DatabaseNotification> listeners = new ConcurrentSet<>();
	private int taskId = -1;

	/**
	 * Create a new notification instance.
	 * 
	 * @param url        Database connection url.
	 * @param properties Properties of the database connection.
	 * @throws SQLException
	 */
	public SQLNotifications(String url, Properties properties) throws SQLException {
		super(url, properties);
	}

	/**
	 * Create a new notification instance.
	 * 
	 * @param url  Database connection url.
	 * @param user Username of the database connection.
	 * @param pass Password of the database connection.
	 * @throws SQLException
	 */
	public SQLNotifications(String url, String user, String pass) throws SQLException {
		super(url, user, pass);
	}

	/**
	 * Add a listener on the given table.
	 * 
	 * @param table    Table name to listen to.
	 * @param notifier Listener to send notifications to.
	 * @throws SQLException
	 */
	public void addListener(String table, DatabaseListener notifier) throws SQLException {
		this.addListener(table, notifier, DEFAULT_DELAY, false);
	}

	/**
	 * Add a listener on the given table.
	 * 
	 * @param table     Table name to listen to.
	 * @param notifier  Listener to send notifications to.
	 * @param overwrite True to overwrite the triggers in the database, otherwise false.
	 * @throws SQLException
	 */
	public void addListener(String table, DatabaseListener notifier, boolean overwrite) throws SQLException {
		this.addListener(table, notifier, DEFAULT_DELAY, overwrite);
	}

	/**
	 * Add a listener on the given table.
	 * 
	 * @param table    Table name to listen to.
	 * @param notifier Listener to send notifications to.
	 * @param delay    How long in ticks to wait before checking.
	 * @throws SQLException
	 */
	public void addListener(String table, DatabaseListener notifier, long delay) throws SQLException {
		this.addListener(table, notifier, delay, false);
	}

	/**
	 * Add a listener on the given table.
	 * 
	 * @param table     Table name to listen to.
	 * @param notifier  Listener to send notifications to.
	 * @param delay     How long in ticks to wait before checking.
	 * @param overwrite True to overwrite the triggers in the database, otherwise false.
	 * @throws SQLException
	 */
	public void addListener(String table, DatabaseListener notifier, long delay, boolean overwrite) throws SQLException {
		this.listeners.add(new DatabaseNotification(this, table, notifier, delay, overwrite));
		this.createLogTable();
		this.createPurgeEvent();

		if (this.taskId == -1) this.taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(NiftyBukkit.getPlugin(), this, 0, delay).getTaskId();
		//if (this.task == null) this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(NiftyBukkit.getPlugin(), this, 0, delay);
	}

	private void createLogTable() throws SQLException {
		this.createTable(ACTIVITY_TABLE, "`id` INT AUTO_INCREMENT PRIMARY KEY, `schema` VARCHAR(255) NOT NULL, `table` VARCHAR(255) NOT NULL, `action` ENUM('insert', 'delete', 'update') NOT NULL, `time` INT NOT NULL, `keys` VARCHAR(255), `old` VARCHAR(255), `new` VARCHAR(255)");
	}

	private void createPurgeEvent() throws SQLException {
		update(StringUtil.format("CREATE EVENT IF NOT EXISTS `purgeNiftyNotifications` ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 DAY DO DELETE LOW_PRIORITY FROM `{0}`.`niftybukkit_activity` WHERE `time` < UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 7 DAY));", this.getSchema()));
	}

	public boolean isRunning() {
		return this.taskId != -1;
	}

	/**
	 * Remove all listeners.
	 */
	public void removeListeners() {
		this.removeListener(null);
	}

	/**
	 * Remove all listeners.
	 * 
	 * @param dropTriggers True to drop triggers, otherwise false.
	 */
	public void removeListeners(boolean dropTriggers) {
		this.removeListener(null, dropTriggers);
	}

	/**
	 * Remove listener from the given table.
	 * 
	 * @param table Table name to remove listeners from.
	 */
	public void removeListener(String table) {
		this.removeListener(table, false);
	}

	/**
	 * Remove listener from the given table.
	 * 
	 * @param table        Table name to remove listeners from.
	 * @param dropTriggers True to drop triggers, otherwise false.
	 */
	public void removeListener(String table, boolean dropTriggers) {
		for (DatabaseNotification listener : this.listeners) {
			if (listener.getTable().equals(table) || StringUtil.isEmpty(table))
				listener.stop(dropTriggers);
		}

		if (this.listeners.size() == 0) {
			if (this.taskId != -1) {
				Bukkit.getServer().getScheduler().cancelTask(this.taskId);
				this.taskId = -1;
			}
		}
	}

	@Override
	public void run() {
		for (DatabaseNotification notification : this.listeners) {
			if (notification.isStopped()) {
				this.listeners.remove(notification);
				continue;
			}

			if (notification.pulse())
				notification.sendNotification();
		}
	}

}