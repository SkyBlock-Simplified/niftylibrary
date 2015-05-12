package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftybukkit.inventory.FakeInventory;
import net.netcoding.niftycore.mojang.MojangProfile;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent extends InventoryCancellableBukkitEvent {

	public InventoryClickEvent(MojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, event);
	}

	private org.bukkit.event.inventory.InventoryClickEvent getClickEvent() {
		return (org.bukkit.event.inventory.InventoryClickEvent)this.event;
	}

	public ClickType getClick() {
		return this.getClickEvent().getClick();
	}

	public ItemStack getClickedItem() {
		return this.getClickedItem(true);
	}

	public ItemStack getClickedItem(boolean firstClick) {
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