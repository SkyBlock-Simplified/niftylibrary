package net.netcoding.nifty.common.api;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.IMinecraftPlugin;

public abstract class MinecraftListener extends MinecraftHelper implements Listener {

	public MinecraftListener(IMinecraftPlugin plugin) {
		super(plugin);
		Nifty.getServer().getPluginManager().registerListener(plugin, this);
	}

}