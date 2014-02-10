package net.netcoding.niftybukkit.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallbackNR {

	public void handleResult(ResultSet result) throws SQLException, Exception;

}