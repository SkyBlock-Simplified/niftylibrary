package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.EnderCrystalPlaceEvent;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.minecraft.scheduler.MinecraftScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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

	/**
	 * Sends out an EnderCrystalPlaceEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (Action.RIGHT_CLICK_BLOCK == event.getAction()) {
			final Block block = event.getClickedBlock();

			if (Material.OBSIDIAN == block.getType()) {
				if (Material.END_CRYSTAL == event.getMaterial()) {
					final Player player = event.getPlayer();

					MinecraftScheduler.schedule(new Runnable() {
						@Override
						public void run() {
							List<Entity> entities = player.getNearbyEntities(4, 4, 4);

							for (Entity entity : entities) {
								if (EntityType.ENDER_CRYSTAL == entity.getType()) {
									EnderCrystal crystal = (EnderCrystal)entity;
									Block belowCrystal = crystal.getLocation().getBlock().getRelative(BlockFace.DOWN);

									if (block.equals(belowCrystal)) {
										BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(player);
										EnderCrystalPlaceEvent event = new EnderCrystalPlaceEvent(profile, crystal);
										Bukkit.getPluginManager().callEvent(event);
										if (event.isCancelled()) crystal.remove();
										break;
									}
								}
							}
						}
					});
				}
			}
		}
	}

}