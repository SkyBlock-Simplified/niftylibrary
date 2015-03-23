package net.netcoding.niftybukkit.yaml;

import java.sql.SQLException;

import net.netcoding.niftybukkit.database.MySQL;
import net.netcoding.niftybukkit.database.PostgreSQL;
import net.netcoding.niftybukkit.database.SQLServer;
import net.netcoding.niftybukkit.database.notifications.SQLNotifications;
import net.netcoding.niftybukkit.yaml.annotations.Comment;
import net.netcoding.niftybukkit.yaml.annotations.Path;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLConfig extends Config {

	private SQLNotifications sql;

	@Comment("Database Driver (mysql, postgresql or mssql)")
	@Path("sql.driver")
	protected String driver = "mysql";

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
	protected int port = 3306;

	@Comment("Database Name")
	@Path("sql.schema")
	protected String schema = "";

	public SQLConfig(JavaPlugin plugin, String fileName) throws SQLException {
		super(plugin, fileName);
		this.reloadSQL();
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

	public final SQLNotifications getSQL() {
		return this.sql;
	}

	public final void reloadSQL() throws SQLException {
		if (this.driver.equalsIgnoreCase("MySQL"))
			this.sql = new MySQL(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
		else if (this.driver.equalsIgnoreCase("PostgreSQL"))
			this.sql = new PostgreSQL(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
		else if (this.driver.equalsIgnoreCase("MSSQL"))
			this.sql = new SQLServer(this.getHost(), this.getPort(), this.getUser(), this.getPass(), this.getSchema());
	}

}
