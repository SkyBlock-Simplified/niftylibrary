package net.netcoding.niftybukkit._new_.minecraft.event.inventory;

import net.netcoding.niftybukkit._new_.minecraft.event.Cancellable;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.minecraft.items.ItemData;

public class InventoryCreativeNbtEvent extends InventoryCreativeEvent implements Cancellable {

	private final org.bukkit.event.inventory.InventoryCreativeEvent event;
	private final ItemData itemData;
	private boolean cancelled = false;

	public InventoryCreativeNbtEvent(BukkitMojangProfile profile, org.bukkit.event.inventory.InventoryCreativeEvent event, ItemData itemData) {
		super(profile);
		this.event = event;
		this.itemData = itemData;
	}

	public final org.bukkit.event.inventory.InventoryCreativeEvent getEvent() {
		return this.event;
	}

	public final ItemData getItem() {
		return this.itemData;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean value) {
		this.cancelled = value;
	}

}