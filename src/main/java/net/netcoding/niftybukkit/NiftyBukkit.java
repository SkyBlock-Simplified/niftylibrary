package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.minecraft.items.enchantments.EnchantmentDatabase;
import net.netcoding.niftybukkit.minecraft.items.ItemDatabase;
import net.netcoding.niftybukkit.minecraft.BukkitPlugin;
import net.netcoding.niftybukkit.minecraft.messages.BungeeHelper;
import net.netcoding.niftybukkit.mojang.BukkitMojangRepository;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public final class NiftyBukkit extends BukkitPlugin {

	private final static transient ConcurrentHashMap<String, ConcurrentList<String>> PLUGINS = new ConcurrentHashMap<>();
	private static transient BukkitPlugin plugin;
	private static transient ItemDatabase itemDatabase;
	private static transient EnchantmentDatabase enchantmentDatabase;
	private static transient BukkitMojangRepository repository;
	private static transient BungeeHelper bungeeHelper;

	@Override
	public void onEnable() {
		this.getLog().console("Registering Helpers");
		plugin = this;
		repository = new BukkitMojangRepository();
		bungeeHelper = new BungeeHelper(this, BungeeHelper.NIFTY_CHANNEL, true);

		PLUGINS.put("Bukkit", new ConcurrentList<String>());
		Bukkit.getLogger().addHandler(new LogHandler());
		for (Plugin plugin : NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugins()) {
			PLUGINS.put(plugin.getName(), new ConcurrentList<String>());
			plugin.getLogger().addHandler(new LogHandler(plugin));
		}

		new NiftyCommand(this);
		new NiftyListener(this);

		(itemDatabase = new ItemDatabase(this)).reload();
		(enchantmentDatabase = new EnchantmentDatabase(this)).reload();
	}

	@Override
	public void onDisable() {
		bungeeHelper.unregister();
	}

	public static BungeeHelper getBungeeHelper() {
		return bungeeHelper;
	}

	public static EnchantmentDatabase getEnchantmentDatabase() {
		return enchantmentDatabase;
	}

	public static ItemDatabase getItemDatabase() {
		return itemDatabase;
	}

	public static BukkitMojangRepository getMojangRepository() {
		return repository;
	}

	public static BukkitPlugin getPlugin() {
		return plugin;
	}

	public static List<String> getPluginCache(String pluginName) {
		return PLUGINS.keySet().contains(pluginName) ? PLUGINS.get(pluginName) : Collections.<String>emptyList();
	}

	public static net.milkbowl.vault.permission.Permission getPermissions() {
		try {
			RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			return (permissionProvider != null ? permissionProvider.getProvider() : null);
		} catch (NoClassDefFoundError ignore) { }

		return null;
	}

	public static com.comphenix.protocol.ProtocolManager getProtocolManager() {
		try {
			return com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
		} catch (NoClassDefFoundError ex) { return null; }
	}

	public static boolean hasProtocolManager() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("ProtocolLib") != null;
	}

	@Deprecated
	public static boolean hasPermissions() {
		return hasVault();
	}

	public static boolean hasVault() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("Vault") != null;
	}

	private class LogHandler extends Handler {

		private final transient Plugin plugin;

		public LogHandler() {
			this(null);
		}

		public LogHandler(Plugin plugin) {
			this.plugin = plugin;
		}

		@Override
		public void close() throws SecurityException { }

		@Override
		public void flush() { }

		@Override
		public void publish(LogRecord record) {
			if (record.getThrown() != null) {
				String pluginName = this.plugin != null ? this.plugin.getName() : null;

				if (StringUtil.isEmpty(pluginName)) {
					try {
						String[] parts = record.getLoggerName().split("\\.");
						Plugin plugin = NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin(parts[parts.length - 1]);
						if (plugin != null) pluginName = parts[parts.length - 1];
					} catch (Exception ignore) { }
				}

				PLUGINS.get(StringUtil.isEmpty(pluginName) ? "Bukkit" : pluginName).add(record.getThrown().getMessage());
			}
		}

	}

}
