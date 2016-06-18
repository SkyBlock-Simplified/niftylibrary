package net.netcoding.niftycraftbukkit.api.plugin;

import net.netcoding.niftybukkit._new_.api.BukkitLogger;
import net.netcoding.niftybukkit._new_.api.plugin.MinecraftPlugin;
import net.netcoding.niftycore.api.plugin.PluginDescription;
import net.netcoding.niftycraftbukkit.api.CraftLogger;
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