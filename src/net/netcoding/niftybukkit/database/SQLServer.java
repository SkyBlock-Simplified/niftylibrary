package net.netcoding.niftybukkit.database;

import java.sql.SQLException;

import net.netcoding.niftybukkit.database.factory.SQLWrapper;
import net.netcoding.niftybukkit.util.StringUtil;

public class SQLServer extends SQLWrapper {

	public static final int DEFAULT_PORT = 1433;

	public SQLServer(String host, String user, String pass, String schema) throws SQLException {
		this(host, DEFAULT_PORT, user, pass, schema);
	}

	public SQLServer(String host, int port, String user, String pass, String schema) throws SQLException {
		super("com.microsoft.jdbc.sqlserver.SQLServerDriver", StringUtil.format("jdbc:microsoft:sqlserver://{0}:{1,number,#};DatabaseName={2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the Microsoft SQL JDBC driver is available.
	 * 
	 * @return True if available, otherwise false.
	 */
	@Override
	public boolean isDriverAvailable() {
		return super.isDriverAvailable();
	}

}