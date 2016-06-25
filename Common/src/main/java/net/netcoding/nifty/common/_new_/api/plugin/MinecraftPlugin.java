package net.netcoding.nifty.common._new_.api.plugin;

import net.netcoding.nifty.common._new_.api.BukkitLogger;
import net.netcoding.nifty.core.api.plugin.Plugin;

public interface MinecraftPlugin extends Plugin<BukkitLogger> {

	static <T> T getPlugin(Class<T> plugin) {
		return null; // TODO
	}

	void onEnable();

	default void onDisable() { }

}