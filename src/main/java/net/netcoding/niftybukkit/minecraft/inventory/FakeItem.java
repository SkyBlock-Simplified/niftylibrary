package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.profile.ProfileJoinEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.ItemInteractEvent;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.reflection.MinecraftProtocol;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static net.netcoding.niftybukkit.minecraft.inventory.FakeInventory.getClickedItem;

public class FakeItem {

	static {
		new ItemOpenerListener(NiftyBukkit.getPlugin());
	}

	private final UUID uniqueId = UUID.randomUUID();
	private final transient FakeItemListener listener;
	private ItemData itemOpener;
	private int itemOpenerSlot = -1;

	public FakeItem(JavaPlugin plugin, FakeItemListener listener) {
		this.listener = listener;
		new FakeBukkitListener(plugin);
	}

	public final ItemData getItemOpener() {
		return this.itemOpener;
	}

	public final int getItemOpenerSlot() {
		return this.itemOpenerSlot > 0 ? this.itemOpenerSlot : 0;
	}

	public final UUID getUniqueId() {
		return this.uniqueId;
	}

	public final void giveItemOpener(BukkitMojangProfile profile) {
		this.giveItemOpener(profile, this.getItemOpenerSlot(), this.getItemOpener());
	}

	private void giveItemOpener(BukkitMojangProfile profile, int index, ItemData itemOpener) {
		if (this.getItemOpener() != null) {
			if (profile.getOfflinePlayer().isOnline())
				profile.getOfflinePlayer().getPlayer().getInventory().setItem(index, itemOpener);
		}
	}

	public static boolean isAnyItemOpener(ItemData itemData) {
		return itemData.getNbt().containsPath(NbtKeys.ITEMOPENER_UUID.getPath());
	}

	public final boolean isItemOpener(ItemData itemData) {
		return isAnyItemOpener(itemData) && itemData.getNbt().getPath(NbtKeys.ITEMOPENER_UUID.getPath()).equals(this.getUniqueId().toString());
	}

