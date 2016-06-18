package net.netcoding.niftybukkit._new_.minecraft.event.bungee;

import net.netcoding.niftybukkit._new_.api.plugin.messaging.BungeeServer;
import net.netcoding.niftybukkit._new_.minecraft.event.Event;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class BungeeLoadedEvent<T extends BukkitMojangProfile> extends Event {

	private final transient ConcurrentSet<BungeeServer> serverList;

	public BungeeLoadedEvent(Collection<BungeeServer> serverList) {
		this.serverList = new ConcurrentSet<>(serverList);
	}

	public Set<BungeeServer> getServers() {
		return Collections.unmodifiableSet(this.serverList);
	}

}