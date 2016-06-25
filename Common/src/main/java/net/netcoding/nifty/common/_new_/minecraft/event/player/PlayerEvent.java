package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.event.Event;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;

public interface PlayerEvent extends Event {

	default Player getPlayer() {
		return this.getProfile().getOfflinePlayer().getPlayer();
	}

	BukkitMojangProfile getProfile();

}