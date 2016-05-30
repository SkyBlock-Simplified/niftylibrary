package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitListener extends BukkitHelper implements Listener {

	private final static transient ConcurrentMap<String, Integer> PLUGINS = new ConcurrentMap<>();

	public BukkitListener(JavaPlugin plugin) {
		super(plugin);
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());

		if (!PLUGINS.keySet().contains(this.getPluginDescription().getName()))
			PLUGINS.put(this.getPlugin().getName(), 1);
		else
			PLUGINS.put(this.getPlugin().getName(), PLUGINS.get(this.getPlugin().getName()) + 1);
	}

	public static int getPluginCache(String pluginName) {
		return PLUGINS.keySet().contains(pluginName) ? PLUGINS.get(pluginName) : 0;
	}

}