package net.netcoding.nifty.common._new_.api.plugin.messaging;

import net.netcoding.nifty.common._new_.api.BukkitMinecraftServer;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;

import java.net.InetSocketAddress;
import java.util.Set;

public class BungeeServer<T extends BukkitMojangProfile> extends BukkitMinecraftServer<T> {

	boolean loadedOnce = false;
	final ConcurrentSet<T> playersLeft = new ConcurrentSet<>();

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	Set<T> getUnsafePlayerList() {
		return this.playerList;
	}

	Set<T> getTotalPlayerList() {
		ConcurrentSet<T> playerList = new ConcurrentSet<>(super.getPlayerList());
		playerList.addAll(this.playersLeft);
		return playerList;
	}

	@Override
	protected final void reset() {
		super.reset();
		this.playersLeft.clear();
	}

	void setAddress(String ip, int port) {
		this.address = new InetSocketAddress(ip, port);
	}

	protected final void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	protected final void setMotd(String motd) {
		this.motd = motd;
	}

	protected final void setOnline(boolean online) {
		this.online = online;
	}

	protected final void setVersion(String name, int protocol) {
		this.version = new Version(name, protocol);
	}

}