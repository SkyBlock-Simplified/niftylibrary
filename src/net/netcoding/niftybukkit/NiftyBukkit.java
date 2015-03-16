package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.inventory.items.ItemDatabase;
import net.netcoding.niftybukkit.minecraft.BukkitPlugin;
import net.netcoding.niftybukkit.minecraft.BungeeHelper;
import net.netcoding.niftybukkit.mojang.MojangRepository;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class NiftyBukkit extends BukkitPlugin {

	//private static transient BukkitPlugin plugin;
	private static transient ItemDatabase itemDatabase;
	private static transient MojangRepository repository;
	private static transient BungeeHelper bungeeHelper;

	@Override
	public void onEnable() {
		//plugin = this;

		this.getLog().console("Registering Commands");
		new NiftyCommand(this);

		this.getLog().console("Registering Listeners");
		new NiftyListener(this);

		this.getLog().console("Registering Helpers");
		repository = new MojangRepository();
		(itemDatabase = new ItemDatabase(this)).reload();
		bungeeHelper = new BungeeHelper(this, BungeeHelper.NIFTY_CHANNEL, true);
	}

	@Override
	public void onDisable() {
		bungeeHelper.unregister();
	}

	public static BungeeHelper getBungeeHelper() {
		return bungeeHelper;
	}

	public static ItemDatabase getItemDatabase() {
		return itemDatabase;
	}

	public static MojangRepository getMojangRepository() {
		return repository;
	}

	public static BukkitPlugin getPlugin() {
		return getPlugin(NiftyBukkit.class);
	}

	public static net.milkbowl.vault.permission.Permission getPermissions() {
		try {
			RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			return (permissionProvider != null ? permissionProvider.getProvider() : null);
		} catch (NoClassDefFoundError ex) { }
	
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

	public static boolean hasPermissions() {
		return NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin("Vault") != null;
	}

}
