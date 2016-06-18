package net.netcoding.niftycraftbukkit.api;

import net.netcoding.niftybukkit._new_.api.BukkitHelper;
import net.netcoding.niftycraftbukkit.api.plugin.CraftPlugin;
import org.bukkit.event.Listener;

public class CraftListener extends BukkitHelper implements Listener {

	public CraftListener(CraftPlugin plugin) {
		super(plugin);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}