	public final boolean isItemOpenerDestructable() {
		return this.itemOpener != null && this.itemOpener.getNbt().<Boolean>getPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath());
	}

	public static void removeAllItemOpeners(BukkitMojangProfile profile) {
		if (profile.isOnlineLocally()) {
			PlayerInventory inventory = profile.getOfflinePlayer().getPlayer().getInventory();
			ItemStack[] contents = inventory.getContents();

			for (int i = 0; i < 9; i++) {
				if (contents[i] == null || Material.AIR == contents[i].getType())
					continue;

				if (isAnyItemOpener(new ItemData(contents[i])))
					inventory.setItem(i, null);
			}
		}
	}

	public final void removeItemOpener(BukkitMojangProfile profile) {
		if (profile.isOnlineLocally()) {
			PlayerInventory inventory = profile.getOfflinePlayer().getPlayer().getInventory();
			ItemStack[] contents = inventory.getContents();

			for (int i = 0; i < 9; i++) {
				if (contents[i] == null || Material.AIR == contents[i].getType())
					continue;

				if (this.isItemOpener(new ItemData(contents[i])))
					inventory.setItem(i, null);
			}
		}
	}

	public void setItemOpener(ItemData itemData) {
		this.setItemOpener(itemData, null);
	}

	public void setItemOpener(ItemData itemData, BukkitMojangProfile profile) {
		this.setItemOpener(-1, itemData, profile);
	}

	public void setItemOpener(int index, ItemData itemData) {
		this.setItemOpener(index, itemData, null);
	}

	public void setItemOpener(int index, ItemData itemData, BukkitMojangProfile specific) {
		ItemData itemOpener = null;

		if (itemData != null && Material.AIR != itemData.getType()) {
			itemOpener = itemData.clone();
			itemOpener.getNbt().putPath(NbtKeys.ITEMOPENER_UUID.getPath(), this.getUniqueId().toString());
			itemOpener.getNbt().putPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath(), false);
		}

		if (specific != null) {
			this.removeItemOpener(specific);
			this.giveItemOpener(specific, index, itemOpener);
		} else {
			this.itemOpenerSlot = index;
			this.itemOpener = itemOpener;

			for (BukkitMojangProfile profile : NiftyBukkit.getBungeeHelper().getPlayerList()) {
				this.removeItemOpener(profile);
				this.giveItemOpener(profile);
			}
		}
	}

	public void setItemOpenerDestructable() {
		this.setItemOpenerDestructable(true);
	}

	public void setItemOpenerDestructable(boolean value) {
		if (this.itemOpener != null)
			this.itemOpener.getNbt().putPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath(), value);
	}

	private class FakeBukkitListener extends BukkitListener {

		public FakeBukkitListener(JavaPlugin plugin) {
			super(plugin);

			if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_pre1.getProtocol())
				new FakeBukkitListener1_9(plugin);
		}

		private class FakeBukkitListener1_9 extends BukkitListener {

			public FakeBukkitListener1_9(JavaPlugin plugin) {
				super(plugin);
			}

			@EventHandler
			public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
				if (FakeItem.this.getItemOpener() != null) {
					ItemData mainHandItem = new ItemData(event.getMainHandItem());
					ItemData offHandItem = new ItemData(event.getOffHandItem());

					if (FakeItem.this.isItemOpener(mainHandItem) || FakeItem.this.isItemOpener(offHandItem))
						event.setCancelled(true);
				}
			}

		}

		@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
		public void onInventoryClick(final org.bukkit.event.inventory.InventoryClickEvent event) {
			if (event.getWhoClicked() instanceof Player) {
				if (InventoryType.SlotType.OUTSIDE != event.getSlotType()) {
					if (FakeItem.this.getItemOpener() != null) {
						BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getWhoClicked());

						if (!FakeInventory.isOpenAnywhere(profile)) {
							ItemData firstClickItem = getClickedItem(event);
							ItemData placeClickItem = getClickedItem(event, false);
							ItemInteractEvent myEvent = null;

							if (FakeItem.this.isItemOpener(firstClickItem))
								myEvent = new ItemInteractEvent(profile, firstClickItem);
							else if (FakeItem.this.isItemOpener(placeClickItem))
								myEvent = new ItemInteractEvent(profile, placeClickItem);

							if (myEvent != null) {
								event.setCancelled(true);
								InventoryType type = profile.getOfflinePlayer().getPlayer().getOpenInventory().getType();

								if (type == InventoryType.CREATIVE || type == InventoryType.CRAFTING)
									FakeItem.this.listener.onItemInteract(myEvent);
							}
						}
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onPlayerInteract(PlayerInteractEvent event) {
			final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());

			if (Action.PHYSICAL != event.getAction()) {
				if (FakeItem.this.getItemOpener() != null) {
					ItemData handItem = new ItemData(event.getItem());

					if (FakeItem.this.isItemOpener(handItem)) {
						ItemInteractEvent myEvent = new ItemInteractEvent(profile, handItem);
						FakeItem.this.listener.onItemInteract(myEvent);
						event.setCancelled(true);
					}
				}
			}
		}

		@EventHandler
		public void onProfileJoin(ProfileJoinEvent event) {
			FakeItem.this.giveItemOpener(event.getProfile());
		}

	}

	private static class ItemOpenerListener extends BukkitListener {

		public ItemOpenerListener(JavaPlugin plugin) {
			super(plugin);
		}

		@EventHandler
		public void onInventoryCreative(InventoryCreativeEvent event) {
			ItemData itemData = new ItemData(Material.AIR == event.getCursor().getType() ? event.getCurrentItem() : event.getCursor());

			if (isAnyItemOpener(itemData)) {
				event.setCancelled(true);
				event.setResult(Event.Result.DENY);
			}
		}

		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent event) {
			for (ItemStack itemStack : event.getDrops()) {
				ItemData itemData = new ItemData(itemStack);

				if (isAnyItemOpener(itemData))
					itemStack.setAmount(0);
			}
		}

		@EventHandler
		public void onPlayerDropItem(PlayerDropItemEvent event) {
			ItemData itemData = new ItemData(event.getItemDrop().getItemStack());

			if (isAnyItemOpener(itemData)) {
				if (itemData.getNbt().<Boolean>getPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath())) {
					event.getItemDrop().remove();
					itemData.setAmount(0);
				} else
					event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onProfileJoin(ProfileJoinEvent event) {
			FakeItem.removeAllItemOpeners(event.getProfile());
		}

	}

}