package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.ItemInteractEvent;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static net.netcoding.niftybukkit.minecraft.inventory.FakeInventory.getClickedItem;

public class FakeItem {

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
		if (this.getItemOpener() != null) {
			if (profile.getOfflinePlayer().isOnline())
				profile.getOfflinePlayer().getPlayer().getInventory().setItem(this.getItemOpenerSlot(), this.getItemOpener());
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
			int slot = this.getItemOpenerSlot();

			if (this.isItemOpener(new ItemData(inventory.getItem(slot))))
				inventory.setItem(slot, null);
		}
	}

	public void setItemOpener(ItemData itemData) {
		this.setItemOpener(-1, itemData);
	}

	public void setItemOpener(int index, ItemData itemData) {
		if (itemData != null && Material.AIR != itemData.getType()) {
			this.itemOpenerSlot = index;
			ItemData itemOpener = itemData.clone();
			itemOpener.getNbt().putPath(NbtKeys.ITEMOPENER_UUID.getPath(), this.getUniqueId().toString());
			itemOpener.getNbt().putPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath(), false);
			this.itemOpener = itemOpener;
		}

		for (BukkitMojangProfile profile : NiftyBukkit.getBungeeHelper().getPlayerList()) {
			this.removeItemOpener(profile);
			this.giveItemOpener(profile);
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
		public void onPlayerPostLogin(PlayerPostLoginEvent event) {
			FakeItem.removeAllItemOpeners(event.getProfile());
		}

	}

}