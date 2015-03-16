package net.netcoding.niftybukkit.minecraft;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitListener extends BukkitHelper implements Listener {

	private final static transient ConcurrentHashMap<String, Integer> PLUGINS = new ConcurrentHashMap<>();

	public BukkitListener(JavaPlugin plugin) {
		super(plugin);
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());

		if (!PLUGINS.keySet().contains(this.getPluginDescription().getName()))
			PLUGINS.put(this.getPlugin().getName(), 1);
		else
			PLUGINS.put(this.getPlugin().getName(), PLUGINS.get(this.getPlugin().getName()) + 1);
	}

	public final static int getPluginCache(String pluginName) {
		return PLUGINS.keySet().contains(pluginName) ? PLUGINS.get(pluginName) : 0;
	}

}