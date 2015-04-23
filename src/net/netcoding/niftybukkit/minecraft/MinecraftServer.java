package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

abstract class MinecraftServer {

	private static final InetSocketAddress serverAddress = new InetSocketAddress(NiftyBukkit.getPlugin().getServer().getIp(), NiftyBukkit.getPlugin().getServer().getPort());
	protected InetSocketAddress address;
	private int maxPlayers = 0;
	private String motd = "";
	protected boolean online = false;
	protected final ConcurrentSet<MojangProfile> playerList = new ConcurrentSet<>();
	protected String serverName = "";
	private Version version = Version.DEFAULT;

	MinecraftServer() { }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof MinecraftServer)) return false;
		MinecraftServer server = (MinecraftServer)obj;
		if (!server.getAddress().getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress())) return false;
		if (server.getAddress().getPort() != this.getAddress().getPort()) return false;
		return true;
	}

	public InetSocketAddress getAddress() {
		return this.address;
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

	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		return 31 * (address == null ? super.hashCode() : address.hashCode());
	}

	public final boolean isCurrentServer() {
		return serverAddress.getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress()) && serverAddress.getPort() == this.getAddress().getPort();
	}

	public boolean isOnline() {
		return this.online;
	}

	void reset() {
		this.motd = "";
		this.maxPlayers = 0;
		this.playerList.clear();
		this.version = Version.DEFAULT;
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

	void setVersion(String name, int protocol) {
		this.version = new Version(name, protocol);
	}

	public static class Version {

		static final Version DEFAULT = new Version("", 0);
		private final String name;
		private final int protocol;

		Version(String name, int protocol) {
			this.name = name;
			this.protocol = protocol;
		}

		public String getName() {
			return this.name;
		}

		public int getProtocol() {
			return this.protocol;
		}

	}

}