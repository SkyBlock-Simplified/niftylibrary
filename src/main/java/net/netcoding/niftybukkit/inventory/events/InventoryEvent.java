package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

abstract class InventoryEvent {

	private final BukkitMojangProfile profile;
	private final BukkitMojangProfile target;

	InventoryEvent(BukkitMojangProfile profile) {
		this(profile, profile);
	}

	InventoryEvent(BukkitMojangProfile profile, BukkitMojangProfile target) {
		this.profile = profile;
		this.target = target;
	}

	/**
	 * Gets the profile performing the event.
	 * 
	 * @return Profile of who performed the event.
	 */
	public BukkitMojangProfile getProfile() {
		return this.profile;
	}

	/**
	 * Gets the profile being viewed by the event.
	 * 
	 * @return Profile being viewed by the event.
	 */
	public BukkitMojangProfile getTarget() {
		return this.target;
	}

}