package net.netcoding.nifty.common.api.inventory.events;

import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;

public final class FakeInventoryOpenEvent extends FakeInventoryEvent implements Cancellable {

	private boolean cancelled = false;

	public FakeInventoryOpenEvent(MinecraftMojangProfile profile, Inventory inventory) {
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