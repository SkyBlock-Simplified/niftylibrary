package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryClickEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryCloseEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryItemInteractEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryOpenEvent;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.ListUtil;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FakeInventory extends FakeInventoryFrame {

	private static final transient ConcurrentHashMap<UUID, ConcurrentHashMap<BukkitMojangProfile, FakeInventoryFrame>> OPENED = new ConcurrentHashMap<>();
	private static final transient ConcurrentHashMap<UUID, ConcurrentHashMap<BukkitMojangProfile, BukkitMojangProfile>> HOLDERS = new ConcurrentHashMap<>();
	private final UUID uniqueId = UUID.randomUUID();
	private final transient FakeInventoryListener listener;
	private ItemStack itemOpener;
	private int itemOpenerSlot = -1;

	public FakeInventory(JavaPlugin plugin, FakeInventoryListener listener) {
		super(plugin);
		this.create();
		this.listener = listener;
	}

	public static boolean isOpenAnywhere(BukkitMojangProfile profile) {
		for (UUID uniqueId : OPENED.keySet()) {
			if (OPENED.get(uniqueId).keySet().contains(profile))
				return true;
		}

		return false;
	}

	public static ItemStack getClickedItem(org.bukkit.event.inventory.InventoryClickEvent event) {
		return getClickedItem(event, true);
	}

	public static ItemStack getClickedItem(org.bukkit.event.inventory.InventoryClickEvent event, boolean firstClick) {
		return event.isShiftClick() || firstClick ? event.getCurrentItem() : event.getCursor();
	}

	public void close(BukkitMojangProfile profile) {
		OPENED.get(this.getUniqueId()).remove(profile);
		HOLDERS.get(this.getUniqueId()).remove(profile);
	}

	public void closeAll() {
		for (BukkitMojangProfile profile : this.getOpened().keySet())
			this.close(profile);
	}

	public void create() {
		OPENED.put(this.getUniqueId(), new ConcurrentHashMap<BukkitMojangProfile, FakeInventoryFrame>());
		HOLDERS.put(this.getUniqueId(), new ConcurrentHashMap<BukkitMojangProfile, BukkitMojangProfile>());
	}

	public void destroy() {
		this.closeAll();
		OPENED.remove(this.getUniqueId());
		HOLDERS.remove(this.getUniqueId());
	}

	public boolean exists() {
		return OPENED.containsKey(this.getUniqueId());
	}

	public ItemStack getItemOpener() {
		return this.itemOpener;
	}

	public int getItemOpenerSlot() {
		return this.itemOpenerSlot > 0 ? this.itemOpenerSlot : 0;
	}

	public BukkitMojangProfile getTargeter(BukkitMojangProfile profile) {
		for (BukkitMojangProfile targeter : HOLDERS.get(this.getUniqueId()).keySet()) {
			if (HOLDERS.get(this.getUniqueId()).get(targeter).equals(profile))
				return targeter;
		}

		return profile;
	}

	public UUID getUniqueId() {
		return this.uniqueId;
	}

	public void giveItemOpener(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (this.getItemOpener() != null) {
				if (profile.getOfflinePlayer().isOnline())
					profile.getOfflinePlayer().getPlayer().getInventory().setItem(this.getItemOpenerSlot(), this.getItemOpener());
			}
		}
	}

	public ConcurrentHashMap<BukkitMojangProfile, FakeInventoryFrame> getOpened() {
		for (BukkitMojangProfile profile : OPENED.get(this.getUniqueId()).keySet()) {
			if (!profile.isOnlineLocally())
				OPENED.get(this.getUniqueId()).remove(profile);
		}

		return OPENED.get(this.getUniqueId());
	}

	public boolean isOpen(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (profile.getOfflinePlayer().isOnline())
				return OPENED.get(this.getUniqueId()).keySet().contains(profile);
		}

		return false;
	}

	public boolean isTargeted(BukkitMojangProfile profile) {
		if (this.exists()) {
			if (profile.getOfflinePlayer().isOnline()) {
				if (!HOLDERS.get(this.getUniqueId()).keySet().contains(profile))
					return HOLDERS.get(this.getUniqueId()).values().contains(profile);
			}
		}

		return false;
	}

	public FakeInventoryInstance getInstance(BukkitMojangProfile profile) {
		if (!this.exists()) this.create();
		FakeInventoryInstance instance;

		if (this.isOpen(profile))
			instance = new FakeInventoryInstance(OPENED.get(this.getUniqueId()).get(profile), this, profile);
		else
			instance = new FakeInventoryInstance(this.getPlugin(), this, profile);

		instance.setAutoCancelled(this.isAutoCancelled());
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
		final ItemStack firstClickItem = FakeInventory.getClickedItem(event);
		final ItemStack placeClickItem = FakeInventory.getClickedItem(event, false);

		if (this.getItemOpener() != null) {
			if (firstClickItem.isSimilar(this.getItemOpener()) || placeClickItem.isSimilar(this.getItemOpener())) {
				event.setResult(Result.DENY);
				profile.getOfflinePlayer().getPlayer().updateInventory();
				event.setCancelled(true);
				return;
			}
		}

		if (this.isOpen(profile)) {
			FakeInventoryFrame frame = OPENED.get(this.getUniqueId()).get(profile);

			if (Material.AIR != firstClickItem.getType()) {
				if (event.getRawSlot() < event.getInventory().getSize()) {
					InventoryClickEvent myEvent = new InventoryClickEvent(profile, event);
					this.listener.onInventoryClick(myEvent);
					event.setCancelled(!frame.isTradingEnabled() || myEvent.isCancelled());
				}

				if (!frame.isTradingEnabled() && event.isShiftClick()) {
					event.setCancelled(true);
					return;
				}

				if (event.isCancelled())
					event.setResult(Result.DENY);
			} else if (Material.AIR != placeClickItem.getType()) {
				if (!frame.isTradingEnabled() && event.getRawSlot() < event.getInventory().getSize()) {
					event.getInventory().setItem(event.getRawSlot(), new ItemStack(Material.AIR));
					ItemStack newItem = placeClickItem.clone();
					placeClickItem.setAmount(0);
					event.setCursor(new ItemStack(Material.AIR));
					InventoryWorkaround.addItems(profile.getOfflinePlayer().getPlayer().getInventory(), newItem);
					profile.getOfflinePlayer().getPlayer().updateInventory();
					event.setCancelled(true);
					event.setResult(Result.DENY);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
		HumanEntity entity = event.getPlayer();

		if (entity instanceof Player) {
			BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getPlayer());

			if (this.isOpen(profile)) {
				OPENED.get(this.getUniqueId()).remove(profile);
				HOLDERS.get(this.getUniqueId()).remove(profile);
				this.listener.onInventoryClose(new InventoryCloseEvent(profile, event));
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryCreative(InventoryCreativeEvent event) {
		ItemStack clickItem = event.getCursor();

		if (this.getItemOpener() != null) {
			if (clickItem.isSimilar(this.getItemOpener())) {
				event.setCursor(new ItemStack(Material.AIR));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onInventoryOpen(org.bukkit.event.inventory.InventoryOpenEvent event) {
		HumanEntity entity = event.getPlayer();

		if (entity instanceof Player) {
			BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getPlayer());

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
			for (ItemStack item : e.getDrops()) {
				if (item.isSimilar(this.getItemOpener()))
					item.setAmount(0);
			}
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (this.getItemOpener() != null) {
			if (event.getItemDrop().getItemStack().isSimilar(this.getItemOpener()))
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();
		BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());

		if (!this.isOpen(profile)) {
			if (Action.PHYSICAL != event.getAction()) {
				if (handItem != null && this.getItemOpener() != null) {
					if (Material.AIR != handItem.getType()) {
						if (handItem.isSimilar(this.getItemOpener())) {
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
		if (!frame.getItems().isEmpty()) {

			if (!this.isOpen(profile) && target.isOnlineLocally()) {
				Player viewerPlayer = profile.getOfflinePlayer().getPlayer();
				Player targetPlayer = target.getOfflinePlayer().getPlayer();
				Inventory inventory = Bukkit.createInventory(targetPlayer, frame.getTotalSlots(), frame.getTitle());
				HOLDERS.get(this.getUniqueId()).put(profile, target);

				if (frame.isAutoCentered()) {
					int full = this.calculateTotalSlots(frame.getItems().size()) - 9;
					int space = (int)Math.floor((9 - (frame.getItems().size() - full)) / 2.0);

					for (int i = 0; i < full; i++)
						inventory.addItem(frame.getItems().get(i));

					for (int j = full; j < frame.getItems().size(); j++)
						inventory.setItem(j + space, frame.getItems().get(j));
				} else {
					for (ItemStack item : frame.getItems())
						inventory.setItem(frame.getItems().indexOf(item), item);
				}


				OPENED.get(this.getUniqueId()).put(profile, frame);
				viewerPlayer.closeInventory();
				viewerPlayer.openInventory(inventory);
			}
		}
	}

	public void setItemOpener(ItemStack itemOpener) {
		this.setItemOpener(-1, itemOpener);
	}

	public void setItemOpener(int slot, ItemStack itemOpener) {
		if (Material.AIR != itemOpener.getType()) {
			this.itemOpenerSlot = slot;
			this.itemOpener = itemOpener;
		}
	}

	public <T extends ItemStack> void update(T[] items) {
		this.update(this.getOpened().keySet(), Arrays.asList(items));
	}

	public <T extends ItemStack> void update(BukkitMojangProfile profile, T[] items) {
		this.update(Collections.singletonList(profile), Arrays.asList(items));
	}

	public <T extends ItemStack> void update(Collection<BukkitMojangProfile> profiles, T[] items) {
		this.update(profiles, Arrays.asList(items));
	}

	public <T extends ItemStack> void update(BukkitMojangProfile profile, Collection<? extends T> items) {
		this.update(Collections.singletonList(profile), items);
	}

	public <T extends ItemStack> void update(Collection<BukkitMojangProfile> profiles, Collection<? extends T> items) {
		for (BukkitMojangProfile profile : profiles) {
			Player player = profile.getOfflinePlayer().getPlayer();
			player.getOpenInventory().getTopInventory().setContents(ListUtil.toArray(items, ItemStack.class));
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
			FakeInventoryFrame current = OPENED.get(this.getUniqueId()).get(profile);
			boolean changed = current.isAutoCancelled() != updated.isAutoCancelled();
			changed = changed || current.isAutoCentered() != updated.isAutoCentered();
			changed = changed || current.isTradingEnabled() != updated.isTradingEnabled();
			changed = changed || current.getTotalSlots() != updated.getTotalSlots();
			changed = changed || !current.getTitle().equalsIgnoreCase(updated.getTitle());
			Player player = profile.getOfflinePlayer().getPlayer();

			if (!changed)
				player.getOpenInventory().getTopInventory().setContents(ListUtil.toArray(updated.getItems(), ItemStack.class));
			else {
				BukkitMojangProfile target = HOLDERS.get(this.getUniqueId()).get(profile);
				this.close(profile);
				this.open(profile, target);
			}
		}
	}

}