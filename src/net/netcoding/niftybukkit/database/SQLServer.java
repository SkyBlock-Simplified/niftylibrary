package net.netcoding.niftybukkit.database;

import java.sql.SQLException;

import net.netcoding.niftybukkit.database.notifications.SQLNotifications;
import net.netcoding.niftybukkit.util.StringUtil;

public class SQLServer extends SQLNotifications {

	private final static boolean MSSQL_DRIVER_LOADED;

	static {
		boolean loaded = false;

		try {
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			loaded = true;
		} catch (ClassNotFoundException ex) { }

		MSSQL_DRIVER_LOADED = loaded;
	}

	public SQLServer(String host, String user, String pass, String schema) throws SQLException {
		this(host, 1433, user, pass, schema);
	}

	public SQLServer(String host, int port, String user, String pass, String schema) throws SQLException {
		super(StringUtil.format("jdbc:microsoft:sqlserver://{0}:{1,number,#};DatabaseName={2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the Microsoft SQL JDBC driver is available.
	 * 
	 * @return True if available, otherwise false.
	 */
	public boolean isDriverAvailable() {
		return MSSQL_DRIVER_LOADED;
	}

}