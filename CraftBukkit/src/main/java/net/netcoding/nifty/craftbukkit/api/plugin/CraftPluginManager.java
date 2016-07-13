package net.netcoding.nifty.craftbukkit.api.plugin;

import net.netcoding.nifty.common.api.plugin.Command;
import net.netcoding.nifty.common.api.plugin.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class CraftPluginManager extends PluginManager {

	private static final CraftPluginManager INSTANCE = new CraftPluginManager();

	private CraftPluginManager() { }

	public static CraftPluginManager getInstance() {
		return INSTANCE;
	}

	public Plugin getBukkitPlugin(String name) {
		return Bukkit.getPluginManager().getPlugin(name);
	}

	public boolean isBukkitPluginEnabled(String name) {
		return this.hasPlugin(name) && Bukkit.getPluginManager().getPlugin(name).isEnabled();
	}

	@Override
	protected void injectCommand(Command command) {

	}

}