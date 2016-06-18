package net.netcoding.nifty.craftbukkit.api.plugin;

import net.netcoding.niftybukkit._new_.api.plugin.PluginManager;
import org.bukkit.Bukkit;

public final class CraftPluginManager extends PluginManager {

	private static CraftPluginManager INSTANCE;

	private CraftPluginManager() { }

	public static CraftPluginManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CraftPluginManager();

		return INSTANCE;
	}

	@Override
	public boolean hasPlugin(String name) {
		return Bukkit.getPluginManager().getPlugin(name) != null;
	}

	@Override
	public boolean isPluginEnabled(String name) {
		return this.hasPlugin(name) && Bukkit.getPluginManager().getPlugin(name).isEnabled();
	}

}