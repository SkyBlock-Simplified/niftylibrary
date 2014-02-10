package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.minecraft.BungeeHelper;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class NiftyBukkit extends JavaPlugin implements Listener {

	private static transient JavaPlugin plugin;

	@Override
	public void onEnable() {
		plugin = this;// test
		new BungeeHelper().register();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
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
		try {
			return com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
		} catch (Exception ex) { }

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

}