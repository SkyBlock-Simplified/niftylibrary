package net.netcoding.niftybukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import net.netcoding.niftybukkit.inventory.items.ItemDatabase;
import net.netcoding.niftybukkit.minecraft.BukkitPlugin;
import net.netcoding.niftybukkit.minecraft.BungeeHelper;
import net.netcoding.niftybukkit.mojang.MojangRepository;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class NiftyBukkit extends BukkitPlugin {

	private final static transient ConcurrentHashMap<String, List<String>> PLUGINS = new ConcurrentHashMap<>();
	private static transient BukkitPlugin plugin;
	private static transient ItemDatabase itemDatabase;
	private static transient MojangRepository repository;
	private static transient BungeeHelper bungeeHelper;

	@Override
	public void onEnable() {
		this.getLog().console("Registering Helpers");
		plugin = this;
		repository = new MojangRepository();
		(itemDatabase = new ItemDatabase(this)).reload();
		bungeeHelper = new BungeeHelper(this, BungeeHelper.NIFTY_CHANNEL, true);

		for (Plugin plugin : NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugins()) {
			PLUGINS.put(plugin.getName(), new ArrayList<String>());
			plugin.getLogger().addHandler(new LogHandler(plugin));
		}

		this.getLog().console("Registering Commands");
		new NiftyCommand(this);

		this.getLog().console("Registering Listeners");
		new NiftyListener(this);
	}

	@Override
	public void onDisable() {
		bungeeHelper.unregister();
	}

	public final static BungeeHelper getBungeeHelper() {
		return bungeeHelper;
	}

	public final static ItemDatabase getItemDatabase() {
		return itemDatabase;
	}

	public final static MojangRepository getMojangRepository() {
		return repository;
	}

	public final static BukkitPlugin getPlugin() {
		return plugin;
	}

	public final static List<String> getPluginCache(String pluginName) {
		return PLUGINS.keySet().contains(pluginName) ? PLUGINS.get(pluginName) : Collections.<String>emptyList();
	}

	public final static net.milkbowl.vault.permission.Permission getPermissions() {
		try {
			RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			return (permissionProvider != null ? permissionProvider.getProvider() : null);
		} catch (NoClassDefFoundError ex) { }
	
		return null;
	}

	public final static com.comphenix.protocol.ProtocolManager getProtocolManager() {
		try {
			return com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
		} catch (NoClassDefFoundError ex) { return null; }
	}

	public final static boolean hasProtocolManager() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("ProtocolLib") != null;
	}

	public final static boolean hasPermissions() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("Vault") != null;
	}

	private class LogHandler extends Handler {

		private final transient Plugin plugin;

		public LogHandler(Plugin plugin) {
			this.plugin = plugin;
		}

		@Override
		public void publish(LogRecord record) {
			if (record.getThrown() != null)
				PLUGINS.get(this.plugin.getName()).add(record.getThrown().getMessage());
		}

		@Override
		public void flush() { }

		@Override
		public void close() throws SecurityException { }
		
	}

}
