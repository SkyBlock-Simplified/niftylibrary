package net.netcoding.niftybukkit.minecraft;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitListener extends BukkitHelper implements Listener {

	public BukkitListener(JavaPlugin plugin) {
		super(plugin);
		super.getPlugin().getServer().getPluginManager().registerEvents(this, super.getPlugin());
	}

}