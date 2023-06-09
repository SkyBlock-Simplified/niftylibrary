package net.netcoding.nifty.common.api.inventory;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.inventory.events.FakeInventoryClickEvent;
import net.netcoding.nifty.common.api.inventory.events.FakeInventoryCloseEvent;
import net.netcoding.nifty.common.api.inventory.events.FakeInventoryOpenEvent;
import net.netcoding.nifty.common.api.inventory.item.FakeItem;
import net.netcoding.nifty.common.api.plugin.Event;
import net.netcoding.nifty.common.api.plugin.MinecraftListener;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.event.EventResult;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryClickEvent;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryCloseEvent;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryOpenEvent;
import net.netcoding.nifty.common.minecraft.event.player.PlayerRespawnEvent;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class FakeInventory extends FakeInventoryFrame {

	private static final transient ConcurrentMap<UUID, ConcurrentMap<MinecraftMojangProfile, FakeInventoryInfo>> OPENED = Concurrent.newMap();
	private final UUID uniqueId = UUID.randomUUID();
	private final transient FakeInventoryListener listener;
	private final FakeItem fakeItem;

	public FakeInventory(MinecraftPlugin plugin, FakeInventoryListener listener) {
		OPENED.put(this.getUniqueId(), Concurrent.newMap());
		this.listener = listener;
		this.fakeItem = new FakeItem(plugin, listener);
		new FakeMinecraftListener(plugin);
	}

	public final void close(MinecraftMojangProfile profile) {
		if (this.isOpen(profile))
			profile.getOfflinePlayer().getPlayer().closeInventory();
	}

	public final void closeAll() {
		this.getOpened().keySet().forEach(this::close);
	}

	private Inventory createInventory(Player targetPlayer, FakeInventoryFrame frame) {
		Map<Integer, ItemStack> items = frame.getItems();
		int currentPage = frame.getCurrentPage();
		int start = (currentPage - 1) * frame.getTotalSlots();
		int calcTotalSlots = Math.max(frame.getTotalSlots(), Math.min(54, calculateTotalSlots(items.size() - start)));
		int pageLeftIndex = frame.getPageLeftSlot() < 0 ? (calcTotalSlots - 2) : frame.getPageLeftSlot();
		int pageRightIndex = frame.getPageRightSlot() < 0 ? (calcTotalSlots - 1) : frame.getPageRightSlot();
		Inventory inventory = Nifty.getServer().createInventory(targetPlayer, calcTotalSlots, frame.getTitle());

		if (items.size() > calcTotalSlots)
			start--; // Next

		if (currentPage > 1)
			start--; // Previous

		System.out.println("Skipping to " + start + " of " + calcTotalSlots);

		if (frame.isAutoCentered()) {
			int full = calcTotalSlots - 9;
			int space = (int)Math.floor((9 - (calcTotalSlots - full)) / 2.0);
			Iterator<ItemStack> iterator = items.values().iterator();

			for (int h = 0; h < start; h++)
				iterator.next();

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
			ConcurrentList<Integer> keys = Concurrent.newList(items.keySet());
			int push = 0;

			for (int i = 0; i < items.size(); i++) {
				if (i < start)
					continue;

				Integer index = keys.get(i);

				if (index >= calcTotalSlots)
					break;

				if (pageLeftIndex == i && currentPage > 1) {
					inventory.setItem(i, frame.getPageLeft());
					//i--;
					push++;
				} else if (pageRightIndex == i && items.size() > calcTotalSlots) {
					inventory.setItem(i, frame.getPageRight());
					//i--;
					push++;
				} else
					inventory.setItem(index + push, items.get(index));
			}
		}

		frame.generateSignature(inventory.getContents());
		return inventory;
	}

	public static ItemStack getClickedItem(InventoryClickEvent event) {
		return getClickedItem(event, true);
	}

	public static ItemStack getClickedItem(InventoryClickEvent event, boolean firstClick) {
		return ItemStack.of(event.isShiftClick() || firstClick ? event.getCurrentItem() : event.getCursor());
	}

	public final ItemStack getItemOpener() {
		return this.fakeItem.getItemOpener();
	}

	public final int getItemOpenerSlot() {
		return this.fakeItem.getItemOpenerSlot();
	}

	public final MinecraftMojangProfile getTarget(MinecraftMojangProfile profile) {
		return this.isOpen(profile) ? this.getOpened().get(profile).getTarget() : profile;
	}

	public static MinecraftMojangProfile getTargetAnywhere(MinecraftMojangProfile profile) {
		Collection<ConcurrentMap<MinecraftMojangProfile, FakeInventoryInfo>> instances = OPENED.values();
		instances = instances.stream().filter(x -> x.containsKey(profile)).collect(Collectors.toSet());

		if (instances.size() == 1)
			return instances.iterator().next().get(profile).getTarget();

		return profile;
	}

	public final MinecraftMojangProfile getTargeter(MinecraftMojangProfile profile) {
		ConcurrentMap<MinecraftMojangProfile, FakeInventoryInfo> opened = this.getOpened();

		for (Map.Entry<MinecraftMojangProfile, FakeInventoryInfo> info : opened.entrySet()) {
			if (info.getValue().getTarget().equals(profile))
				return info.getKey();
		}

		return profile;
	}

	public final UUID getUniqueId() {
		return this.uniqueId;
	}

	public FakeInventoryInstance getInstance(MinecraftMojangProfile profile) {
		if (this.isOpen(profile))
			return new FakeInventoryInstance(this.getOpened().get(profile).getFrame(), this, profile);
		else
			return this.newInstance(profile);
	}

	public final void giveItemOpener(MinecraftMojangProfile profile) {
		this.fakeItem.giveItemOpener(profile);
	}

	public ConcurrentMap<MinecraftMojangProfile, FakeInventoryInfo> getOpened() {
		ConcurrentMap<MinecraftMojangProfile, FakeInventoryInfo> opened = OPENED.get(this.getUniqueId());
		opened.keySet().stream().filter(profile -> !profile.isOnlineLocally()).forEach(opened::remove);
		return opened;
	}

	public final boolean isOpen(MinecraftMojangProfile profile) {
		return profile.isOnlineLocally() && this.getOpened().containsKey(profile);
	}

	public static boolean isOpenAnywhere(MinecraftMojangProfile profile) {
		for (UUID uniqueId : OPENED.keySet()) {
			if (OPENED.get(uniqueId).containsKey(profile))
				return true;
		}

		return false;
	}

	public final boolean isItemOpener(ItemStack ItemStack) {
		return this.fakeItem.isItemOpener(ItemStack);
	}

	public final boolean isItemOpenerDestructable() {
		return this.fakeItem.isItemOpenerDestructable();
	}

	public final boolean isTargeted(MinecraftMojangProfile profile) {
		for (FakeInventoryInfo info : this.getOpened().values()) {
			if (info.getTarget().equals(profile))
				return true;
		}

		return false;
	}

	public FakeInventoryInstance newInstance(MinecraftMojangProfile profile) {
		FakeInventoryInstance instance = new FakeInventoryInstance(this, profile);
		instance.setAllowEmpty(this.isAllowEmpty());
		instance.setAutoCenter(this.isAutoCentered());
		instance.setTradingEnabled(this.isTradingEnabled());
		instance.setTitle(this.getTitle());
		return instance;
	}

	public void open(MinecraftMojangProfile profile) {
		this.open(profile, (this.isOpen(profile) ? this.getTarget(profile) : profile));
	}

	public void open(MinecraftMojangProfile profile, MinecraftMojangProfile target) {
		this.open(profile, target, this);
	}

	void open(MinecraftMojangProfile profile, MinecraftMojangProfile target, FakeInventoryFrame frame) {
		if (frame.isAllowEmpty() || !frame.getItems().isEmpty()) {
			if (profile.isOnlineLocally() && target.isOnlineLocally()) {
				Player viewerPlayer = profile.getOfflinePlayer().getPlayer();
				Player targetPlayer = target.getOfflinePlayer().getPlayer();
				FakeInventoryFrame updated = frame;

				if (updated.getItems().isEmpty())
					updated.add(ItemStack.of(Material.AIR));

				if (this.isOpen(profile)) {
					if (MinecraftProtocol.isPre1_8())
						return;

					FakeInventoryFrame current = this.getOpened().get(profile).getFrame();
					current.update(updated);
					updated = current;
				}

				FakeInventoryInfo info = new FakeInventoryInfo(target, updated);
				info.setOpening(this.isOpen(profile));
				this.getOpened().put(profile, info);
				viewerPlayer.openInventory(this.createInventory(targetPlayer, updated));
			}
		}
	}

	public final void removeItemOpener(MinecraftMojangProfile profile) {
		this.fakeItem.removeItemOpener(profile);
	}

	public final void reopenAll() {
		for (Map.Entry<MinecraftMojangProfile, FakeInventoryInfo> instance : this.getOpened().entrySet())
			this.open(instance.getKey(), instance.getValue().getTarget(), instance.getValue().getFrame());
	}

	public final void reopenAll(FakeInventoryFrame frame) {
		for (MinecraftMojangProfile profile : this.getOpened().keySet())
			this.open(profile, this.getTarget(profile), frame);
	}

	public void setItemOpener(ItemStack ItemStack) {
		this.setItemOpener(-1, ItemStack);
	}

	public void setItemOpener(int index, ItemStack ItemStack) {
		this.fakeItem.setItemOpener(index, ItemStack);
	}

	public void setItemOpenerDestructable() {
		this.setItemOpenerDestructable(true);
	}

	public void setItemOpenerDestructable(boolean value) {
		this.fakeItem.setItemOpenerDestructable(value);
	}

	private class FakeMinecraftListener extends MinecraftListener {

		public FakeMinecraftListener(MinecraftPlugin plugin) {
			super(plugin);
		}

		@Event(priority = Event.Priority.HIGH, ignoreCancelled = true)
		public void onInventoryClick(InventoryClickEvent event) {
			if (!(event.getWhoClicked() instanceof Player)) return;
			if (InventoryType.SlotType.OUTSIDE == event.getSlotType()) return;
			final MinecraftMojangProfile profile = Nifty.getMojangRepository().searchByPlayer((Player)event.getWhoClicked());
			final MinecraftMojangProfile target = FakeInventory.this.getTarget(profile);
			final ItemStack firstClickItem = getClickedItem(event);
			final ItemStack placeClickItem = getClickedItem(event, false);
			final Player player = profile.getOfflinePlayer().getPlayer();

			if (firstClickItem.getNbt().notEmpty()) {
				this.getLog().message(player, "Item NBT: {0}", firstClickItem.getNbt());
			}

			if (FakeInventory.this.isOpen(profile)) {
				final FakeInventoryFrame frame = FakeInventory.this.getOpened().get(profile).getFrame();
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
						boolean traded = false;

						if (Math.min(rawSlot, event.getRawSlot()) >= event.getInventory().getSize())
							verified = true;
						else {
							ItemStack itemStack = (rawSlot >= event.getInventory().getSize() ? placeClickItem : frame.getMetadata(NbtKeys.TRADE_ITEM));
							rawSlot = (rawSlot >= event.getInventory().getSize() ? event.getRawSlot() : rawSlot);
							contents[rawSlot] = itemStack;
							frame.putMetadata("replace", true);

							if (Math.max(rawSlot, event.getRawSlot()) < event.getInventory().getSize())
								verified = true;
							else {
								verified = frame.verifySignature(contents);
								traded = true;
							}
						}

						frame.putMetadata(NbtKeys.TRADE_COMPLETE, true);
						frame.putMetadata(NbtKeys.TRADE_SLOT, rawSlot);
						frame.putMetadata(NbtKeys.TRADE_SWITCH, traded);
					}
				}

				if (verified && !event.isShiftClick()) {
					if (!frame.isTradingEnabled()) { // Non-Trading Event
						if (firstClickItem.getNbt().containsPath(NbtKeys.PAGING.getPath())) {
							frame.setCurrentPage(firstClickItem.getNbt().<Integer>getPath(NbtKeys.PAGING.getPath()));
							event.setCancelled(true);

							Nifty.getScheduler().schedule(() -> {
								System.out.println("OPENING PAGE: " + frame.getCurrentPage() + ":" + frame.getItems().size());
								FakeInventory.this.open(profile, target, frame);
							});
						} else {
							if (event.getRawSlot() < event.getInventory().getSize()) {
								if (!event.isShiftClick()) {
									if (Material.AIR == placeClickItem.getType()) {
										FakeInventoryClickEvent myEvent = new FakeInventoryClickEvent(profile, event);
										FakeInventory.this.listener.onInventoryClick(myEvent);
									}
								}

								event.setCancelled(true);
							}
						}
					} else { // Trading Event
						boolean clearCurrent = false;

						if (placeClickItem.getNbt().containsPath(NbtKeys.PAGING.getPath())) {
							frame.setCurrentPage(placeClickItem.getNbt().<Integer>getPath(NbtKeys.PAGING.getPath()));
							event.setCancelled(true);

							Nifty.getScheduler().schedule(() -> {
								System.out.println("OPENING PAGE: " + frame.getCurrentPage() + ":" + frame.getItems().size());
								FakeInventory.this.open(profile, target, frame);
							});
						} else {
							if (frame.hasMetadata(NbtKeys.TRADE_COMPLETE)) {
								FakeInventoryClickEvent myEvent = new FakeInventoryClickEvent(profile, event);
								FakeInventory.this.listener.onInventoryClick(myEvent);
								event.setCancelled(myEvent.isCancelled());
								this.getLog().console("Cancelled: {0}", event.isCancelled());

								if (!event.isCancelled()) {
									int rawSlot = frame.getMetadata(NbtKeys.TRADE_SLOT);
									this.getLog().console("Replace: {0}:{1}", rawSlot, event.getRawSlot());

									//if (event.getRawSlot() < event.getInventory().getSize())
									//	contents[event.getRawSlot()] = myEvent.getPlacedItem().clone();

									//player.getOpenInventory().getTopInventory().setItem(rawSlot, myEvent.getPlacedItem());
									clearCurrent = true;
								}
							}
						}

						if (frame.hasMetadata(NbtKeys.TRADE_COMPLETE)) {
							this.getLog().console("Trade Switch: {0}", frame.getMetadata(NbtKeys.TRADE_SWITCH));
							if (!event.isCancelled()) {
								frame.generateSignature(player.getOpenInventory().getTopInventory().getContents());

								if (clearCurrent) {
									//event.setCurrentItem(new ItemStack(Material.AIR));
									//event.setCursor(new ItemStack(Material.AIR));
								}
							} else {
								int rawSlot = frame.getMetadata(NbtKeys.TRADE_SLOT);
								this.getLog().console("Slots: {0}:{1}:{2}", rawSlot, event.getRawSlot(), event.getSlot());

								//if (event.getRawSlot() < event.getInventory().getSize())
								//	contents[event.getRawSlot()] = myEvent.getPlacedItem().clone();

								//player.getOpenInventory().getTopInventory().setItem(rawSlot, placeClickItem);
								//event.setCurrentItem(new ItemStack(Material.AIR));
								//event.setCursor(new ItemStack(Material.AIR));
								//player.getOpenInventory().getTopInventory().setContents(contents);
								ItemStack[] more1 = player.getOpenInventory().getTopInventory().getContents().clone();
								for (int i = 0; i < more1.length; i++) {
									//this.getLog().console("Cancelled: {0}:{1}", i, (more1[i] == null ? "null" : more1[i].getType()));
								}
							}

							for (int i = 0; i < contents.length; i++) {
								//this.getLog().console("Contents: {0}:{1}", i, (contents[i] == null ? "null" : contents[i].getType()));
							}
							for (int i = 0; i < original.length; i++) {
								//this.getLog().console("Original: {0}:{1}", i, (original[i] == null ? "null" : original[i].getType()));
							}
						}
					}
				} else
					event.setCancelled(true);

				if (event.isCancelled() || frame.hasMetadata(NbtKeys.TRADE_COMPLETE)) {
					frame.removeMetadata(NbtKeys.TRADE_COMPLETE);
					frame.removeMetadata(NbtKeys.TRADE_ITEM);
					frame.removeMetadata(NbtKeys.TRADE_SLOT);
					frame.removeMetadata(NbtKeys.TRADE_SWITCH);
				}
			}

			if (event.isCancelled()) {
				event.setResult(EventResult.DENY);
				player.updateInventory();
			}
		}

		@Event
		public void onInventoryClose(InventoryCloseEvent event) {
			HumanEntity entity = event.getPlayer();

			if (entity instanceof Player) {
				final MinecraftMojangProfile profile = Nifty.getMojangRepository().searchByPlayer((Player)event.getPlayer());
				Player player = profile.getOfflinePlayer().getPlayer();
				player.updateInventory();

				if (FakeInventory.this.isOpen(profile)) {
					FakeInventoryInfo info = FakeInventory.this.getOpened().get(profile);

					if (!info.isOpening()) {
						FakeInventory.this.getOpened().remove(profile);
						FakeInventory.this.listener.onInventoryClose(new FakeInventoryCloseEvent(profile, event.getInventory()));
					}

					info.setOpening(false);
				}
			}
		}

		@Event(priority = Event.Priority.HIGHEST)
		public void onInventoryOpen(InventoryOpenEvent event) {
			HumanEntity entity = event.getPlayer();

			if (entity instanceof Player) {
				final MinecraftMojangProfile profile = Nifty.getMojangRepository().searchByPlayer((Player)event.getPlayer());

				if (FakeInventory.this.isOpen(profile)) {
					FakeInventoryOpenEvent myEvent = new FakeInventoryOpenEvent(profile, event.getInventory());
					FakeInventory.this.listener.onInventoryOpen(myEvent);
					event.setCancelled(myEvent.isCancelled());
				}
			}
		}

		@Event
		public void onPlayerRespawn(PlayerRespawnEvent event) {
			FakeInventory.this.giveItemOpener(event.getProfile());
		}

	}

}