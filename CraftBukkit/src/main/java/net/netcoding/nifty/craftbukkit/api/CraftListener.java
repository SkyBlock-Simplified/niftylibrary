package net.netcoding.nifty.craftbukkit.api;

import net.netcoding.nifty.common.api.MinecraftHelper;
import net.netcoding.nifty.craftbukkit.api.plugin.CraftPlugin;
import org.bukkit.event.Listener;

public class CraftListener extends MinecraftHelper implements Listener {

	public CraftListener(CraftPlugin plugin) {
		super(plugin);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}