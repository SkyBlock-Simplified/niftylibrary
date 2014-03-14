package net.netcoding.niftybukkit;

import java.lang.reflect.Field;

import net.netcoding.niftybukkit.database.MySQL;
import net.netcoding.niftybukkit.inventory.items.ItemDatabase;
import net.netcoding.niftybukkit.minecraft.BukkitPlugin;
import net.netcoding.niftybukkit.minecraft.BungeeHelper;
import net.netcoding.niftybukkit.mojang.ProfileRepository;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

public class NiftyBukkit extends BukkitPlugin {

	private static transient BukkitPlugin plugin;
	private static transient ItemDatabase itemDatabase;
	private static final transient String bukkitPath;
	private static final transient String minecraftPath;
	private static transient MySQL mysql;
	private static transient boolean isMysqlMode;
	private static transient ProfileRepository repository;

	static {
		Class<?> craftServer = Bukkit.getServer().getClass();
		bukkitPath = craftServer.getPackage().getName();
		String mcPath = "";

		try {
			Field mcServer = craftServer.getDeclaredField("console");
			mcPath = mcServer.getType().getPackage().getName();
		} catch (Exception ex) { }
		
		minecraftPath = mcPath;
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.saveConfig();
		plugin = this;
		repository = new ProfileRepository();
		itemDatabase = new ItemDatabase(this);
		itemDatabase.reload();

		try {
			FileConfiguration config = this.getConfig();
			mysql = new MySQL(config.getString("host"), config.getInt("port"),
					config.getString("schema"), config.getString("user"),
					config.getString("pass"));
		} catch (Exception ex) { }

		if (isMysqlMode = mysql.testConnection()) {
			mysql.setAutoReconnect();
			this.getLog().console("Using MySQL Storage");
		} else
			this.getLog().console("Using YAML Storage");

		new NiftyListener(this);
		new BungeeHelper(this).register();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
	}

	@Override
	public void onDisable() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		repository.save();
	}

	public static String getBukkitPackage() {
		return bukkitPath;
	}

	public static ItemDatabase getItemDatabase() {
		return itemDatabase;
	}

	public static String getMinecraftPackage() {
		return minecraftPath;
	}

	public static ProfileRepository getMojangRepository() {
		return repository;
	}

	public static MySQL getMySQL() {
		return mysql;
	}

	public static BukkitPlugin getPlugin() {
		return plugin;
	}

	public static net.milkbowl.vault.permission.Permission getPermissions() {
		try {
			RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			return (permissionProvider != null ? permissionProvider.getProvider() : null);
		} catch (Exception ex) { }
	
		return null;
	}

	public static com.comphenix.protocol.ProtocolManager getProtocolManager() {
		try {
			return com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
		} catch (Exception ex) { return null; }
	}

	public static boolean isMysqlMode() {
		return isMysqlMode;
	}

	public static boolean protocolManagerExists() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("ProtocolLib") != null;
	}

	public static boolean vaultExists() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("Vault") != null;
	}

}
