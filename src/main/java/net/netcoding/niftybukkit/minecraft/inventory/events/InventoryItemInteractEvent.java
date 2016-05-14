package net.netcoding.niftybukkit.minecraft.inventory.events;

import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryItemInteractEvent extends InventoryEvent {

	private final transient PlayerInteractEvent event;

	public InventoryItemInteractEvent(BukkitMojangProfile profile, PlayerInteractEvent event) {
		super(profile, profile);
		this.event = event;
	}

	public ItemData getItem() {
		return new ItemData(this.event.getItem());
	}

}