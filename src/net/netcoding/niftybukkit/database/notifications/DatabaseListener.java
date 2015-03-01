package net.netcoding.niftybukkit.database.notifications;

import java.sql.SQLException;

public interface DatabaseListener {

	public void onDatabaseNotification(DatabaseNotification databaseNotification) throws SQLException;

}