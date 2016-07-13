package net.netcoding.nifty.common.api.inventory.events;

import net.netcoding.nifty.common.api.inventory.FakeInventory;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

abstract class FakeEvent {

	private final MinecraftMojangProfile profile;
	private final MinecraftMojangProfile target;

	FakeEvent(MinecraftMojangProfile profile) {
		this.profile = profile;
		this.target = FakeInventory.getTargetAnywhere(profile);
	}

	/**
	 * Gets the profile performing the event.
	 *
	 * @return Profile of who performed the event.
	 */
	public final MinecraftMojangProfile getProfile() {
		return this.profile;
	}

	/**
	 * Gets the profile being viewed by the event.
	 *
	 * @return Profile being viewed by the event.
	 */
	public final MinecraftMojangProfile getTarget() {
		return this.target;
	}

}