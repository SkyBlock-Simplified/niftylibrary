package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.Server;
import net.netcoding.nifty.core.api.plugin.Plugin;
import net.netcoding.nifty.core.api.plugin.PluginDescription;

public abstract class MinecraftPlugin extends MinecraftCore implements Plugin<MinecraftLogger>, Listener {

	public final boolean isEnabled() {
		return Nifty.getPluginManager().isEnabled(this.getDesc().getName());
	}

	@Override
	public final MinecraftLogger getLog() {
		return Nifty.getPluginManager().getPluginLog(this);
	}

	@Override
	public PluginDescription getDesc() {
		return Nifty.getPluginManager().getPluginDescription(this);
	}

	public final Server getServer() {
		return Nifty.getServer();
	}

	public void onEnable() { }

	public void onDisable() { }

	protected final void setEnabled(boolean value) {
		Nifty.getPluginManager().setEnabled(this, value);
	}

}