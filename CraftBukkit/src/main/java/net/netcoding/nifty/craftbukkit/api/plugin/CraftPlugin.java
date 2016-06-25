package net.netcoding.nifty.craftbukkit.api.plugin;

import net.netcoding.nifty.common._new_.api.BukkitLogger;
import net.netcoding.nifty.common._new_.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.core.api.plugin.PluginDescription;
import net.netcoding.nifty.craftbukkit.api.CraftLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftPlugin extends JavaPlugin implements MinecraftPlugin {

	private CraftLogger logger;
	private PluginDescription description;

	@Override
	public PluginDescription getPluginDescription() {
		if (this.description == null)
			this.description = new PluginDescription(this.getDescription().getName(), this.getFile(), this.getDataFolder());

		return this.description;
	}

	@Override
	public BukkitLogger getLog() {
		if (this.logger == null)
			this.logger = new CraftLogger(this.getLogger());

		return this.logger;
	}

}