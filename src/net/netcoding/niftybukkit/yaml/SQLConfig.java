package net.netcoding.niftybukkit.yaml;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;

import net.netcoding.niftybukkit.database.MySQL;
import net.netcoding.niftybukkit.database.PostgreSQL;
import net.netcoding.niftybukkit.database.SQLServer;
import net.netcoding.niftybukkit.database.factory.SQLWrapper;
import net.netcoding.niftybukkit.yaml.annotations.Comment;
import net.netcoding.niftybukkit.yaml.annotations.Path;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLConfig<T extends SQLWrapper> extends Config {

	private transient SQLWrapper factory;

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

	public final SQLWrapper getSQL() {
		return this.getSuperClass().cast(this.factory);
	}

	public final T getCastedSQL() {
		return this.getSuperClass().cast(this.factory);
	}

	@SuppressWarnings("unchecked")
	private Class<T> getSuperClass() {
		ParameterizedType superClass = (ParameterizedType)this.getClass().getGenericSuperclass();
		return (Class<T>)(superClass.getActualTypeArguments().length == 0 ? SQLWrapper.class : superClass.getActualTypeArguments()[0]);
	}

	private void handleForcedWrapper(boolean loading) {
		if (!SQLWrapper.class.equals(this.getSuperClass())) {
			if (this.root.has("sql")) {
				ConfigSection sql = (ConfigSection)this.root.get("sql");
				sql.remove("driver");
			}

			if (loading) {
				if (this.driver.equalsIgnoreCase("sql")) {
					Class<T> clazz = this.getSuperClass();
					this.driver = clazz.getSimpleName().toLowerCase();

					if (this.port == 0) {
						try {
							Field portField = clazz.getField("DEFAULT_PORT");
							this.port = portField.getInt(null);
						} catch (Exception ex) { }
					}
				}
			}
		}
	}

	@Override
	public void init() throws InvalidConfigurationException {
		super.init();
		//this.initSQL();
	}

	/*private void initSQL() throws InvalidConfigurationException {
		Class<?> clazz = this.getSuperClass();

		if (!SQLWrapper.class.equals(this.getSuperClass())) {
			if (this.driver.equalsIgnoreCase("sql")) {
				try {
					Field portField = clazz.getField("DEFAULT_PORT");
					this.port = portField.getInt(null);

					if (PostgreSQL.class.isAssignableFrom(clazz))
						this.driver = "postgresql";
					else if (SQLServer.class.isAssignableFrom(clazz))
						this.driver = "sqlserver";
					else if (MySQL.class.isAssignableFrom(clazz))
						this.driver = "mysql";
				} catch (Exception ex) { }

				if (!this.driver.equalsIgnoreCase("sql"))
					this.save();
			}
		}
	}*/

	protected void initFactory() throws SQLException {
		if (this.driver.equalsIgnoreCase("PostgreSQL"))
			this.factory = new PostgreSQL(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
		else if (this.driver.equalsIgnoreCase("SQLServer"))
			this.factory = new SQLServer(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
		else if (this.driver.equalsIgnoreCase("MySQL"))
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

	@Override
	protected void loadFromYaml() {
		super.loadFromYaml();
		this.handleForcedWrapper(true);
	}

	@Override
	protected void saveToYaml() throws InvalidConfigurationException {
		this.handleForcedWrapper(false);
		super.saveToYaml();
	}

}
