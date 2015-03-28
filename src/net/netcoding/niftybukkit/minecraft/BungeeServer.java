package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;
import java.util.Set;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

public class BungeeServer extends MinecraftServer {

	boolean loadedOnce = false;
	final ConcurrentSet<MojangProfile> playersLeft = new ConcurrentSet<>();

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	Set<MojangProfile> getTotalPlayerList() {
		ConcurrentSet<MojangProfile> playerList = new ConcurrentSet<>(super.getPlayerList());
		playerList.addAll(this.playersLeft);
		return playerList;
	}

	@Override
	public void reset() {
		super.reset();
		this.playersLeft.clear();
	}

	void setAddress(String ip, int port) {
		this.address = new InetSocketAddress(ip, port);
	}

}