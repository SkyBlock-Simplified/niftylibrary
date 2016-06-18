package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;

public class InventoryOpenEvent extends InventoryEvent implements Cancellable {

	private boolean cancelled;

	public InventoryOpenEvent(BukkitMojangProfile profile) {
		super(profile);
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean value) {
		this.cancelled = value;
	}

}