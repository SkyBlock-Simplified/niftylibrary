package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public final class PlayerNameChangeEvent implements PlayerEvent {

	private final BukkitMojangProfile profile;

	public PlayerNameChangeEvent(BukkitMojangProfile profile) {
		this.profile = profile;
	}

	@Override
	public BukkitMojangProfile getProfile() {
		return this.profile;
	}

}