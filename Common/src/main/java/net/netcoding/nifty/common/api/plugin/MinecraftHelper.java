package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.core.api.plugin.PluginDescription;

public abstract class MinecraftHelper extends MinecraftCore {

	private final transient MinecraftPlugin plugin;

	protected MinecraftHelper(MinecraftPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public final MinecraftLogger getLog() {
		return this.getPlugin().getLog();
	}

	public final MinecraftPlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public final PluginDescription getDesc() {
		return this.getPlugin().getDesc();
	}

}