package net.netcoding.niftybukkit.minecraft.inventory.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

abstract class InventoryEvent {

	private final BukkitMojangProfile profile;
	private final BukkitMojangProfile target;

	InventoryEvent(BukkitMojangProfile profile, BukkitMojangProfile target) {
		this.profile = profile;
		this.target = target;
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