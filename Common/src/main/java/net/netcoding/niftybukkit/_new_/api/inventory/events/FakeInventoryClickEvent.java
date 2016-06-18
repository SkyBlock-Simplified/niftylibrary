package net.netcoding.niftybukkit._new_.api.inventory.events;

import net.netcoding.niftybukkit._new_.api.inventory.FakeInventory;
import net.netcoding.niftybukkit._new_.minecraft.event.Cancellable;
import net.netcoding.niftybukkit._new_.minecraft.event.inventory.InventoryClickEvent;
import net.netcoding.niftybukkit._new_.minecraft.inventory.ClickType;
import net.netcoding.niftybukkit._new_.minecraft.inventory.Inventory;
import net.netcoding.niftybukkit._new_.minecraft.inventory.InventoryType;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit._new_.yaml.converters.ItemStack;

public final class FakeInventoryClickEvent extends FakeInventoryEvent implements Cancellable {

	private final InventoryClickEvent clickEvent;
	private boolean cancelled = false;

	public FakeInventoryClickEvent(BukkitMojangProfile profile, InventoryClickEvent clickEvent) {
		super(profile, clickEvent.getInventory());
		this.clickEvent = clickEvent;
	}

	private InventoryClickEvent getClickEvent() {
		return this.clickEvent;
	}

	public ClickType getClick() {
		return this.getClickEvent().getClick();
	}

	public ItemStack getClickedItem() {
		return FakeInventory.getClickedItem(this.getClickEvent(), true);
	}

	public int getHotbarButton() {
		return this.getClickEvent().getHotbarButton();
	}

	public ItemStack getPlacedItem() {
		return FakeInventory.getClickedItem(this.getClickEvent(), false);
	}

	public int getRawSlot() {
		return this.getClickEvent().getRawSlot();
	}

	public int getSlot() {
		return this.getClickEvent().getSlot();
	}

	public InventoryType.SlotType getSlotType() {
		return this.getClickEvent().getSlotType();
	}

	/**
	 * Gets if the event is cancelled.
	 *
	 * @return True if cancelled, otherwise false.
	 */
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Prevents the event from occurring.
	 *
	 * @param cancelled True to cancel, otherwise false.
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}