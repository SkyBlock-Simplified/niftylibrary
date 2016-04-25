package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.NiftyBukkit;
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
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class FakeInventory extends FakeInventoryFrame {

	private static final transient ConcurrentMap<UUID, ConcurrentMap<BukkitMojangProfile, FakeInventoryFrame>> OPENED = new ConcurrentMap<>();
	private static final transient ConcurrentMap<UUID, ConcurrentMap<BukkitMojangProfile, BukkitMojangProfile>> HOLDERS = new ConcurrentMap<>();
	private final UUID uniqueId = UUID.randomUUID();
	private final transient FakeInventoryListener listener;
	private ItemData itemOpener;
	private int itemOpenerIndex = -1;
	private boolean itemOpenerDestructable = false;

	public FakeInventory(JavaPlugin plugin, FakeInventoryListener listener) {
		super(plugin);
		this.create();
		this.listener = listener;
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

		for (int h = 0; h < start; h++) {
			iterator.next();
		}

		//this.getLog().console("Page: {0}", currentPage);
		//this.getLog().console("Items Size: {0}", items.size());
		//this.getLog().console("Start: {0}", start);
		//this.getLog().console("Calculate: {0}", calculateTotalSlots(items.size() - start));
		//this.getLog().console("Min: {0}", Math.min(calculateTotalSlots(items.size() - start), 54));
		//this.getLog().console("Total Slots 1: {0}", frame.getTotalSlots());
		int calcTotalSlots = Math.min(frame.getTotalSlots(), Math.min(54, calculateTotalSlots(items.size() - start)));
		//this.getLog().console("Total Slots 2: {0}", calcTotalSlots);
		int pageLeftIndex = frame.getPageLeftIndex() < 0 ? (calcTotalSlots - 2) : frame.getPageLeftIndex();
		int pageRightIndex = frame.getPageRightIndex() < 0 ? (calcTotalSlots - 1) : frame.getPageRightIndex();
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

	public int getItemOpenerIndex() {
		return this.itemOpenerIndex > 0 ? this.itemOpenerIndex : 0;
	}

	public BukkitMojangProfile getTarget(BukkitMojangProfile profile) {
		return HOLDERS.get(this.getUniqueId()).get(profile);
	}

	public BukkitMojangProfile getTargeter(BukkitMojangProfile profile) {
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
		this.create();
		FakeInventoryInstance instance;

		if (this.isOpen(profile))
			instance = new FakeInventoryInstance(this.getOpened().get(profile), this, profile);
		else
			instance = this.newInstance(profile);

		return instance;
	}

	public void giveItemOpener(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (this.getItemOpener() != null) {
				if (profile.getOfflinePlayer().isOnline())
					profile.getOfflinePlayer().getPlayer().getInventory().setItem(this.getItemOpenerIndex(), this.getItemOpener());
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
		return itemData.hasNbt(NbtKeys.ITEM_OPENER.getKey());
	}

	public boolean isOpen(BukkitMojangProfile profile) {
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
		return this.getUniqueId().toString().equals(itemData.getNbt(NbtKeys.ITEM_OPENER.getKey()));
	}

	public boolean isItemOpenerDestructable() {
		return this.itemOpenerDestructable;
	}

	public boolean isTargeted(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (profile.getOfflinePlayer().isOnline())
				return HOLDERS.get(this.getUniqueId()).values().contains(profile);
		}

		return false;
	}

	public FakeInventoryInstance newInstance(BukkitMojangProfile profile) {
		FakeInventoryInstance instance = new FakeInventoryInstance(this.getPlugin(), this, profile);
		instance.setAllowEmpty(this.isAllowEmpty());
		instance.setAutoCenter(this.isAutoCentered());
		instance.setTradingEnabled(this.isTradingEnabled());
		instance.setTitle(this.getTitle());
		return instance;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onInventoryClick(final org.bukkit.event.inventory.InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (SlotType.OUTSIDE == event.getSlotType()) return;
		final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getWhoClicked());
		final ItemData firstClickItem = FakeInventory.getClickedItem(event);
		final ItemData placeClickItem = FakeInventory.getClickedItem(event, false);
		final Player player = profile.getOfflinePlayer().getPlayer();
		boolean doSkip = false;

		if (this.getItemOpener() != null) {
			if (this.isItemOpener(firstClickItem) || this.isItemOpener(placeClickItem))
				event.setCancelled(doSkip = true);
		}

		if (this.isOpen(profile) && !doSkip) {
			this.getLog().message(player, "{{0}} is viewing fake inv!", profile.getName());
			this.getLog().broadcast("{{0}} is viewing fake inv!", profile.getName());
			FakeInventoryFrame frame = this.getOpened().get(profile);
			boolean verified = frame.verifySignature(player.getOpenInventory().getTopInventory().getContents());
			this.getLog().broadcast("Verified: {{0}}", verified);

			if (verified /*|| frame.isTradingEnabled()*/) {
				this.getLog().broadcast("Trading: {{0}}", frame.isTradingEnabled());
				this.getLog().console("First: {0}", firstClickItem);
				this.getLog().console("Place: {0}", placeClickItem);

				if (Material.AIR != firstClickItem.getType() && !frame.isTradingEnabled()) {
					this.getLog().broadcast("Made it x1");
					if (firstClickItem.hasNbt(NbtKeys.PAGING.getKey())) {
						this.getLog().broadcast("Made it x1.1");
						frame.setCurrentPage(firstClickItem.<Integer>getNbt(NbtKeys.PAGING.getKey()));
						this.update(profile, frame);
					} else {
						this.getLog().broadcast("Made it x1.2");
						if (event.getRawSlot() < event.getInventory().getSize()) {
							this.getLog().broadcast("Made it x1.3");
							InventoryClickEvent myEvent = new InventoryClickEvent(profile, event);
							this.listener.onInventoryClick(myEvent);
							event.setCancelled(!frame.isTradingEnabled() || myEvent.isCancelled());
						}

						if (!frame.isTradingEnabled() && event.isShiftClick())
							event.setCancelled(true);
					}
				} else if (Material.AIR != placeClickItem.getType()) {
					this.getLog().broadcast("Made it x2");
					if (placeClickItem.hasNbt(NbtKeys.PAGING.getKey())) {
						this.getLog().broadcast("Made it x2.1");
						frame.setCurrentPage(placeClickItem.<Integer>getNbt(NbtKeys.PAGING.getKey()));
						this.update(profile, frame);
					} else {
						this.getLog().broadcast("Made it x2.2");
						if (frame.isTradingEnabled()) {
							this.getLog().broadcast("Made it x2.3");
							InventoryClickEvent myEvent = new InventoryClickEvent(profile, event);
							this.listener.onInventoryClick(myEvent);
							event.setCancelled(!frame.isTradingEnabled() || myEvent.isCancelled());
						}

						if (!frame.isTradingEnabled() || event.isShiftClick())
							event.setCancelled(true);

						if (event.isCancelled() || (!frame.isTradingEnabled() && event.getRawSlot() < event.getInventory().getSize())) {
							event.setCursor(new ItemStack(Material.AIR));
							event.setCancelled(true);
						}

						// TODO: Adjust contents for verification to remove isTradingEnabled() workaround
						if (frame.isTradingEnabled() && !event.isCancelled())
							frame.generateSignature(player.getOpenInventory().getTopInventory().getContents());
					}
				}
			} else
				event.setCancelled(true);
		}

		if (event.isCancelled()) {
			event.setResult(Result.DENY);
			player.updateInventory();
		}
	}

	@EventHandler
	public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
		HumanEntity entity = event.getPlayer();

		if (entity instanceof Player) {
			final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getPlayer());

			if (this.isOpen(profile)) {
				OPENED.get(this.getUniqueId()).remove(profile);
				HOLDERS.get(this.getUniqueId()).remove(profile);
				this.listener.onInventoryClose(new InventoryCloseEvent(profile, event));
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryCreative(InventoryCreativeEvent event) {
		if (this.getItemOpener() != null) {
			ItemData clickItem = new ItemData(event.getCursor());

			if (this.isItemOpener(clickItem)) {
				event.setCursor(new ItemStack(Material.AIR));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onInventoryOpen(org.bukkit.event.inventory.InventoryOpenEvent event) {
		HumanEntity entity = event.getPlayer();

		if (entity instanceof Player) {
			final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getPlayer());

			if (this.isOpen(profile)) {
				InventoryOpenEvent myEvent = new InventoryOpenEvent(profile, event);
				this.listener.onInventoryOpen(myEvent);
				event.setCancelled(myEvent.isCancelled());
			}
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (this.getItemOpener() != null) {
			for (ItemStack itemStack : e.getDrops()) {
				ItemData itemData = new ItemData(itemStack);

				if (this.isItemOpener(itemData))
					itemStack.setAmount(0);
			}
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (this.getItemOpener() != null) {
			ItemStack itemStack = event.getItemDrop().getItemStack();

			if (this.isItemOpener(new ItemData(itemStack))) {
				event.setCancelled(true);

				if (this.isItemOpenerDestructable()) {
					event.getItemDrop().remove();
					itemStack.setAmount(0);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onPlayerInteract(PlayerInteractEvent event) {
		final BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());

		if (!this.isOpen(profile)) {
			if (Action.PHYSICAL != event.getAction()) {
				ItemData handItem = new ItemData(event.getItem());

				if (handItem != null && this.getItemOpener() != null) {
					if (Material.AIR != handItem.getType()) {
						if (this.isItemOpener(handItem)) {
							InventoryItemInteractEvent myEvent = new InventoryItemInteractEvent(profile, event);
							this.listener.onInventoryItemInteract(myEvent);
							event.setCancelled(myEvent.isCancelled());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerPostLogin(PlayerPostLoginEvent event) {
		this.giveItemOpener(event.getProfile());
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		this.giveItemOpener(NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer()));
	}

	public void open(BukkitMojangProfile profile) {
		this.open(profile, profile);
	}

	public void open(BukkitMojangProfile profile, BukkitMojangProfile target) {
		this.open(profile, target, this);
	}

	void open(BukkitMojangProfile profile, BukkitMojangProfile target, FakeInventoryFrame frame) {
		if (frame.isAllowEmpty() || !frame.getItems().isEmpty()) {
			if (profile.isOnlineLocally() && target.isOnlineLocally()) {
				Player viewerPlayer = profile.getOfflinePlayer().getPlayer();
				Player targetPlayer = target.getOfflinePlayer().getPlayer();
				Inventory inventory = this.createInventory(targetPlayer, frame);
				viewerPlayer.closeInventory();
				OPENED.get(this.getUniqueId()).put(profile, frame);
				HOLDERS.get(this.getUniqueId()).put(profile, target);
				viewerPlayer.openInventory(inventory);
			}
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
			this.itemOpenerIndex = index;
			ItemData itemOpener = itemData.clone();
			itemOpener.putNbt(NbtKeys.ITEM_OPENER.getKey(), this.getUniqueId().toString());
			this.itemOpener = itemOpener;
		}
	}

	public void update(ItemData[] items) {
		this.update(this.getOpened().keySet(), Arrays.asList(items));
	}

	public void update(Collection<ItemData> items) {
		this.update(this.getOpened().keySet(), items);
	}

	public void update(ConcurrentMap<Integer, ItemData> items) {
		this.update(this.getOpened().keySet(), items);
	}

	public void update(BukkitMojangProfile profile, ItemData[] items) {
		this.update(Collections.singletonList(profile), Arrays.asList(items));
	}

	public void update(BukkitMojangProfile profile, Collection<ItemData> items) {
		this.update(Collections.singletonList(profile), items);
	}

	public void update(Collection<BukkitMojangProfile> profiles, ItemData[] items) {
		this.update(profiles, Arrays.asList(items));
	}

	public void update(Collection<BukkitMojangProfile> profiles, Collection<ItemData> items) {
		for (BukkitMojangProfile profile : profiles) {
			FakeInventoryFrame current = this.getOpened().get(profile);
			current.clearItems();
			current.addAll(items);
			this.update(profile, current.getItems());
		}
	}

	public void update(Collection<BukkitMojangProfile> profiles, Map<Integer, ItemData> items) {
		for (BukkitMojangProfile profile : profiles)
			this.update(profile, items);
	}

	public void update(FakeInventoryFrame updated) {
		this.update(this.getOpened().keySet(), updated);
	}

	public void update(Collection<BukkitMojangProfile> profiles, FakeInventoryFrame updated) {
		for (BukkitMojangProfile profile : profiles)
			this.update(profile, updated);
	}

	public void update(BukkitMojangProfile profile, FakeInventoryFrame updated) {
		this.updateHandle(profile, updated);
	}

	public void update(BukkitMojangProfile profile, Map<Integer, ItemData> items) {
		FakeInventoryFrame current = this.getOpened().get(profile);
		current.clearItems();
		current.putAll(items);
		this.updateHandle(profile, current);
	}

	private void updateHandle(BukkitMojangProfile profile, FakeInventoryFrame updated) {
		FakeInventoryFrame current = this.getOpened().get(profile);
		boolean requiresPacket = !current.getTitle().equals(updated.getTitle());
		Player player = profile.getOfflinePlayer().getPlayer();
		requiresPacket = requiresPacket || current.getTotalSlots() != updated.getTotalSlots();
		this.getLog().console("Total Items Before: {{0}}:{{1}}", current.getItems().size(), updated.getItems().size());

		// TODO: TESTING
		int totalSlots = updated.getTotalSlots();
		this.getLog().console("Total Slots Before: {{0}}:{{1}}:{{2}}", current.getTotalSlots(), updated.getTotalSlots(), totalSlots);

		if (updated.getTotalSlots() < updated.getItems().size()) {
			updated.putMetadata(NbtKeys.TOTAL_SLOTS.getKey(), updated.getTotalSlots());
			totalSlots = calculateTotalSlots(updated.getItems().size());
		} else if (updated.getTotalSlots() > updated.getItems().size() && updated.hasMetadata(NbtKeys.TOTAL_SLOTS.getKey())) {
			totalSlots = updated.getMetadata(NbtKeys.TOTAL_SLOTS.getKey());
			updated.removeMetadata(NbtKeys.TOTAL_SLOTS.getKey());
		}

		this.getLog().console("Total Slots After 1: {{0}}:{{1}}:{{2}}", current.getTotalSlots(), updated.getTotalSlots(), totalSlots);
		// TODO: TESTING

		BukkitMojangProfile target = HOLDERS.get(this.getUniqueId()).get(profile);
		updated.setTotalSlots(totalSlots);
		current.update(updated);
		this.getLog().console("Total Slots After 2: {{0}}:{{1}}:{{2}}", current.getTotalSlots(), updated.getTotalSlots(), totalSlots);
		this.getLog().console("Total Items After: {{0}}:{{1}}", current.getItems().size(), updated.getItems().size());
		Inventory inventory = this.createInventory(target.getOfflinePlayer().getPlayer(), current);

		if (requiresPacket) {
			player.getOpenInventory().getTopInventory().clear();
			profile.updateOpenInventory(current.getTitle(), current.getTotalSlots(), inventory.getContents());
			player.getOpenInventory().getTopInventory().clear();
		}


		//CraftPlayer cp;
		//CraftInventoryPlayer cip;

		player.getOpenInventory().getTopInventory().clear();
		player.getOpenInventory().getTopInventory().setContents(inventory.getContents());
	}

}