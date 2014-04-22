package net.netcoding.niftybukkit.database.pooling;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;

/**
 * Offers unified way to handle database connections. Provides connection pooling functionality.
 */
public class ConnectionPool extends ConnectionFactory implements Runnable {

	private boolean firstConnection = true;
	private int minimumConnections = 3;
	private int maximumConnections = 10;
	private transient Vector<Connection> usedConnections = new Vector<>();
	private transient Vector<Connection> availableConnections = new Vector<>();

	/**
	 * @param url  Database URL.
	 * @param user Username for the database connection.
	 * @param pass Password for the database connection.
	 */
	public ConnectionPool(String url, String user, String pass) {
		super(url, user, pass);
	}

	/**
	 * @param url        Database URL
	 * @param properties Properties of the connection to establish.
	 */
	public ConnectionPool(String url, Properties properties) {
		super(url, properties);
		Bukkit.getScheduler().runTaskTimer(NiftyBukkit.getPlugin(), this, 0, 300);
	}

	/**
	 * Creates the pool of available connections according to currently set number of pool size.
	 * Note that connection pool must be initialized to it's full capacity, or it is emptied and objects
	 * are free to be garbage collected.
	 *
	 * @throws SQLException If case of not finishing creating connection pool, this exception is thrown.
	 */
	private synchronized void initializeConnections() throws SQLException {
		if (!this.firstConnection) return;
		this.firstConnection = false;

		try {
			for (int i = 0; i < this.getMinimumConnections(); i++)
				this.availableConnections.add(new RecoverableConnection(super.getConnection(), this));
		} catch (SQLException ex) {
			this.availableConnections.removeAll(this.availableConnections);
			this.availableConnections = new Vector<>();
			throw new SQLException(StringUtil.format("Could not establish connection: {0}", ex.getMessage()), ex);
		}
	}

	/**
	 * Returns a connection from connection pool.
	 *
	 * @param waitTime Time to wait for a connection.
	 * @return Connection to the database.
	 * @throws SQLException When connection is not available within given wait time.
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return this.getConnection(WaitTime.IMMEDIATELY);
	}

	/**
	 * Returns a connection from connection pool.
	 *
	 * @param waitTime Time to wait for a connection.
	 * @return Connection to the database.
	 * @throws SQLException When connection is not available within given wait time.
	 */
	public Connection getConnection(WaitTime waitTime) throws SQLException {
		this.initializeConnections();
		Connection connection;

		//System.out.println("used connections: " + this.usedConnections.size());
		//System.out.println("available connections: " + this.availableConnections.size());

		synchronized (ConnectionPool.class) {
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
				connection = this.availableConnections.lastElement();
				this.availableConnections.removeElement(connection);
				this.usedConnections.addElement(connection);
	
				if (connection.isClosed()) {
					System.out.println("CLOSED, EXPECT ERROR");
				}
			}
		}

		if (connection.isClosed()) {
			System.out.println("reboot connection");
			connection = new RecoverableConnection(super.getConnection(), this);
		}

		return connection;
	}

	/**
	 * Returns the maximum number of concurrent connections.
	 */
	public int getMaximumConnections() {
		return this.maximumConnections;
	}

	/**
	 * Returns the minimum number of concurrent connections.
	 */
	public int getMinimumConnections() {
		return this.minimumConnections;
	}

	/**
	 * Returns a connection to the connection pool, making it available for use.
	 */
	public void recycle(Connection connection) {
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
		synchronized (this) {
			while (this.availableConnections.size() > this.getMinimumConnections()) {
				Connection connection = this.availableConnections.lastElement();
				this.availableConnections.removeElement(connection);
				//((RecoverableConnection)connection).closeOnly();
			}
		}
	}

}