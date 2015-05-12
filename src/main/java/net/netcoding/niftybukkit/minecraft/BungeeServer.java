package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;
import java.util.Set;

import net.netcoding.niftybukkit.minecraft_new.BukkitMinecraftServer;
import net.netcoding.niftycore.minecraft.MinecraftVersion;
import net.netcoding.niftycore.mojang.MojangProfile;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;

public class BungeeServer extends BukkitMinecraftServer {

	boolean loadedOnce = false;
	final ConcurrentSet<MojangProfile> playersLeft = new ConcurrentSet<>();

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	Set<MojangProfile> getUnsafePlayerList() {
		return this.playerList;
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
		this.version = new MinecraftVersion(name, protocol);
	}

}