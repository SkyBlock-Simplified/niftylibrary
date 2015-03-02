package net.netcoding.niftybukkit.database.pooling;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.factory.SQLFactory;

import org.bukkit.Bukkit;

/**
 * Handles database connections with connection pooling functionality.
 */
public abstract class SQLPooling extends SQLFactory implements Runnable {

	private transient Vector<Connection> availableConnections = new Vector<>();
	private boolean firstConnection = true;
	private int maximumConnections = 10;
	private int minimumConnections = 3;
	private transient Vector<Connection> usedConnections = new Vector<>();

	/**
	 * Create a new pooling instance.
	 * 
	 * @param url  Database connection url.
	 * @param user Username of the database connection.
	 * @param pass Password of the database connection.
	 * @throws SQLException
	 */
	public SQLPooling(String url, String user, String pass) throws SQLException {
		super(url, user, pass);
		Bukkit.getScheduler().runTaskTimer(NiftyBukkit.getPlugin(), this, 0, 300);
	}

	/**
	 * Create a new pooling instance.
	 * 
	 * @param url        Database connection url.
	 * @param properties Properties of the database connection.
	 * @throws SQLException
	 */
	public SQLPooling(String url, Properties properties) throws SQLException {
		super(url, properties);
		Bukkit.getScheduler().runTaskTimer(NiftyBukkit.getPlugin(), this, 0, 300);
	}

	/**
	 * Creates the pool of available connections according to currently set number of pool size.
	 * Note that connection pool must be initialized to it's full capacity, or it is emptied and objects
	 * are free to be garbage collected.
	 * 
	 * @throws SQLException
	 */
	private synchronized void initializeConnections() throws SQLException {
		if (!this.firstConnection) return;
		this.firstConnection = false;

		for (int i = 0; i < this.getMinimumConnections(); i++)
			this.availableConnections.add(new RecoverableConnection(super.getConnection(), this));
	}

	/**
	 * Gets a connection from connection pool.
	 * 
	 * @return Connection to the database.
	 * @throws SQLException When connection is not available immediately.
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return this.getConnection(WaitTime.IMMEDIATELY);
	}

	/**
	 * Gets a connection from connection pool, waiting if necessary.
	 * 
	 * @param waitTime Time to wait for a connection.
	 * @return Connection to the database.
	 * @throws SQLException When connection is not available within given wait time.
	 */
	public Connection getConnection(WaitTime waitTime) throws SQLException {
		this.initializeConnections();
		Connection connection;

		synchronized (SQLPooling.class) {
			if (this.availableConnections.size() == 0) {
				if (this.usedConnections.size() < this.getMaximumConnections())
					this.usedConnections.addElement(connection = new RecoverableConnection(super.getConnection(), this));
				else {
					if (waitTime.equals(WaitTime.IMMEDIATELY))
						throw new SQLException("No connection available at the moment.");
					else {
						try {
							Thread.sleep(waitTime.getWaitTime());
						} catch (InterruptedException ex) { }

						connection = this.getConnection();
					}
				}
			} else {
				connection = this.availableConnections.firstElement();
				this.availableConnections.removeElement(connection);
				this.usedConnections.addElement(connection);
	
				if (connection.isClosed()) {
					System.out.println("CLOSED, EXPECT ERROR");
				}
			}
		}

		if (connection.isClosed()) {
			System.out.println("CLOSED, REBOOT CONNECTION");
			connection = new RecoverableConnection(super.getConnection(), this);
		}

		return connection;
	}

	/**
	 * Gets the maximum number of concurrent connections.
	 */
	public int getMaximumConnections() {
		return this.maximumConnections;
	}

	/**
	 * Gets the minimum number of concurrent connections.
	 */
	public int getMinimumConnections() {
		return this.minimumConnections;
	}

	void recycle(Connection connection) {
		this.usedConnections.removeElement(connection);
		this.availableConnections.addElement(connection);
	}

	/**
	 * Sets the maximum number of concurrent connections.
	 * 
	 * @param count Maximum number of connections to have available.
	 */
	public void setMaximumConnections(int count) {
		this.maximumConnections = count;
	}

	/**
	 * Sets the minimum number of concurrent connections.
	 * 
	 * @param count Minimum number of connections to have available.
	 */
	public void setMinimumConnections(int count) {
		this.minimumConnections = count;
	}

	@Override
	public void run() {
		synchronized (SQLPooling.class) {
			while (this.availableConnections.size() > this.getMinimumConnections()) {
				Connection connection = this.availableConnections.lastElement();
				this.availableConnections.removeElement(connection);

				try {
					if (!connection.isClosed())
						((RecoverableConnection)connection).closeOnly();
				} catch (SQLException e) { }
			}
		}
	}

}