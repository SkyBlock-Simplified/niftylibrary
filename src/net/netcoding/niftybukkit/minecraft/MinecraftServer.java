package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

abstract class MinecraftServer {

	protected InetSocketAddress address;
	private String gameVersion = "";
	private int maxPlayers = -1;
	private String motd = "";
	private boolean online = false;
	protected ConcurrentSet<MojangProfile> playerList = new ConcurrentSet<>();
	private int protocolVersion = -1;
	protected String serverName = "";

	MinecraftServer() { }

	public InetSocketAddress getAddress() {
		return this.address;
	}

	public String getGameVersion() {
		return this.gameVersion;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public String getMotd() {
		return this.motd;
	}

	public String getName() {
		return this.serverName;
	}

	public int getPlayerCount() {
		return this.playerList.size();
	}

	public Set<MojangProfile> getPlayerList() {
		return Collections.unmodifiableSet(this.playerList);
	}

	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	public final boolean isCurrentServer() {
		return this.getAddress() != null && NiftyBukkit.getPlugin().getServer().getIp().equals(this.getAddress().getHostName()) && this.getAddress().getPort() == NiftyBukkit.getPlugin().getServer().getPort();
	}

	public boolean isOnline() {
		return this.online;
	}

	void reset() {
		this.protocolVersion = -1;
		this.gameVersion = "";
		this.motd = "";
		this.online = false;
		this.maxPlayers = -1;
		this.playerList.clear();
	}

	void setGameVersion(String gameVersion) {
		this.gameVersion = gameVersion;
	}

	void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	void setMotd(String motd) {
		this.motd = motd;
	}

	void setOnline(boolean online) {
		this.online = online;
	}

	void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

}