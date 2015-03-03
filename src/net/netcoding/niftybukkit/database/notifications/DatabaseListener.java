package net.netcoding.niftybukkit.database.notifications;

import java.sql.SQLException;

public interface DatabaseListener {

	public void onDatabaseNotification(final DatabaseNotification databaseNotification) throws SQLException;

}