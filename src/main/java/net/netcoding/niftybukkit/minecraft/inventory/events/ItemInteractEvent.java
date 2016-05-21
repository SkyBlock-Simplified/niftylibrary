package net.netcoding.niftybukkit.minecraft.inventory.events;

import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

public class ItemInteractEvent extends InventoryEvent {

	private final ItemData itemData;

	public ItemInteractEvent(BukkitMojangProfile profile, ItemData itemData) {
		super(profile, profile);
		this.itemData = itemData.clone();
	}

	public final ItemData getItem() {
		return this.itemData;
	}

}