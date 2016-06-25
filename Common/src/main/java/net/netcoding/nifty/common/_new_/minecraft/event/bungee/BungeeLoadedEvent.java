package net.netcoding.nifty.common._new_.minecraft.event.bungee;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;

import java.util.Set;

public final class BungeeLoadedEvent<T extends BukkitMojangProfile> implements BungeeEvent {

	public Set<BungeeServer<BukkitMojangProfile>> getServers() {
		return Nifty.getBungeeHelper().getServers();
	}

}