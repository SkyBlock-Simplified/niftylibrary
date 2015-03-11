package net.netcoding.niftybukkit.database;

import java.sql.SQLException;

import net.netcoding.niftybukkit.database.notifications.SQLNotifications;
import net.netcoding.niftybukkit.util.StringUtil;

public class PostgreSQL extends SQLNotifications {

	public PostgreSQL(String host, String user, String pass, String schema) throws SQLException {
		this(host, 5432, user, pass, schema);
	}

	public PostgreSQL(String host, int port, String user, String pass, String schema) throws SQLException {
		super("org.postgresql.Driver", StringUtil.format("jdbc:postgresql://{0}:{1,number,#}/{2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the PostgreSQL JDBC driver is available.
	 * 
	 * @return True if available, otherwise false.
	 */
	@Override
	public boolean isDriverAvailable() {
		return super.isDriverAvailable();
	}

}