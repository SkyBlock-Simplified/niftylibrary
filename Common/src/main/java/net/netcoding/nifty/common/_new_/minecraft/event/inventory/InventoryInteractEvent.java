package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;

public abstract class InventoryInteractEvent extends InventoryEvent implements Cancellable {

	private boolean cancelled;

	public InventoryInteractEvent(BukkitMojangProfile profile) {
		super(profile);
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean value) {
		this.cancelled = value;
	}

}