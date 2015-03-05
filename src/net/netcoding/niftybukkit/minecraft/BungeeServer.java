package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;

public class BungeeServer extends MinecraftServer {

	boolean loadedOnce = false;

	BungeeServer(String serverName) {
		this.serverName = serverName;
	}

	void setAddress(String ip, int port) {
		this.address = InetSocketAddress.createUnresolved(ip, port);
	}

}