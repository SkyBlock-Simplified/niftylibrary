package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;

abstract class InventoryEvent {

	private final transient MojangProfile profile;

	InventoryEvent(MojangProfile profile) {
		this.profile = profile;
	}

	/**
	 * Gets the profile associated with the event.
	 * 
	 * @return Profile of who performed the event.
	 */
	public MojangProfile getProfile() {
		return this.profile;
	}

}