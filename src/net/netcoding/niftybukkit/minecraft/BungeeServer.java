package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

public class BungeeServer extends MinecraftServer {

	boolean loadedOnce = false;
	final ConcurrentSet<MojangProfile> playersLeft = new ConcurrentSet<>();

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public Set<MojangProfile> getPlayerList() {
		ConcurrentSet<MojangProfile> totalList = new ConcurrentSet<>();
		totalList.addAll(this.playerList);
		if (this.isCurrentServer()) totalList.addAll(this.playersLeft);
		return Collections.unmodifiableSet(totalList);
	}

	@Override
	public void reset() {
		super.reset();
		this.loadedOnce = false;
	}

	void setAddress(String ip, int port) {
		this.address = InetSocketAddress.createUnresolved(ip, port);
	}

}