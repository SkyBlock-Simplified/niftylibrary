package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryClickEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryCloseEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryItemInteractEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryOpenEvent;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class FakeInventory extends FakeInventoryFrame {

	private static final transient ConcurrentMap<UUID, ConcurrentMap<BukkitMojangProfile, FakeInventoryFrame>> OPENED = new ConcurrentMap<>();
	private static final transient ConcurrentMap<UUID, ConcurrentMap<BukkitMojangProfile, BukkitMojangProfile>> HOLDERS = new ConcurrentMap<>();
	private final UUID uniqueId = UUID.randomUUID();
	private final transient FakeInventoryListener listener;
	private ItemData itemOpener;
	private int itemOpenerSlot = -1;
	private boolean itemOpenerDestructable = false;

	public FakeInventory(JavaPlugin plugin, FakeInventoryListener listener) {
		super();
		this.create();
		this.listener = listener;
		new FakeBukkitListener(plugin);
	}

	public final void close(BukkitMojangProfile profile) {
		OPENED.get(this.getUniqueId()).remove(profile);
		HOLDERS.get(this.getUniqueId()).remove(profile);
	}

	public final void closeAll() {
		for (BukkitMojangProfile profile : this.getOpened().keySet())
			this.close(profile);
	}

	private void create() {
		if (!this.exists()) {
			OPENED.put(this.getUniqueId(), new ConcurrentMap<BukkitMojangProfile, FakeInventoryFrame>());
			HOLDERS.put(this.getUniqueId(), new ConcurrentMap<BukkitMojangProfile, BukkitMojangProfile>());
		}
	}

	private Inventory createInventory(Player targetPlayer, FakeInventoryFrame frame) {
		Map<Integer, ItemData> items = frame.getItems();
		Iterator<ItemData> iterator = items.values().iterator();
		int currentPage = frame.getCurrentPage();
		int start = (currentPage - 1) * frame.getTotalSlots();

		for (int h = 0; h < start; h++)
			iterator.next();

		int calcTotalSlots = Math.max(frame.getTotalSlots(), Math.min(54, calculateTotalSlots(items.size() - start)));
		int pageLeftIndex = frame.getPageLeftSlot() < 0 ? (calcTotalSlots - 2) : frame.getPageLeftSlot();
		int pageRightIndex = frame.getPageRightSlot() < 0 ? (calcTotalSlots - 1) : frame.getPageRightSlot();
		Inventory inventory = Bukkit.createInventory(targetPlayer, calcTotalSlots, frame.getTitle());

		if (frame.isAutoCentered()) {
			int full = calcTotalSlots - 9;
			int space = (int)Math.floor((9 - (calcTotalSlots - full)) / 2.0);

			for (int i = 0; i < full; i++) {
				if (pageLeftIndex == i && currentPage > 1)
					inventory.addItem(frame.getPageLeft());
				else if (pageRightIndex == i && items.size() > calcTotalSlots)
					inventory.addItem(frame.getPageRight());
				else
					inventory.addItem(iterator.next());
			}

			for (int j = full; j < calcTotalSlots; j++) {
				int index = j + space;

				if (pageLeftIndex == index && currentPage > 1)
					inventory.setItem(index, frame.getPageLeft());
				else if (pageRightIndex == j && items.size() > calcTotalSlots)
					inventory.setItem(index, frame.getPageRight());
				else
					inventory.setItem(index + space, iterator.next());
			}
		} else {
			int push = 0;
			int added = 0;

			for (Map.Entry<Integer, ItemData> item : items.entrySet()) {
				if (pageLeftIndex == item.getKey() && currentPage > 1) {
					push++;
					inventory.setItem(item.getKey(), frame.getPageLeft());
				} else if (pageRightIndex == item.getKey() && items.size() > calcTotalSlots) {
					push++;
					inventory.setItem(item.getKey(), frame.getPageRight());
				} else
					inventory.setItem((item.getKey() + push), item.getValue());

				added += 1;
				if (added == calcTotalSlots)
					break;
			}
		}

		frame.generateSignature(inventory.getContents());
		return inventory;
	}

	public final boolean exists() {
		return OPENED.containsKey(this.getUniqueId());
	}

	public static ItemData getClickedItem(org.bukkit.event.inventory.InventoryClickEvent event) {
		return getClickedItem(event, true);
	}

	public static ItemData getClickedItem(org.bukkit.event.inventory.InventoryClickEvent event, boolean firstClick) {
		return new ItemData(event.isShiftClick() || firstClick ? event.getCurrentItem() : event.getCursor());
	}

	public ItemData getItemOpener() {
		return this.itemOpener;
	}

	public int getItemOpenerSlot() {
		return this.itemOpenerSlot > 0 ? this.itemOpenerSlot : 0;
	}

	public final BukkitMojangProfile getTarget(BukkitMojangProfile profile) {
		return HOLDERS.get(this.getUniqueId()).get(profile);
	}

	public final BukkitMojangProfile getTargeter(BukkitMojangProfile profile) {
		ConcurrentMap<BukkitMojangProfile, BukkitMojangProfile> targets = HOLDERS.get(this.getUniqueId());

		for (BukkitMojangProfile opener : targets.keySet()) {
			if (targets.get(opener).equals(profile))
				return opener;
		}

		return profile;
	}

	public final UUID getUniqueId() {
		return this.uniqueId;
	}

	public FakeInventoryInstance getInstance(BukkitMojangProfile profile) {
		if (this.isOpen(profile))
			return new FakeInventoryInstance(this.getOpened().get(profile), this, profile);
		else
			return this.newInstance(profile);
	}

	public void giveItemOpener(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (this.getItemOpener() != null) {
				if (profile.getOfflinePlayer().isOnline())
					profile.getOfflinePlayer().getPlayer().getInventory().setItem(this.getItemOpenerSlot(), this.getItemOpener());
			}
		}
	}

	public ConcurrentMap<BukkitMojangProfile, FakeInventoryFrame> getOpened() {
		for (BukkitMojangProfile profile : OPENED.get(this.getUniqueId()).keySet()) {
			if (!profile.isOnlineLocally())
				OPENED.get(this.getUniqueId()).remove(profile);
		}

		return OPENED.get(this.getUniqueId());
	}

	public static boolean isAnyItemOpener(ItemData itemData) {
		return itemData.containsNbtKey(NbtKeys.ITEM_OPENER.getKey());
	}

	public final boolean isOpen(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (profile.getOfflinePlayer().isOnline())
				return OPENED.get(this.getUniqueId()).keySet().contains(profile);
		}

		return false;
	}

	public static boolean isOpenAnywhere(BukkitMojangProfile profile) {
		for (UUID uniqueId : OPENED.keySet()) {
			if (OPENED.get(uniqueId).keySet().contains(profile))
				return true;
		}

		return false;
	}

	private boolean isItemOpener(ItemData itemData) {
		return isAnyItemOpener(itemData) && this.getUniqueId().toString().equals(itemData.<String>getNbt(NbtKeys.ITEM_OPENER.getKey()));
	}

	public boolean isItemOpenerDestructable() {
		return this.itemOpenerDestructable;
	}

	public final boolean isTargeted(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (profile.getOfflinePlayer().isOnline())
				return HOLDERS.get(this.getUniqueId()).values().contains(profile);
		}

		return false;
	}

	public FakeInventoryInstance newInstance(BukkitMojangProfile profile) {
		FakeInventoryInstance instance = new FakeInventoryInstance(this, profile);
		instance.setAllowEmpty(this.isAllowEmpty());
		instance.setAutoCenter(this.isAutoCentered());
		instance.setTradingEnabled(this.isTradingEnabled());
		instance.setTitle(this.getTitle());
		return instance;
	}



	public void open(BukkitMojangProfile profile) {
		this.open(profile, (this.isOpen(profile) ? this.getTarget(profile) : profile));
	}

	public void open(BukkitMojangProfile profile, BukkitMojangProfile target) {
		this.open(profile, target, this);
	}

	void open(BukkitMojangProfile profile, BukkitMojangProfile target, FakeInventoryFrame frame) {
		if (frame.isAllowEmpty() || !frame.getItems().isEmpty()) {
			if (profile.isOnlineLocally() && target.isOnlineLocally()) {
				Player viewerPlayer = profile.getOfflinePlayer().getPlayer();
				Player targetPlayer = target.getOfflinePlayer().getPlayer();
				FakeInventoryFrame updated = frame;

				if (this.isOpen(profile)) {
					FakeInventoryFrame current = this.getOpened().get(profile);
					current.update(updated);
					updated = current;
				}

				Inventory inventory = this.createInventory(targetPlayer, updated);
				viewerPlayer.openInventory(inventory);
				OPENED.get(this.getUniqueId()).put(profile, updated);
				HOLDERS.get(this.getUniqueId()).put(profile, target);
			}
		}
	}

	public final void reopenAll() {
		for (BukkitMojangProfile profile : this.getOpened().keySet()) {
			BukkitMojangProfile target = this.getTarget(profile);
			FakeInventoryFrame frame = this.getOpened().get(profile);
			this.open(profile, target, frame);
		}
	}

	public final void reopenAll(FakeInventoryFrame frame) {
		for (BukkitMojangProfile profile : this.getOpened().keySet()) {
			BukkitMojangProfile target = this.getTarget(profile);
			this.open(profile, target, frame);
		}
	}

	public void setItemOpenerDestructable() {
		this.setItemOpenerDestructable(true);
	}

	public void setItemOpenerDestructable(boolean value) {
		this.itemOpenerDestructable = value;
	}

	public void setItemOpener(ItemData itemData) {
		this.setItemOpener(-1, itemData);
	}

	public void setItemOpener(int index, ItemData itemData) {
		if (itemData != null && Material.AIR != itemData.getType()) {
			this.itemOpenerSlot = index;
			ItemData itemOpener = itemData.clone();
			itemOpener.putNbt(NbtKeys.ITEM_OPENER.getKey(), this.getUniqueId().toString());
			this.itemOpener = itemOpener;
		}
	}

	private class FakeBukkitListener extends BukkitListener {

		public FakeBukkitListener(JavaPlugin plugin) {
			super(plugin);
		}

		@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
		public void onInventoryClick(final org.bukkit.event.inventory.InventoryClickEvent event) {
			if (!(event.getWhoClicked() instanceof Player)) return;
			if (InventoryType.SlotType.OUTSIDE == event.getSlotType()) return;
			final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getWhoClicked());
			final BukkitMojangProfile target = FakeInventory.this.getTarget(profile);
			ItemData firstClickItem = getClickedItem(event);
			ItemData placeClickItem = getClickedItem(event, false);
			final Player player = profile.getOfflinePlayer().getPlayer();

			if (getItemOpener() != null) {
				if (FakeInventory.this.isItemOpener(firstClickItem) || FakeInventory.this.isItemOpener(placeClickItem))
					event.setCancelled(true);
			}

			if (FakeInventory.this.isOpen(profile)) {
				if (!event.isCancelled()) {
					FakeInventoryFrame frame = FakeInventory.this.getOpened().get(profile);
					ItemStack[] original = player.getOpenInventory().getTopInventory().getContents().clone();
					ItemStack[] contents = original.clone();
					boolean verified = frame.verifySignature(contents);

					if (frame.isTradingEnabled()) {
						if (!frame.hasMetadata(NbtKeys.TRADE_SLOT)) {
							frame.putMetadata(NbtKeys.TRADE_SLOT, event.getRawSlot());

							if (event.getRawSlot() < event.getInventory().getSize())
								frame.putMetadata(NbtKeys.TRADE_ITEM, firstClickItem);
						} else {
							int rawSlot = frame.getMetadata(NbtKeys.TRADE_SLOT);

							if (Math.max(rawSlot, event.getRawSlot()) < event.getInventory().getSize())
								verified = true;
							else if (Math.min(rawSlot, event.getRawSlot()) >= event.getInventory().getSize())
								verified = true;
							else {
								ItemStack itemStack = (rawSlot >= event.getInventory().getSize() ? placeClickItem : frame.<ItemStack>getMetadata(NbtKeys.TRADE_ITEM));
								rawSlot = (rawSlot >= event.getInventory().getSize() ? event.getRawSlot() : rawSlot);
								frame.putMetadata(NbtKeys.TRADE_SLOT, rawSlot);
								frame.putMetadata(NbtKeys.TRADE_COMPLETE, true);
								contents[rawSlot] = itemStack;

								if (!verified)
									verified = frame.verifySignature(contents);
							}
						}
					}

					if (verified && !event.isShiftClick()) {
						if (!frame.isTradingEnabled()) { // Non-Trading Event
							if (firstClickItem.containsNbtKey(NbtKeys.PAGING.getKey())) {
								frame.setCurrentPage(firstClickItem.<Integer>getNbt(NbtKeys.PAGING.getKey()));
								FakeInventory.this.open(profile, target, frame);
							} else {
								if (event.getRawSlot() < event.getInventory().getSize()) {
									if (!event.isShiftClick()) {
										if (Material.AIR == placeClickItem.getType()) {
											InventoryClickEvent myEvent = new InventoryClickEvent(profile, event);
											FakeInventory.this.listener.onInventoryClick(myEvent);
										}
									}

									event.setCancelled(true);
								}
							}
						} else { // Trading Event
							boolean clearCurrent = false;

							if (placeClickItem.containsNbtKey(NbtKeys.PAGING.getKey())) {
								frame.setCurrentPage(placeClickItem.<Integer>getNbt(NbtKeys.PAGING.getKey()));
								FakeInventory.this.open(profile, target, frame);
							} else {
								if (frame.hasMetadata(NbtKeys.TRADE_COMPLETE)) {
									InventoryClickEvent myEvent = new InventoryClickEvent(profile, event);
									FakeInventory.this.listener.onInventoryClick(myEvent);
									event.setCancelled(myEvent.isCancelled());
									this.getLog().console("Cancelled: {0}", event.isCancelled());

									if (!event.isCancelled()) {
										if (event.getRawSlot() < event.getInventory().getSize()) {
											int rawSlot = frame.getMetadata(NbtKeys.TRADE_SLOT);
											contents[rawSlot] = myEvent.getPlacedItem().clone();
										}

										player.getOpenInventory().setItem(event.getRawSlot(), myEvent.getPlacedItem());
										clearCurrent = true;
									}
								}
							}

							if (!event.isCancelled()) {
								frame.generateSignature(player.getOpenInventory().getTopInventory().getContents());

								if (clearCurrent)
									event.setCurrentItem(new ItemStack(Material.AIR));
							}
						}
					} else
						event.setCancelled(true);

					if (event.isCancelled() || frame.hasMetadata(NbtKeys.TRADE_COMPLETE)) {
						frame.removeMetadata(NbtKeys.TRADE_SLOT);
						frame.removeMetadata(NbtKeys.TRADE_ITEM);
						frame.removeMetadata(NbtKeys.TRADE_COMPLETE);
					}
				}

				if (event.isCancelled()) {
					event.setResult(Event.Result.DENY);
					player.updateInventory();
				}
			}
		}

		@EventHandler
		public void onInventoryClose(final org.bukkit.event.inventory.InventoryCloseEvent event) {
			HumanEntity entity = event.getPlayer();

			if (entity instanceof Player) {
				final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getPlayer());
				profile.getOfflinePlayer().getPlayer().updateInventory();

				if (FakeInventory.this.isOpen(profile)) {
					OPENED.get(FakeInventory.this.getUniqueId()).remove(profile);
					HOLDERS.get(FakeInventory.this.getUniqueId()).remove(profile);
					FakeInventory.this.listener.onInventoryClose(new InventoryCloseEvent(profile, event));
				}
			}
		}

		@EventHandler
		public void onInventoryCreative(InventoryCreativeEvent event) {
			if (FakeInventory.this.getItemOpener() != null) {
				ItemData itemData = new ItemData(Material.AIR == event.getCursor().getType() ? event.getCurrentItem() : event.getCursor());

				if (FakeInventory.this.isItemOpener(itemData)) {
					event.setCancelled(true);
					event.setResult(Event.Result.DENY);
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
		public void onInventoryOpen(final org.bukkit.event.inventory.InventoryOpenEvent event) {
			HumanEntity entity = event.getPlayer();

			if (entity instanceof Player) {
				final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getPlayer());

				if (FakeInventory.this.isOpen(profile)) {
					InventoryOpenEvent myEvent = new InventoryOpenEvent(profile, event);
					FakeInventory.this.listener.onInventoryOpen(myEvent);
					event.setCancelled(myEvent.isCancelled());
				}
			}
		}

		@EventHandler(ignoreCancelled = false)
		public void onPlayerDeath(PlayerDeathEvent e) {
			if (FakeInventory.this.getItemOpener() != null) {
				for (ItemStack itemStack : e.getDrops()) {
					ItemData itemData = new ItemData(itemStack);

					if (FakeInventory.this.isItemOpener(itemData))
						itemStack.setAmount(0);
				}
			}
		}

		@EventHandler(ignoreCancelled = false)
		public void onPlayerDropItem(PlayerDropItemEvent event) {
			if (FakeInventory.this.getItemOpener() != null) {
				ItemStack itemStack = event.getItemDrop().getItemStack();

				if (FakeInventory.this.isItemOpener(new ItemData(itemStack))) {
					event.setCancelled(true);

					if (FakeInventory.this.isItemOpenerDestructable()) {
						event.getItemDrop().remove();
						itemStack.setAmount(0);
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
		public void onPlayerInteract(PlayerInteractEvent event) {
			final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());

			if (!FakeInventory.this.isOpen(profile)) {
				if (Action.PHYSICAL != event.getAction()) {
					ItemData handItem = new ItemData(event.getItem());

					if (Material.AIR != handItem.getType() && FakeInventory.this.getItemOpener() != null) {
						if (FakeInventory.this.isItemOpener(handItem)) {
							InventoryItemInteractEvent myEvent = new InventoryItemInteractEvent(profile, event);
							FakeInventory.this.listener.onInventoryItemInteract(myEvent);
							event.setCancelled(myEvent.isCancelled());
						}
					}
				}
			}
		}

		@EventHandler
		public void onPlayerPostLogin(PlayerPostLoginEvent event) {
			FakeInventory.this.giveItemOpener(event.getProfile());
		}

		@EventHandler
		public void onPlayerRespawn(PlayerRespawnEvent event) {
			FakeInventory.this.giveItemOpener(NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer()));
		}

	}

}