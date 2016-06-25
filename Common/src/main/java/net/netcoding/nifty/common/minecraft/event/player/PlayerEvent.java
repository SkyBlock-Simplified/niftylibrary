package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.minecraft.event.Event;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public interface PlayerEvent extends Event {

	default Player getPlayer() {
		return this.getProfile().getOfflinePlayer().getPlayer();
	}

	BukkitMojangProfile getProfile();

}