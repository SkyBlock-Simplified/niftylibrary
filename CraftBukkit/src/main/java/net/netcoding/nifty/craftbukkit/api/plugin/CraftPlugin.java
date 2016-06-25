package net.netcoding.nifty.craftbukkit.api.plugin;

import net.netcoding.nifty.common.api.MinecraftLogger;
import net.netcoding.nifty.common.api.plugin.IMinecraftPlugin;
import net.netcoding.nifty.core.api.plugin.PluginDescription;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftPlugin extends JavaPlugin implements IMinecraftPlugin {

	private final MinecraftLogger logger = new MinecraftLogger(this);

	@Override
	public final PluginDescription getPluginDescription() {
		return new PluginDescription(this.getDescription().getName(), this.getFile(), this.getDataFolder());
	}

	@Override
	public final MinecraftLogger getLog() {
		return this.logger;
	}

}