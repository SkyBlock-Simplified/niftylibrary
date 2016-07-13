package net.netcoding.nifty.craftbukkit.api;

import net.netcoding.nifty.common.api.plugin.MinecraftHelper;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CraftListener extends MinecraftHelper implements Listener {

	public CraftListener(JavaPlugin plugin, MinecraftPlugin minePlugin) {
		super(minePlugin);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}