package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public final class PlayerNameChangeEvent implements PlayerEvent {

	private final MinecraftMojangProfile profile;

	public PlayerNameChangeEvent(MinecraftMojangProfile profile) {
		this.profile = profile;
	}

	@Override
	public MinecraftMojangProfile getProfile() {
		return this.profile;
	}

}