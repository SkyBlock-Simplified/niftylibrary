package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

final class NiftyListener extends BukkitListener {

	NiftyListener(JavaPlugin plugin) {
		super(plugin);
	}

	/**
	 * Sends an event after it assumes the player
	 * has finished logging in.
	 */
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

	/**
	 * Strips Mac OSX Special Characters
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSignChange(SignChangeEvent event) {
		for (int i = 0; i < event.getLines().length; i++) {
			String newLine = "";

			for (char c : event.getLine(i).toCharArray()) {
				if (c < 0xF700 || c > 0xF747)
					newLine += c;
			}

			event.setLine(i, newLine);
		}
	}

}