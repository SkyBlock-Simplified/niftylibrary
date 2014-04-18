package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.inventory.items.ItemDatabase;
import net.netcoding.niftybukkit.minecraft.BukkitPlugin;
import net.netcoding.niftybukkit.minecraft.BungeeHelper;
import net.netcoding.niftybukkit.mojang.ProfileRepository;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class NiftyBukkit extends BukkitPlugin {

	private static transient BukkitPlugin plugin;
	private static transient ItemDatabase itemDatabase;
	private static transient ProfileRepository repository;
	private static transient BungeeHelper bungeeHelper;

	@Override
	public void onEnable() {
		plugin = this;
		repository = new ProfileRepository();
		(itemDatabase = new ItemDatabase(this)).reload();

		new NiftyListener(this);
		(bungeeHelper = new BungeeHelper(this)).register();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, BungeeHelper.NIFTY_CHANNEL, bungeeHelper);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, BungeeHelper.NIFTY_CHANNEL);
	}

	@Override
	public void onDisable() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, BungeeHelper.BUNGEE_CHANNEL);
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BungeeHelper.NIFTY_CHANNEL);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, BungeeHelper.NIFTY_CHANNEL);
	}

	public static BungeeHelper getBungeeHelper() {
		return bungeeHelper;
	}

	public static ItemDatabase getItemDatabase() {
		return itemDatabase;
	}

	public static ProfileRepository getMojangRepository() {
		return repository;
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

	public static boolean protocolManagerExists() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("ProtocolLib") != null;
	}

	public static boolean vaultExists() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("Vault") != null;
	}

}
