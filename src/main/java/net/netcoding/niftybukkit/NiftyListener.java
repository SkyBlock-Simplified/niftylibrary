package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

final class NiftyListener extends BukkitListener {

	NiftyListener(JavaPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		this.getPlugin().getServer().getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
			@Override
			public void run() {
				BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());
				getPlugin().getServer().getPluginManager().callEvent(new PlayerPostLoginEvent(profile));
			}
		}, 10L);
	}

}