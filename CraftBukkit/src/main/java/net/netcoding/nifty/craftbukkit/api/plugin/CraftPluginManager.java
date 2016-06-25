package net.netcoding.nifty.craftbukkit.api.plugin;

import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.api.plugin.PluginManager;
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
	public MinecraftPlugin getPlugin(String name) {
		return null; // TODO
	}

	@Override
	public <T extends MinecraftPlugin> T getPlugin(Class<?> plugin) {
		return null; // TODO
	}

	@Override
	public boolean hasPlugin(String name) {
		return Bukkit.getPluginManager().getPlugin(name) != null;
	}

	@Override
	public boolean isEnabled(String name) {
		return this.hasPlugin(name) && Bukkit.getPluginManager().getPlugin(name).isEnabled();
	}

}