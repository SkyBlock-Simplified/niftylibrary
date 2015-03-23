package net.netcoding.niftybukkit.database;

import java.sql.SQLException;

import net.netcoding.niftybukkit.database.notifications.SQLNotifications;
import net.netcoding.niftybukkit.util.StringUtil;

public class MySQL extends SQLNotifications {

	public static final int DEFAULT_PORT = 3306;

	public MySQL(String host, String user, String pass, String schema) throws SQLException {
		this(host, DEFAULT_PORT, user, pass, schema);
	}

	public MySQL(String host, int port, String user, String pass, String schema) throws SQLException {
		super("com.mysql.jdbc.Driver", StringUtil.format("jdbc:mysql://{0}:{1,number,#}/{2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the MySQL JDBC driver is available.
	 * 
	 * @return True if available, otherwise false.
	 */
	@Override
	public boolean isDriverAvailable() {
		return super.isDriverAvailable();
	}

}