package net.netcoding.nifty.common._new_.api;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.plugin.MinecraftPlugin;

public abstract class BukkitListener extends BukkitHelper implements Listener {

	public BukkitListener(MinecraftPlugin plugin) {
		super(plugin);
		Nifty.getServer().getPluginManager().registerListener(plugin, this);
	}

}