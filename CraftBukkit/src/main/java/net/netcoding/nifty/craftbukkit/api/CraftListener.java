package net.netcoding.nifty.craftbukkit.api;

import net.netcoding.nifty.common._new_.api.BukkitHelper;
import net.netcoding.nifty.craftbukkit.api.plugin.CraftPlugin;
import org.bukkit.event.Listener;

public class CraftListener extends BukkitHelper implements Listener {

	public CraftListener(CraftPlugin plugin) {
		super(plugin);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}