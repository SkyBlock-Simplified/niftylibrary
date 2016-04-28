package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryCreativeEvent;

public class InventoryCreativeNbtEvent extends PlayerEvent implements Cancellable {

	private final InventoryCreativeEvent event;
	private final ItemData itemData;
	private boolean cancelled = false;

	public InventoryCreativeNbtEvent(BukkitMojangProfile profile, InventoryCreativeEvent event, ItemData itemData) {
		super(profile);
		this.event = event;
		this.itemData = itemData;
	}

	public final InventoryCreativeEvent getEvent() {
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