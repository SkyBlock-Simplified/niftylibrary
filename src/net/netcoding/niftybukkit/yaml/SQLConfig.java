package net.netcoding.niftybukkit.yaml;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;

import net.netcoding.niftybukkit.database.MySQL;
import net.netcoding.niftybukkit.database.PostgreSQL;
import net.netcoding.niftybukkit.database.SQLServer;
import net.netcoding.niftybukkit.database.factory.SQLFactory;
import net.netcoding.niftybukkit.yaml.annotations.Comment;
import net.netcoding.niftybukkit.yaml.annotations.Path;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLConfig<T extends SQLFactory> extends Config {

	private transient SQLFactory factory;

	@Comment("Database Driver (mysql, postgresql or sqlserver)")
	@Path("sql.driver")
	protected String driver = "sql";

	@Comment("Database Host")
	@Path("sql.host")
	protected String hostname = "localhost";

	@Comment("Database Username")
	@Path("sql.user")
	protected String username = "minecraft";

	@Comment("Database Password")
	@Path("sql.pass")
	protected String password = "";

	@Comment("Database Port")
	@Path("sql.port")
	protected int port = 0;

	@Comment("Database Name")
	@Path("sql.schema")
	protected String schema = "";

	public SQLConfig(JavaPlugin plugin, String fileName, String... header) {
		this(plugin, fileName, false, header);
	}

	public SQLConfig(JavaPlugin plugin, String fileName, boolean skipFailedConversion, String... header) {
		super(plugin, fileName);
	}

	public final String getHost() {
		return this.hostname;
	}

	public final String getUser() {
		return this.username;
	}

	public final String getPass() {
		return this.password;
	}

	public final int getPort() {
		return this.port;
	}

	public final String getSchema() {
		return this.schema;
	}

	public final T getSQL() {
		return this.getSuperClass().cast(this.factory);
	}

	@SuppressWarnings("unchecked")
	private Class<T> getSuperClass() {
		ParameterizedType superClass = (ParameterizedType)this.getClass().getGenericSuperclass();
		return (Class<T>)superClass.getActualTypeArguments()[0];
	}

	@Override
	public void init() throws InvalidConfigurationException {
		super.init();
		this.initSQL();
	}

	private void initSQL() throws InvalidConfigurationException {
		Class<?> clazz = this.getSuperClass();

		if (this.driver.equalsIgnoreCase("sql")) {
			if (PostgreSQL.class.isAssignableFrom(clazz)) {
				this.driver = "postgresql";
				this.port = PostgreSQL.DEFAULT_PORT;
			} else if (SQLServer.class.isAssignableFrom(clazz)) {
				this.driver = "mssql";
				this.port = SQLServer.DEFAULT_PORT;
			} else if (MySQL.class.isAssignableFrom(clazz)) {
				this.driver = "mysql";
				this.port = MySQL.DEFAULT_PORT;
			}

			if (!this.driver.equalsIgnoreCase("sql"))
				this.save();
		}
	}

	private void initFactory() throws SQLException {
		if (this.driver.equalsIgnoreCase("PostgreSQL"))
			this.factory = new PostgreSQL(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
		else if (this.driver.equalsIgnoreCase("SQLServer"))
			this.factory = new SQLServer(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
		else
			this.factory = new MySQL(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
	}

	@Override
	public void load() throws InvalidConfigurationException {
		super.load();

		try {
			this.initFactory();
		} catch (SQLException sqlex) {
			throw new InvalidConfigurationException("Invalid SQL Configuration!", sqlex);
		}
	}

}
