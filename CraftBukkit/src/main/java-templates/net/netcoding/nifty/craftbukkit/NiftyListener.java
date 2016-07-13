package net.netcoding.nifty.craftbukkit;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.inventory.FakeInventory;
import net.netcoding.nifty.common.api.inventory.item.FakeItem;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.minecraft.event.entity.EnderCrystalPlaceEvent;
import net.netcoding.nifty.common.minecraft.event.server.GameStartingEvent;
import net.netcoding.nifty.common.minecraft.event.server.GameStoppingEvent;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.craftbukkit.api.CraftListener;
import net.netcoding.nifty.craftbukkit.minecraft.entity.block.CraftEnderCrystal;
import net.netcoding.nifty.craftbukkit.minecraft.event.inventory.CraftInventoryCreativeNbtEvent;
import net.netcoding.nifty.craftbukkit.minecraft.event.player.CraftPlayerJoinEvent;
import net.netcoding.nifty.craftbukkit.minecraft.event.player.CraftPlayerQuitEvent;
import net.netcoding.nifty.craftbukkit.minecraft.event.server.CraftServerPingEvent;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

final class NiftyListener extends CraftListener {

	NiftyListener(JavaPlugin plugin, MinecraftPlugin minePlugin) {
		super(plugin, minePlugin);
	}
/* TODO: REGISTER ALL EVENTS ??
final Listener listener = someListener();
RegisteredListener registeredListener = new RegisteredListener(listener, new EventExecutor() {
    @Override
    public void execute(Listener listener, Event event) throws EventException {
        listener.getClass().getDeclaredMethod("onEvent", Event.class).invoke(listener, event);
    }
}, EventPriority.NORMAL, this, false);
for(HandlerList handler : HandlerList.getHandlerLists())
    handler.register(registeredListener);
*/
	/**
	 * Sends out an InventoryCreativeNbtEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryCreative(final InventoryCreativeEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			MinecraftMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer((Player)event.getWhoClicked());

			if (!FakeInventory.isOpenAnywhere(profile)) {
				ItemStack item = new CraftItemStack(Material.AIR == event.getCursor().getType() ? event.getCurrentItem() : event.getCursor());

				if (!FakeItem.isAnyItemOpener(item) && item.getNbt().notEmpty()) {
					CraftInventoryCreativeNbtEvent myEvent = new CraftInventoryCreativeNbtEvent(event);
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
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
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
									MinecraftMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(player);
									EnderCrystalPlaceEvent myEvent = new EnderCrystalPlaceEvent(profile, new CraftEnderCrystal(crystal));
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
		Nifty.getScheduler().schedule(this.getPlugin(), () -> {
			MinecraftMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(event.getPlayer());
			Nifty.getPluginManager().call(new CraftPlayerJoinEvent(profile, event));
		}, 10L);
	}

	/**
	 * Sends an event after as the player is
	 * logging out.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		MinecraftMojangProfile profile = CraftMojangRepository.getInstance().searchByBukkitPlayer(event.getPlayer());
		CraftPlayerQuitEvent myEvent = new CraftPlayerQuitEvent(profile, event);
		Nifty.getPluginManager().call(myEvent);
	}

	/**
	 * Sends an event when ${name} enables.
	 */
	@EventHandler
	public void onPluginEnable(final PluginEnableEvent event) {
		if (event.getPlugin().getName().equals("${name}"))
			Nifty.getPluginManager().call(new GameStartingEvent());
	}

	/**
	 * Sends an event when ${name} disables.
	 */
	@EventHandler
	public void onPluginDisable(final PluginDisableEvent event) {
		if (event.getPlugin().getName().equals("{$name}"))
			Nifty.getPluginManager().call(new GameStoppingEvent());
	}

	/**
	 * Sends out a custom ping event.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onServerListPing(final ServerListPingEvent event) {
		Nifty.getPluginManager().call(new CraftServerPingEvent(event));
	}

	/**
	 * Strips Mac OSX Special Characters
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSignChange(final SignChangeEvent event) {
		for (int i = 0; i < event.getLines().length; i++) {
			StringBuilder newLine = new StringBuilder();

			for (char c : event.getLine(i).toCharArray()) {
				if (c < 0xF700 || c > 0xF747)
					newLine.append(c);
			}

			event.setLine(i, newLine.toString());
		}
	}

}