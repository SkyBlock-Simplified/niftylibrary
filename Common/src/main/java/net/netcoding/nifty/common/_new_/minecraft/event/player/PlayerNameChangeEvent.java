package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;

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