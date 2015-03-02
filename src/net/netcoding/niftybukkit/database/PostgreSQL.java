package net.netcoding.niftybukkit.database;

import java.sql.SQLException;

import net.netcoding.niftybukkit.database.notifications.SQLNotifications;
import net.netcoding.niftybukkit.util.StringUtil;

public class PostgreSQL extends SQLNotifications {

	private final static boolean POSTGRESQL_DRIVER_LOADED;

	static {
		boolean loaded = false;

		try {
			Class.forName("org.postgresql.Driver");
			loaded = true;
		} catch (ClassNotFoundException ex) { }

		POSTGRESQL_DRIVER_LOADED = loaded;
	}

	public PostgreSQL(String host, String user, String pass, String schema) throws SQLException {
		this(host, 5432, user, pass, schema);
	}

	public PostgreSQL(String host, int port, String user, String pass, String schema) throws SQLException {
		super(StringUtil.format("jdbc:postgresql://{0}:{1,number,#}/{2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the PostgreSQL JDBC driver is available.
	 * 
	 * @return True if available, otherwise false.
	 */
	public boolean isDriverAvailable() {
		return POSTGRESQL_DRIVER_LOADED;
	}

}