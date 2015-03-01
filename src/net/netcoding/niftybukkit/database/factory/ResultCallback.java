package net.netcoding.niftybukkit.database.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultCallback<T> {

	public T handle(ResultSet result) throws SQLException;

}