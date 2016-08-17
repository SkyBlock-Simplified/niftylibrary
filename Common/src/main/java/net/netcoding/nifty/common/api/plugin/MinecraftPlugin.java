package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.Server;
import net.netcoding.nifty.core.api.color.ChatColor;
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
			return other.getClass().equals(this.getClass());
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
		return 32 * this.getClass().hashCode();
	}

	public final boolean isEnabled() {
		return Nifty.getPluginManager().isEnabled(this.getDesc().getName());
	}

	protected void onEnable() { }

	protected void onDisable() { }

	protected final void setEnabled(boolean value) {
		Nifty.getPluginManager().setEnabled(this, value);
	}

	public final void setLogColors(ChatColor color, ChatColor important) {
		this.setLogColors(color, important, this.getLog().getBracket());
	}

	public final void setLogColors(ChatColor color, ChatColor important, ChatColor bracket) {
		this.getLog().setBracket(bracket);
		this.getLog().setColor(color);
		this.getLog().setImportant(important);
	}

}