package net.netcoding.nifty.common.api.inventory.item;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.inventory.FakeInventory;
import net.netcoding.nifty.common.api.inventory.NbtKeys;
import net.netcoding.nifty.common.api.inventory.events.FakeItemInteractEvent;
import net.netcoding.nifty.common.api.plugin.Event;
import net.netcoding.nifty.common.api.plugin.MinecraftListener;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.minecraft.block.Action;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.event.EventResult;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryClickEvent;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryCreativeEvent;
import net.netcoding.nifty.common.minecraft.event.player.*;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.type.PlayerInventory;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;

import java.util.UUID;

public class FakeItem {

	static {
		new ItemOpenerListener(Nifty.getPlugin());
	}

	private final UUID uniqueId = UUID.randomUUID();
	private final transient FakeItemListener listener;
	private ItemStack itemOpener;
	private int itemOpenerSlot = -1;

	public FakeItem(MinecraftPlugin plugin, FakeItemListener listener) {
		this.listener = listener;
		new FakeMinecraftListener(plugin);
	}

	public final ItemStack getItemOpener() {
		return this.itemOpener;
	}

	public final int getItemOpenerSlot() {
		return this.itemOpenerSlot > 0 ? this.itemOpenerSlot : 0;
	}

	public final UUID getUniqueId() {
		return this.uniqueId;
	}

	public final void giveItemOpener(MinecraftMojangProfile profile) {
		this.giveItemOpener(profile, this.getItemOpenerSlot(), this.getItemOpener());
	}

	private void giveItemOpener(MinecraftMojangProfile profile, int index, ItemStack itemOpener) {
		if (this.getItemOpener() != null) {
			if (profile.getOfflinePlayer().isOnline())
				profile.getOfflinePlayer().getPlayer().getInventory().setItem(index, itemOpener);
		}
	}

	public static boolean isAnyItemOpener(ItemStack ItemStack) {
		return ItemStack.getNbt().containsPath(NbtKeys.ITEMOPENER_UUID.getPath());
	}

	public final boolean isItemOpener(ItemStack ItemStack) {
		return isAnyItemOpener(ItemStack) && ItemStack.getNbt().getPath(NbtKeys.ITEMOPENER_UUID.getPath()).equals(this.getUniqueId().toString());
	}

