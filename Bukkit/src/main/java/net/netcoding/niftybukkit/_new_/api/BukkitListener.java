package net.netcoding.niftybukkit._new_.api;

import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.api.plugin.MinecraftPlugin;

public abstract class BukkitListener extends BukkitHelper implements Listener {

	public BukkitListener(MinecraftPlugin plugin) {
		super(plugin);
		Nifty.getPluginManager().registerListener(plugin, this);
	}

}