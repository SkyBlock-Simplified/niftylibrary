package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.Server;
import net.netcoding.nifty.core.api.plugin.Plugin;
import net.netcoding.nifty.core.api.plugin.PluginDescription;

public abstract class MinecraftPlugin extends MinecraftCore implements Plugin<MinecraftLogger>, Listener {

	public final boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof MinecraftPlugin))
			return false;
		else {
			MinecraftPlugin other = (MinecraftPlugin)obj;
			return other.getName().equalsIgnoreCase(this.getName());
		}
	}

	@Override
	public final MinecraftLogger getLog() {
		return Nifty.getPluginManager().getPluginLog(this);
	}

	@Override
	public final PluginDescription getDesc() {
		return Nifty.getPluginManager().getPluginDescription(this);
	}

	public final Server getServer() {
		return Nifty.getServer();
	}

	public final int hashCode() {
		return 32 * this.getName().hashCode();
	}

	public final boolean isEnabled() {
		return Nifty.getPluginManager().isEnabled(this.getDesc().getName());
	}

	public void onEnable() { }

	public void onDisable() { }

	protected final void setEnabled(boolean value) {
		Nifty.getPluginManager().setEnabled(this, value);
	}

}