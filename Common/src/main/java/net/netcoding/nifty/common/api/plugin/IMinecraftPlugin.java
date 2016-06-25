package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.MinecraftLogger;
import net.netcoding.nifty.core.api.plugin.Plugin;

public interface IMinecraftPlugin extends Plugin<MinecraftLogger> {

	static <T> T getPlugin(Class<T> plugin) {
		return plugin.cast(Nifty.getServer().getPluginManager().getPlugin(plugin));
	}

	void onEnable();

	default void onDisable() { }

}