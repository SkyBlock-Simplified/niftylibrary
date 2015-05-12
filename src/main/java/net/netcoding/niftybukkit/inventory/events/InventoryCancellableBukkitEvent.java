package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftycore.mojang.MojangProfile;

import org.bukkit.event.Cancellable;

abstract class InventoryCancellableBukkitEvent extends InventoryBukkitEvent implements Cancellable {

	private boolean cancelled = false;

	InventoryCancellableBukkitEvent(MojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, event);
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
