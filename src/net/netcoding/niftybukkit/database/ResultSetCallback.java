package net.netcoding.niftybukkit.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallback {

	public Object handleResult(ResultSet result) throws SQLException, Exception;

}