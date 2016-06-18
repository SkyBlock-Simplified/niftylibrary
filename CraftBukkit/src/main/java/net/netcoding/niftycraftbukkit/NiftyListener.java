package net.netcoding.niftycraftbukkit;

import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.api.inventory.FakeInventory;
import net.netcoding.niftybukkit._new_.api.inventory.item.FakeItem;
import net.netcoding.niftybukkit._new_.minecraft.event.entity.EnderCrystalPlaceEvent;
import net.netcoding.niftybukkit._new_.minecraft.event.inventory.InventoryCreativeNbtEvent;
import net.netcoding.niftybukkit._new_.minecraft.event.profile.ProfileJoinEvent;
import net.netcoding.niftybukkit._new_.minecraft.event.profile.ProfileQuitEvent;
import net.netcoding.niftybukkit._new_.minecraft.event.server.GameStartingEvent;
import net.netcoding.niftybukkit._new_.minecraft.event.server.GameStoppingEvent;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftycore.api.scheduler.MinecraftScheduler;
import net.netcoding.niftycraftbukkit.api.CraftListener;
import net.netcoding.niftycraftbukkit.api.plugin.CraftPlugin;
import net.netcoding.niftycraftbukkit.minecraft.event.server.CraftServerPingEvent;
import net.netcoding.niftycraftbukkit.mojang.CraftMojangRepository;
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
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

final class NiftyListener extends CraftListener {

	NiftyListener(CraftPlugin plugin) {
		super(plugin);
	}

	/**
	 * Sends out an InventoryCreativeNbtEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryCreative(InventoryCreativeEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			BukkitMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer((Player)event.getWhoClicked());

			if (!FakeInventory.isOpenAnywhere(profile)) {
				ItemData itemData = new ItemData(Material.AIR == event.getCursor().getType() ? event.getCurrentItem() : event.getCursor());

				if (!FakeItem.isAnyItemOpener(itemData) && itemData.getNbt().notEmpty()) {
					InventoryCreativeNbtEvent myEvent = new InventoryCreativeNbtEvent(profile, event, itemData);
					Nifty.getPluginManager().call(myEvent);

					if (myEvent.isCancelled())
						event.setCancelled(true);
				}
			}
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

					MinecraftScheduler.getInstance().schedule(() -> {
						List<Entity> entities = player.getNearbyEntities(4, 4, 4);

						for (Entity entity : entities) {
							if (EntityType.ENDER_CRYSTAL == entity.getType()) {
								EnderCrystal crystal = (EnderCrystal)entity;
								Block belowCrystal = crystal.getLocation().getBlock().getRelative(BlockFace.DOWN);

								if (block.equals(belowCrystal)) {
									BukkitMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(player);
									EnderCrystalPlaceEvent myEvent = new EnderCrystalPlaceEvent(profile, crystal);
									Nifty.getPluginManager().call(myEvent);

									if (myEvent.isCancelled())
										crystal.remove();

									break;
								}
							}
						}
					});
				}
			}
		}
	}

	/**
	 * Sends an event after it assumes the player
	 * has finished logging in.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		MinecraftScheduler.getInstance().schedule(this.getPlugin(), () -> {
			BukkitMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(event.getPlayer());
			Nifty.getPluginManager().call(new ProfileJoinEvent(profile));
		}, 10L);
	}

	/**
	 * Sends an event after as the player is
	 * logging out.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		BukkitMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(event.getPlayer());
		ProfileQuitEvent myEvent = new ProfileQuitEvent(profile, event.getQuitMessage());
		event.setQuitMessage(myEvent.getQuitMessage());
	}

	/**
	 * Sends an event when NiftyBukkit enables.
	 */
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().getName().equals("NiftyBukkit"))
			Nifty.getPluginManager().call(new GameStartingEvent());
	}

	/**
	 * Sends an event when NiftyBukkit disables.
	 */
	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().getName().equals("NiftyBukkit"))
			Nifty.getPluginManager().call(new GameStoppingEvent());
	}

	/**
	 * Sends out a custom ping event.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onServerListPing(ServerListPingEvent event) {
		Nifty.getPluginManager().call(new CraftServerPingEvent(event));
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