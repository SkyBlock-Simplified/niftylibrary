package net.netcoding.nifty.common.api.inventory.events;

import net.netcoding.nifty.common.api.inventory.FakeInventory;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryClickEvent;
import net.netcoding.nifty.common.minecraft.inventory.ClickType;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

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