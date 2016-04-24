package net.netcoding.niftybukkit.minecraft.inventory.events;

import net.netcoding.niftybukkit.minecraft.inventory.FakeInventory;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class InventoryClickEvent extends InventoryCancellableBukkitEvent {

	public InventoryClickEvent(BukkitMojangProfile profile, BukkitMojangProfile target, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, target, event);
	}

	private org.bukkit.event.inventory.InventoryClickEvent getClickEvent() {
		return (org.bukkit.event.inventory.InventoryClickEvent)this.event;
	}

	public ClickType getClick() {
		return this.getClickEvent().getClick();
	}

	public ItemData getClickedItem() {
		return this.getClickedItem(true);
	}

	public ItemData getClickedItem(boolean firstClick) {
		return FakeInventory.getClickedItem(this.getClickEvent(), firstClick);
	}

	public int getHotbarButton() {
		return this.getClickEvent().getHotbarButton();
	}

	public int getRawSlot() {
		return this.getClickEvent().getRawSlot();
	}

	public int getSlot() {
		return this.getClickEvent().getSlot();
	}

	public SlotType getSlotType() {
		return this.getClickEvent().getSlotType();
	}

}