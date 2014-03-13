package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.ProfileRepository;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

class NiftyListener extends BukkitListener {

	NiftyListener(JavaPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		this.getPlugin().getServer().getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().getPluginManager().callEvent(new PlayerPostLoginEvent(event.getPlayer()));
			}
		}, 10L);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerLogin(final AsyncPlayerPreLoginEvent event) {
		ProfileRepository.searchByExactUsername(event.getName());
	}

}