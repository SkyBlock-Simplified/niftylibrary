package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.core.api.plugin.PluginDescription;

final class PluginDetails {

	private final MinecraftLogger log;
	private final PluginDescription description;
	private boolean enabled = true;

	PluginDetails(MinecraftPlugin plugin, PluginDescription description) {
		this.log = new MinecraftLogger(plugin);
		this.description = description;
	}

	public PluginDescription getDescription() {
		return this.description;
	}

	public MinecraftLogger getLog() {
		return this.log;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	void setEnabled(boolean value) {
		this.enabled = value;
	}

}