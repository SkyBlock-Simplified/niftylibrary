package net.netcoding.niftybukkit._new_.minecraft.event.profile;

import net.netcoding.niftybukkit._new_.minecraft.event.Event;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;

public abstract class ProfileEvent extends Event {

	private final BukkitMojangProfile profile;

	protected ProfileEvent(BukkitMojangProfile profile) {
		this.profile = profile;
	}

	public BukkitMojangProfile getProfile() {
		return this.profile;
	}

}