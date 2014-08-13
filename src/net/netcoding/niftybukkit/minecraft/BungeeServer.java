package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;
import java.util.Set;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

public class BungeeServer extends MinecraftServer {

	boolean loadedOnce = false;
	Set<MojangProfile> tempList = new ConcurrentSet<>();

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	void setAddress(String ip, int port) {
		this.setAddress(new InetSocketAddress(ip, port));
	}

	void setAddress(InetSocketAddress address) {
		this.address = address;
	}

}