	public final boolean isItemOpenerDestructable() {
		return this.itemOpener != null && this.itemOpener.getNbt().<Boolean>getPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath());
	}

	public static void removeAllItemOpeners(MinecraftMojangProfile profile) {
		if (profile.isOnlineLocally()) {
			PlayerInventory inventory = profile.getOfflinePlayer().getPlayer().getInventory();
			ItemStack[] contents = inventory.getContents();

			for (int i = 0; i < 9; i++) {
				if (contents[i] == null || Material.AIR == contents[i].getType())
					continue;

				if (isAnyItemOpener(contents[i]))
					inventory.setItem(i, null);
			}
		}
	}

	public final void removeItemOpener(MinecraftMojangProfile profile) {
		if (profile.isOnlineLocally()) {
			PlayerInventory inventory = profile.getOfflinePlayer().getPlayer().getInventory();
			ItemStack[] contents = inventory.getContents();

			for (int i = 0; i < 9; i++) {
				if (contents[i] == null || Material.AIR == contents[i].getType())
					continue;

				if (this.isItemOpener(contents[i]))
					inventory.setItem(i, null);
			}
		}
	}

	public void setItemOpener(ItemStack ItemStack) {
		this.setItemOpener(ItemStack, null);
	}

	public void setItemOpener(ItemStack ItemStack, MinecraftMojangProfile profile) {
		this.setItemOpener(-1, ItemStack, profile);
	}

	public void setItemOpener(int index, ItemStack ItemStack) {
		this.setItemOpener(index, ItemStack, null);
	}

	public void setItemOpener(int index, ItemStack ItemStack, MinecraftMojangProfile specific) {
		ItemStack itemOpener = null;

		if (ItemStack != null && Material.AIR != ItemStack.getType()) {
			itemOpener = ItemStack.clone();
			itemOpener.getNbt().putPath(NbtKeys.ITEMOPENER_UUID.getPath(), this.getUniqueId().toString());
			itemOpener.getNbt().putPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath(), false);
		}

		if (specific != null) {
			this.removeItemOpener(specific);
			this.giveItemOpener(specific, index, itemOpener);
		} else {
			this.itemOpenerSlot = index;
			this.itemOpener = itemOpener;

			for (MinecraftMojangProfile profile : Nifty.getBungeeHelper().getPlayerList()) {
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

	private class FakeMinecraftListener extends MinecraftListener {

		public FakeMinecraftListener(MinecraftPlugin plugin) {
			super(plugin);

			if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_pre1.getProtocol())
				new FakeMinecraftListener1_9(plugin);
		}

		private class FakeMinecraftListener1_9 extends MinecraftListener {

			public FakeMinecraftListener1_9(MinecraftPlugin plugin) {
				super(plugin);
			}

			@Event
			public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
				if (FakeItem.this.getItemOpener() != null) {
					ItemStack mainHandItem = event.getMainHandItem();
					ItemStack offHandItem = event.getOffHandItem();

					if (FakeItem.this.isItemOpener(mainHandItem) || FakeItem.this.isItemOpener(offHandItem))
						event.setCancelled(true);
				}
			}

		}

		@Event(priority = Event.Priority.LOWEST, ignoreCancelled = true)
		public void onInventoryClick(final InventoryClickEvent event) {
			if (event.getWhoClicked() instanceof Player) {
				if (InventoryType.SlotType.OUTSIDE != event.getSlotType()) {
					if (FakeItem.this.getItemOpener() != null) {
						MinecraftMojangProfile profile = Nifty.getMojangRepository().searchByPlayer((Player)event.getWhoClicked());

						if (!FakeInventory.isOpenAnywhere(profile)) {
							ItemStack firstClickItem = FakeInventory.getClickedItem(event);
							ItemStack placeClickItem = FakeInventory.getClickedItem(event, false);
							FakeItemInteractEvent myEvent = null;

							if (FakeItem.this.isItemOpener(firstClickItem))
								myEvent = new FakeItemInteractEvent(profile, firstClickItem);
							else if (FakeItem.this.isItemOpener(placeClickItem))
								myEvent = new FakeItemInteractEvent(profile, placeClickItem);

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

		@Event(priority = Event.Priority.LOWEST)
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (Action.PHYSICAL != event.getAction()) {
				if (FakeItem.this.getItemOpener() != null) {
					ItemStack handItem = event.getItem();

					if (FakeItem.this.isItemOpener(handItem)) {
						FakeItemInteractEvent myEvent = new FakeItemInteractEvent(event.getProfile(), handItem);
						FakeItem.this.listener.onItemInteract(myEvent);
						event.setCancelled(true);
					}
				}
			}
		}

		@Event
		public void onProfileJoin(PlayerJoinEvent event) {
			FakeItem.this.giveItemOpener(event.getProfile());
		}

	}

	private static class ItemOpenerListener extends MinecraftListener {

		public ItemOpenerListener(MinecraftPlugin plugin) {
			super(plugin);
		}

		@Event
		public void onInventoryCreative(InventoryCreativeEvent event) {
			ItemStack ItemStack = (Material.AIR == event.getCursor().getType() ? event.getCurrentItem() : event.getCursor());

			if (isAnyItemOpener(ItemStack)) {
				event.setCancelled(true);
				event.setResult(EventResult.DENY);
			}
		}

		@Event
		public void onPlayerDeath(PlayerDeathEvent event) {
			event.getDrops().stream().filter(FakeItem::isAnyItemOpener).forEach(itemStack -> itemStack.setAmount(0));
		}

		@Event
		public void onPlayerDropItem(PlayerDropItemEvent event) {
			ItemStack ItemStack = event.getItem().getItemStack();

			if (isAnyItemOpener(ItemStack)) {
				if (ItemStack.getNbt().<Boolean>getPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath())) {
					event.getItem().remove();
					ItemStack.setAmount(0);
				} else
					event.setCancelled(true);
			}
		}

		@Event(priority = Event.Priority.LOWEST)
		public void onProfileJoin(PlayerJoinEvent event) {
			FakeItem.removeAllItemOpeners(event.getProfile());
		}

	}

}