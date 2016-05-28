package net.netcoding.niftybukkit.minecraft.messages;

import net.netcoding.niftybukkit.minecraft.BukkitMinecraftServer;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;

import java.net.InetSocketAddress;
import java.util.Set;

public class BungeeServer extends BukkitMinecraftServer<BukkitMojangProfile> {

	boolean loadedOnce = false;
	final ConcurrentSet<BukkitMojangProfile> playersLeft = new ConcurrentSet<>();

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	Set<BukkitMojangProfile> getUnsafePlayerList() {
		return this.playerList;
	}

	Set<BukkitMojangProfile> getTotalPlayerList() {
		ConcurrentSet<BukkitMojangProfile> playerList = new ConcurrentSet<>(super.getPlayerList());
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

	protected void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	protected void setMotd(String motd) {
		this.motd = motd;
	}

	protected void setOnline(boolean online) {
		this.online = online;
	}

	protected void setVersion(String name, int protocol) {
		this.version = new Version(name, protocol);
	}

}