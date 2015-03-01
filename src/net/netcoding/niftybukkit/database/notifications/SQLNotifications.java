package net.netcoding.niftybukkit.database.notifications;

import java.sql.SQLException;
import java.util.Properties;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.pooling.SQLPooling;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class SQLNotifications extends SQLPooling {

	static final String ACTIVITY_TABLE = "niftybukkit_activity";
	private static final int DEFAULT_DELAY = 10;
	private final transient ConcurrentList<DatabaseNotification> listeners = new ConcurrentList<>();
	private transient BukkitTask task;

	public SQLNotifications(String url, String user, String pass) throws SQLException {
		super(url, user, pass);
	}

	public SQLNotifications(String url, Properties properties) throws SQLException {
		super(url, properties);
	}

	public void addDatabaseListener(String table, DatabaseListener notifier) throws SQLException, Exception {
		this.addDatabaseListener(table, notifier, DEFAULT_DELAY, false);
	}

	public void addDatabaseListener(String table, DatabaseListener notifier, long delay) throws SQLException, Exception {
		this.addDatabaseListener(table, notifier, delay, false);
	}

	public void addDatabaseListener(String table, DatabaseListener notifier, boolean overwrite) throws SQLException, Exception {
		this.addDatabaseListener(table, notifier, DEFAULT_DELAY, overwrite);
	}

	public void addDatabaseListener(String table, DatabaseListener notifier, long delay, boolean overwrite) throws SQLException, Exception {
		DatabaseNotification listener = new DatabaseNotification(this, table, notifier, delay, overwrite);
		this.listeners.add(listener);
		this.createLogTable();
		this.createPurgeEvent();
		if (this.task == null) this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(NiftyBukkit.getPlugin(), this, 0, delay);
	}

	private void createLogTable() throws SQLException {
		this.createTable(ACTIVITY_TABLE, "`id` INT AUTO_INCREMENT PRIMARY KEY, `schema` VARCHAR(255) NOT NULL, `table` VARCHAR(255) NOT NULL, `action` ENUM('insert', 'delete', 'update') NOT NULL, `time` INT NOT NULL, `keys` VARCHAR(255), `old` VARCHAR(255), `new` VARCHAR(255)");
	}

	private void createPurgeEvent() throws SQLException {
		update(StringUtil.format("CREATE EVENT IF NOT EXISTS `purgeNiftyNotifications` ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 DAY DO DELETE LOW_PRIORITY FROM `{0}`.`niftybukkit_activity` WHERE `time` < UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 7 DAY));", this.getSchema()));
	}

	public void removeListener(String table) {
		this.removeListener(table, false);
	}

	public void removeListener(String table, boolean dropTriggers) {
		for (DatabaseNotification listener : this.listeners) {
			if (listener.getTable().equals(table))
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

	@Override
	public void run() {
		if (this.task == null) return;

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