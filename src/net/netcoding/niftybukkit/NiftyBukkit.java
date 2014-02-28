package net.netcoding.niftybukkit;

import java.lang.reflect.Field;

import net.netcoding.niftybukkit.items.ItemDatabase;
import net.netcoding.niftybukkit.minecraft.BungeeHelper;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("rawtypes")
public class NiftyBukkit extends JavaPlugin implements Listener {

	private static transient JavaPlugin plugin;
	private static transient ItemDatabase itemDatabase;
	private static final transient String bukkitPath;
	private static final transient String minecraftPath;

	static {
		Class craftServer = Bukkit.getServer().getClass();
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
		plugin = this;
		itemDatabase = new ItemDatabase(this);
		new BungeeHelper(this).register();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
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

	public static JavaPlugin getPlugin() {
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
		if (protocolManagerExists())
			return com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
		else
			return null;
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().getPluginManager().callEvent(new PlayerPostLoginEvent(event.getPlayer()));
			}
		}, 10L);
	}

	public static boolean protocolManagerExists() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("ProtocolLib") != null;
	}

}
