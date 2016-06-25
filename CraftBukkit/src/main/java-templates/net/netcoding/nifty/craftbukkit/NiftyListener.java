package net.netcoding.nifty.craftbukkit;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.inventory.FakeInventory;
import net.netcoding.nifty.common.api.inventory.item.FakeItem;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;
import net.netcoding.nifty.craftbukkit.api.CraftListener;
import net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.api.plugin.CraftPlugin;
import net.netcoding.nifty.craftbukkit.minecraft.event.server.CraftServerPingEvent;
import net.netcoding.nifty.craftbukkit.mojang.CraftMojangRepository;
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
				ItemStack itemData = new CraftItemStack(Material.AIR == event.getCursor().getType() ? event.getCurrentItem() : event.getCursor());

				if (!FakeItem.isAnyItemOpener(itemData) && itemData.getNbt().notEmpty()) {
					/*InventoryCreativeNbtEvent myEvent = new InventoryCreativeNbtEvent(profile, event, itemData);
					Nifty.getServer().getPluginManager().call(myEvent);

					if (myEvent.isCancelled())
						event.setCancelled(true);*/
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

					Nifty.getScheduler().schedule(() -> {
						List<Entity> entities = player.getNearbyEntities(4, 4, 4);

						for (Entity entity : entities) {
							if (EntityType.ENDER_CRYSTAL == entity.getType()) {
								EnderCrystal crystal = (EnderCrystal)entity;
								Block belowCrystal = crystal.getLocation().getBlock().getRelative(BlockFace.DOWN);

								if (block.equals(belowCrystal)) {
									BukkitMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(player);
									//EnderCrystalPlaceEvent myEvent = new EnderCrystalPlaceEvent(profile, crystal);
									//Nifty.getServer().getPluginManager().call(myEvent);

									//if (myEvent.isCancelled())
									//	crystal.remove();

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
	public void onPlayerJoin(final org.bukkit.event.player.PlayerJoinEvent event) {
		Nifty.getScheduler().schedule(this.getPlugin(), () -> {
			BukkitMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(event.getPlayer());
			//Nifty.getServer().getPluginManager().call(new PlayerJoinEvent(profile));
		}, 10L);
	}

	/**
	 * Sends an event after as the player is
	 * logging out.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
		BukkitMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(event.getPlayer());
		//PlayerQuitEvent myEvent = new PlayerQuitEvent(profile, event.getQuitMessage());
		//event.setQuitMessage(myEvent.getQuitMessage());
	}

	/**
	 * Sends an event when NiftyBukkit enables.
	 */
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		//if (event.getPlugin().getName().equals("NiftyBukkit"))
		//	Nifty.getServer().getPluginManager().call(new GameStartingEvent());
	}

	/**
	 * Sends an event when NiftyBukkit disables.
	 */
	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		//if (event.getPlugin().getName().equals("NiftyBukkit"))
		//	Nifty.getServer().getPluginManager().call(new GameStoppingEvent());
	}

	/**
	 * Sends out a custom ping event.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onServerListPing(ServerListPingEvent event) {
		Nifty.getServer().getPluginManager().call(new CraftServerPingEvent(event));
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