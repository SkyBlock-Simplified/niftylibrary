package net.netcoding.niftybukkit.database.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface AsyncResultCallback {

	public void handle(ResultSet result) throws SQLException;

}