package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryClickEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryCloseEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryItemInteractEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryOpenEvent;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
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
import java.util.Map;
import java.util.UUID;

public class FakeInventory extends FakeInventoryFrame {

	static final String ITEM_OPENER_KEY = "FAKEINV_ITEM";
	static final String SIGNATURE_KEY = "FAKEINV_SIGNATURE";
	private static final transient ConcurrentMap<UUID, ConcurrentMap<BukkitMojangProfile, FakeInventoryFrame>> OPENED = new ConcurrentMap<>();
	private static final transient ConcurrentMap<UUID, ConcurrentMap<BukkitMojangProfile, BukkitMojangProfile>> HOLDERS = new ConcurrentMap<>();
	private final UUID uniqueId = UUID.randomUUID();
	private final transient FakeInventoryListener listener;
	private ItemData itemOpener;
	private int itemOpenerSlot = -1;
	private boolean itemOpenerDestructable = false;
	public boolean DEBUG = false;

	public FakeInventory(JavaPlugin plugin, FakeInventoryListener listener) {
		super(plugin);
		this.create();
		this.listener = listener;
	}

	public static ItemData getClickedItem(org.bukkit.event.inventory.InventoryClickEvent event) {
		return getClickedItem(event, true);
	}

	public static ItemData getClickedItem(org.bukkit.event.inventory.InventoryClickEvent event, boolean firstClick) {
		return new ItemData(event.isShiftClick() || firstClick ? event.getCurrentItem() : event.getCursor());
	}

	public final void close(BukkitMojangProfile profile) {
		OPENED.get(this.getUniqueId()).remove(profile);
		HOLDERS.get(this.getUniqueId()).remove(profile);
	}

	public final void closeAll() {
		for (BukkitMojangProfile profile : this.getOpened().keySet())
			this.close(profile);
	}

	public final void create() {
		if (!this.exists()) {
			OPENED.put(this.getUniqueId(), new ConcurrentMap<BukkitMojangProfile, FakeInventoryFrame>());
			HOLDERS.put(this.getUniqueId(), new ConcurrentMap<BukkitMojangProfile, BukkitMojangProfile>());
		}
	}

	private Inventory createInventory(Player targetPlayer, boolean autoCentered, int totalSlots, String title, Map<Integer, ItemData> items) {
		Inventory inventory = Bukkit.createInventory(targetPlayer, totalSlots, title);

		if (autoCentered) {
			ConcurrentList<ItemData> itemList = new ConcurrentList<>(items.values());
			int full = this.calculateTotalSlots(itemList.size()) - 9;
			int space = (int)Math.floor((9 - (itemList.size() - full)) / 2.0);

			for (int i = 0; i < full; i++)
				inventory.addItem(items.get(i));

			for (int j = full; j < items.size(); j++)
				inventory.setItem(j + space, items.get(j));
		} else {
			for (Integer index : items.keySet())
				inventory.setItem(index, items.get(index));
		}

		return inventory;
	}

	public final void destroy() {
		if (this.exists()) {
			this.closeAll();
			OPENED.remove(this.getUniqueId());
			HOLDERS.remove(this.getUniqueId());
		}
	}

	public final boolean exists() {
		return OPENED.containsKey(this.getUniqueId());
	}

	public ItemData getItemOpener() {
		return this.itemOpener;
	}

	public int getItemOpenerSlot() {
		return this.itemOpenerSlot > 0 ? this.itemOpenerSlot : 0;
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
		return itemData.hasNbt(ITEM_OPENER_KEY);
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
		return this.getUniqueId().toString().equals(itemData.getNbt(ITEM_OPENER_KEY));
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
			FakeInventoryFrame frame = this.getOpened().get(profile);
			boolean verified = frame.verifySignature(player.getOpenInventory().getTopInventory().getContents());

			if (verified || frame.isTradingEnabled()) {
				if (Material.AIR != firstClickItem.getType() && !frame.isTradingEnabled()) {
					if (event.getRawSlot() < event.getInventory().getSize()) {
						InventoryClickEvent myEvent = new InventoryClickEvent(profile, event);
						this.listener.onInventoryClick(myEvent);
						event.setCancelled(!frame.isTradingEnabled() || myEvent.isCancelled());
					}

					if (!frame.isTradingEnabled() && event.isShiftClick())
						event.setCancelled(true);
				} else if (Material.AIR != placeClickItem.getType()) {
					// TODO: Adjust contents for verification to remove isTradingEnabled() workaround
					if (frame.isTradingEnabled()) {
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
				Inventory inventory = this.createInventory(targetPlayer, frame.isAutoCentered(), frame.getTotalSlots(), frame.getTitle(), frame.getItems());
				viewerPlayer.closeInventory();
				OPENED.get(this.getUniqueId()).put(profile, frame);
				HOLDERS.get(this.getUniqueId()).put(profile, target);
				frame.createSignature();
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

	public void setItemOpener(ItemData itemOpener) {
		this.setItemOpener(-1, itemOpener);
	}

	public void setItemOpener(int slot, ItemData itemOpener) {
		if (itemOpener != null && Material.AIR != itemOpener.getType()) {
			this.itemOpenerSlot = slot;
			ItemData nbtOpener = itemOpener.clone();
			nbtOpener.putNbt(ITEM_OPENER_KEY, this.getUniqueId().toString());
			this.itemOpener = new ItemData(nbtOpener);
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

	public void update(BukkitMojangProfile profile, Map<Integer, ItemData> items) {
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
		for (BukkitMojangProfile profile : profiles) {
			BukkitMojangProfile target = HOLDERS.get(this.getUniqueId()).get(profile);
			FakeInventoryFrame current = this.getOpened().get(profile);
			Player player = profile.getOfflinePlayer().getPlayer();
			current.clearItems();
			current.putAll(items);
			current.createSignature();
			Inventory inventory = this.createInventory(target.getOfflinePlayer().getPlayer(), current.isAutoCentered(), current.getTotalSlots(), current.getTitle(), current.getItems());
			player.getOpenInventory().getTopInventory().setContents(inventory.getContents());
		}
	}

	public void update(FakeInventoryFrame updated) {
		this.update(this.getOpened().keySet(), updated);
	}

	public void update(BukkitMojangProfile profile, FakeInventoryFrame updated) {
		this.update(Collections.singletonList(profile), updated);
	}

	public void update(Collection<BukkitMojangProfile> profiles, FakeInventoryFrame updated) {
		for (BukkitMojangProfile profile : profiles) {
			FakeInventoryFrame current = this.getOpened().get(profile);
			boolean requiresPacket = !current.getTitle().equals(updated.getTitle());
			requiresPacket = requiresPacket || current.getTotalSlots() != updated.getTotalSlots();
			boolean changed = requiresPacket;
			changed = changed || current.isAllowEmpty() != updated.isAllowEmpty();
			changed = changed || current.isAutoCentered() != updated.isAutoCentered();
			changed = changed || current.isTradingEnabled() != updated.isTradingEnabled();
			changed = changed || current.getTotalSlots() != updated.getTotalSlots();
			changed = changed || !current.getTitle().equalsIgnoreCase(updated.getTitle());
			changed = changed || !current.getAllMetadata().equals(updated.getAllMetadata());

			if (changed) {
				current.update(updated);

				if (requiresPacket)
					profile.updateOpenInventory(updated.getTitle(), updated.getTotalSlots());
			}

			this.update(profile, updated.getItems());
		}
	}

}