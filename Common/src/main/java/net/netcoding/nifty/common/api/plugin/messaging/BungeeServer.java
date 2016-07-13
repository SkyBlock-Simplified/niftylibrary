package net.netcoding.nifty.common.api.plugin.messaging;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.core.api.MinecraftServer;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;

import java.net.InetSocketAddress;
import java.util.Set;

public final class BungeeServer<T extends MinecraftMojangProfile> extends MinecraftServer<T> {

	private static final InetSocketAddress CURRENT_ADDRESS = Nifty.getServer().getAddress();
	boolean loadedOnce = false;
	final ConcurrentSet<T> playersLeft = Concurrent.newSet();

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	Set<T> getUnsafePlayerList() {
		return this.playerList;
	}

	Set<T> getTotalPlayerList() {
		ConcurrentSet<T> playerList = Concurrent.newSet(super.getPlayerList());
		playerList.addAll(this.playerList);
		return playerList;
	}

	public final boolean isCurrentServer() {
		return CURRENT_ADDRESS.getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress()) && CURRENT_ADDRESS.getPort() == this.getAddress().getPort();
	}

	@Override
	protected final void reset() {
		super.reset();
		this.playersLeft.clear();
	}

	void setAddress(String ip, int port) {
		this.address = new InetSocketAddress(ip, port);
	}

	@Override
	protected final void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	@Override
	protected final void setMotd(String motd) {
		this.motd = motd;
	}

	@Override
	protected final void setOnline(boolean online) {
		this.online = online;
	}

	@Override
	protected final void setVersion(String name, int protocol) {
		this.version = new Version(name, protocol);
	}

}