package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.event.Event;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public interface PlayerEvent extends Event {

	default Player getPlayer() {
		return this.getProfile().getOfflinePlayer().getPlayer();
	}

	MinecraftMojangProfile getProfile();

}