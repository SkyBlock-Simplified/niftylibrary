package net.netcoding.nifty.common.api.inventory.events;

import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public final class FakeItemInteractEvent extends FakeEvent implements Cancellable {

	private final ItemStack item;
	private boolean cancelled = false;

	public FakeItemInteractEvent(BukkitMojangProfile profile, ItemStack item) {
		super(profile);
		this.item = item.clone();
	}

	public final ItemStack getItem() {
		return this.item;
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