package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftycore.mojang.MojangProfile;

abstract class InventoryEvent {

	private final MojangProfile profile;
	private final MojangProfile target;

	InventoryEvent(MojangProfile profile) {
		this(profile, profile);
	}

	InventoryEvent(MojangProfile profile, MojangProfile target) {
		this.profile = profile;
		this.target = target;
	}

	/**
	 * Gets the profile performing the event.
	 * 
	 * @return Profile of who performed the event.
	 */
	public MojangProfile getProfile() {
		return this.profile;
	}

	/**
	 * Gets the profile being viewed by the event.
	 * 
	 * @return Profile being viewed by the event.
	 */
	public MojangProfile getTarget() {
		return this.target;
	}

}