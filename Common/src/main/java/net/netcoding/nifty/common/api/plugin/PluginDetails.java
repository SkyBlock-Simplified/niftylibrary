package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.core.api.plugin.PluginDescription;

final class PluginDetails {

	private final MinecraftPlugin plugin;
	private final PluginDescription description;
	private final MinecraftLogger log;
	private boolean enabled = true;

	PluginDetails(MinecraftPlugin plugin, PluginDescription description) {
		this.plugin = plugin;
		this.description = description;
		this.log = new MinecraftLogger(description);
	}

	public PluginDescription getDescription() {
		return this.description;
	}

	public MinecraftLogger getLog() {
		return this.log;
	}

	public MinecraftPlugin getPlugin() {
		return this.plugin;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	void setEnabled(boolean value) {
		this.enabled = value;
	}

}