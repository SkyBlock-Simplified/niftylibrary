package net.netcoding.niftybukkit.inventory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class FakeInventory extends FakeInventoryFrame implements Listener {

	private static final transient ConcurrentHashMap<UUID, ConcurrentSet<String>> opened = new ConcurrentHashMap<>();
	private final transient UUID uuid = UUID.randomUUID();
	private final transient FakeInventoryListener listener;
	private transient ItemStack itemOpener;

	public FakeInventory(JavaPlugin plugin, FakeInventoryListener listener) {
		this(plugin, listener, false);
	}

	public FakeInventory(JavaPlugin plugin, FakeInventoryListener listener, boolean autoCancel) {
		super(plugin, autoCancel);
		opened.put(this.getUUID(), new ConcurrentSet<String>());
		this.listener = listener;
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	public void close(Player player) {
		if (player != null && this.isOpen(player)) player.closeInventory();
	}

	public void close(String playerName) {
		this.close(BukkitHelper.findPlayer(playerName));
	}

	public ItemStack getItemOpener() {
		return this.itemOpener;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public boolean isOpen(HumanEntity entity) {
		return entity != null && entity instanceof Player && this.isOpen((Player)entity);
	}

	public boolean isOpen(Player player) {
		return player != null && this.isOpen(player.getName());
	}

	public boolean isOpen(String playerName) {
		for (String currentPlayer : opened.get(this.getUUID())) {
			if (currentPlayer.equals(playerName))
				return true;
		}

		return false;
	}

	public static boolean isOpenAnywhere(Player player) {
		if (player == null) return false;

		for (UUID uuid : opened.keySet()) {
			for (String currentPlayer : opened.get(uuid)) {
				if (currentPlayer.equals(player.getName()))
					return true;
			}
		}

		return false;
	}

	public static boolean isOpenAnywhere(String playerName) {
		return isOpenAnywhere(findPlayer(playerName));
	}

	public FakeInventoryInstance newInstance(Player player) {
		return this.newInstance(player.getName());
	}

	public FakeInventoryInstance newInstance(String playerName) {
		return new FakeInventoryInstance(this.getPlugin(), this, playerName, this.isAutoCancelled());
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity entity = event.getWhoClicked();

		if (entity instanceof Player) {
			if (this.isOpen(entity)) {
				if (!event.getSlotType().equals(SlotType.OUTSIDE)) {
					final ItemStack currentItem = event.getClick().isShiftClick() ? event.getCurrentItem() : event.getCursor();

					if (currentItem != null) {
						if (!Material.AIR.equals(currentItem.getType())) {
							if (event.getClick().isShiftClick() && this.isShiftClickDisabled()) {
								event.setCancelled(true);
								return;
							} else if (this.getItemOpener() != null && this.getItemOpener().isSimilar(currentItem)) {
								event.setCancelled(true);
								return;
							}

							this.listener.onInventoryClick(event);
							if (this.isAutoCancelled()) event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		HumanEntity entity = event.getPlayer();

		if (entity instanceof Player) {
			if (this.isOpen(entity)) {
				opened.get(this.getUUID()).remove(entity.getName());
				this.listener.onInventoryClose(event);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		HumanEntity entity = event.getPlayer();

		if (entity instanceof Player) {
			if (this.isOpen(entity))
				this.listener.onInventoryOpen(event);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();

		if (handItem != null && this.getItemOpener() != null) {
			if (!Material.AIR.equals(handItem.getType())) {
				if (handItem.isSimilar(this.getItemOpener())) {
					if (handItem.isSimilar(this.getItemOpener())) {
						this.open(player);
						event.setCancelled(true);
					}
				}
			}
		}
	}

	public void open(String playerName) {
		this.open(findPlayer(playerName));
	}

	public void open(Player player) {
		this.open(player, this.getItemsArray());
	}

	void open(Player player, ItemStack[] items) {
		if (player != null) {
			if (items != null) {
				if (!this.isOpen(player)) {
					Inventory inventory = Bukkit.createInventory(player, 9, "");
					inventory.setContents(items);
					opened.get(this.getUUID()).add(player.getName());
					player.openInventory(inventory);
				}
			}
		}
	}

	public void setItemOpener(ItemStack itemOpener) {
		if (itemOpener != null && !Material.AIR.equals(itemOpener.getType())) {
			this.itemOpener = itemOpener;
		}
	}

}