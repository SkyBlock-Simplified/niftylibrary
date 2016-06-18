package net.netcoding.niftybukkit._new_.api.inventory.events;

import net.netcoding.niftybukkit._new_.api.inventory.FakeInventory;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;

abstract class FakeEvent {

	private final BukkitMojangProfile profile;
	private final BukkitMojangProfile target;

	FakeEvent(BukkitMojangProfile profile) {
		this.profile = profile;
		this.target = FakeInventory.getTargetAnywhere(profile);
	}

	/**
	 * Gets the profile performing the event.
	 *
	 * @return Profile of who performed the event.
	 */
	public final BukkitMojangProfile getProfile() {
		return this.profile;
	}

	/**
	 * Gets the profile being viewed by the event.
	 *
	 * @return Profile being viewed by the event.
	 */
	public final BukkitMojangProfile getTarget() {
		return this.target;
	}

}