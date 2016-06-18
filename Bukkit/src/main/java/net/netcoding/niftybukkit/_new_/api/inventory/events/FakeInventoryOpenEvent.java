package net.netcoding.niftybukkit._new_.api.inventory.events;

import net.netcoding.niftybukkit._new_.minecraft.event.Cancellable;
import net.netcoding.niftybukkit._new_.minecraft.inventory.Inventory;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;

public final class FakeInventoryOpenEvent extends FakeInventoryEvent implements Cancellable {

	private boolean cancelled = false;

	public FakeInventoryOpenEvent(BukkitMojangProfile profile, Inventory inventory) {
		super(profile, inventory);
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