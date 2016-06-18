package net.netcoding.nifty.common._new_.api.inventory.item;

import net.netcoding.nifty.common._new_.api.Event;
import net.netcoding.nifty.common._new_.api.inventory.FakeInventory;
import net.netcoding.nifty.common._new_.api.inventory.events.FakeItemInteractEvent;
import net.netcoding.nifty.common._new_.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.event.inventory.InventoryClickEvent;
import net.netcoding.nifty.common._new_.minecraft.event.profile.ProfileJoinEvent;
import net.netcoding.nifty.common._new_.minecraft.inventory.PlayerInventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.minecraft.material.Material;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.reflection.MinecraftProtocol;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.BukkitListener;
import net.netcoding.nifty.common._new_.api.inventory.NbtKeys;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.UUID;

import static net.netcoding.nifty.common._new_.api.inventory.FakeInventory.getClickedItem;

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
		new FakeBukkitListener(plugin);
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

	public final void giveItemOpener(BukkitMojangProfile profile) {
		this.giveItemOpener(profile, this.getItemOpenerSlot(), this.getItemOpener());
	}

	private void giveItemOpener(BukkitMojangProfile profile, int index, ItemStack itemOpener) {
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

	public static void removeAllItemOpeners(BukkitMojangProfile profile) {
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

	public final void removeItemOpener(BukkitMojangProfile profile) {
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

	public void setItemOpener(ItemStack ItemStack, BukkitMojangProfile profile) {
		this.setItemOpener(-1, ItemStack, profile);
	}

	public void setItemOpener(int index, ItemStack ItemStack) {
		this.setItemOpener(index, ItemStack, null);
	}

	public void setItemOpener(int index, ItemStack ItemStack, BukkitMojangProfile specific) {
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

			for (BukkitMojangProfile profile : Nifty.getBungeeHelper().getPlayerList()) {
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

		public FakeBukkitListener(MinecraftPlugin plugin) {
			super(plugin);

			if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_9_pre1.getProtocol())
				new FakeBukkitListener1_9(plugin);
		}

		private class FakeBukkitListener1_9 extends BukkitListener {

			public FakeBukkitListener1_9(MinecraftPlugin plugin) {
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
						BukkitMojangProfile profile = Nifty.getMojangRepository().searchByPlayer((Player)event.getWhoClicked());

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
			final BukkitMojangProfile profile = Nifty.getMojangRepository().searchByPlayer(event.getPlayer());

			if (Action.PHYSICAL != event.getAction()) {
				if (FakeItem.this.getItemOpener() != null) {
					ItemStack handItem = new ItemStack(event.getItem());

					if (FakeItem.this.isItemOpener(handItem)) {
						FakeItemInteractEvent myEvent = new FakeItemInteractEvent(profile, handItem);
						FakeItem.this.listener.onItemInteract(myEvent);
						event.setCancelled(true);
					}
				}
			}
		}

		@Event
		public void onProfileJoin(ProfileJoinEvent event) {
			FakeItem.this.giveItemOpener(event.getProfile());
		}

	}

	private static class ItemOpenerListener extends BukkitListener {

		public ItemOpenerListener(MinecraftPlugin plugin) {
			super(plugin);
		}

		@Event
		public void onInventoryCreative(InventoryCreativeEvent event) {
			ItemStack ItemStack = new ItemStack(Material.AIR == event.getCursor().getType() ? event.getCurrentItem() : event.getCursor());

			if (isAnyItemOpener(ItemStack)) {
				event.setCancelled(true);
				event.setResult(net.netcoding.nifty.common._new_.minecraft.event.Event.Result.DENY);
			}
		}

		@Event
		public void onPlayerDeath(PlayerDeathEvent event) {
			for (ItemStack itemStack : event.getDrops()) {
				ItemStack ItemStack = new ItemStack(itemStack);

				if (isAnyItemOpener(ItemStack))
					itemStack.setAmount(0);
			}
		}

		@Event
		public void onPlayerDropItem(PlayerDropItemEvent event) {
			ItemStack ItemStack = new ItemStack(event.getItemDrop().getItemStack());

			if (isAnyItemOpener(ItemStack)) {
				if (ItemStack.getNbt().<Boolean>getPath(NbtKeys.ITEMOPENER_DESTRUCTABLE.getPath())) {
					event.getItemDrop().remove();
					ItemStack.setAmount(0);
				} else
					event.setCancelled(true);
			}
		}

		@Event(priority = Event.Priority.LOWEST)
		public void onProfileJoin(ProfileJoinEvent event) {
			FakeItem.removeAllItemOpeners(event.getProfile());
		}

	}

}