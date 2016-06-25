package net.netcoding.nifty.common._new_.api;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.core.api.MinecraftServer;

import java.net.InetSocketAddress;

public abstract class BukkitMinecraftServer<T extends BukkitMojangProfile> extends MinecraftServer<T> {

	private static final InetSocketAddress serverAddress = Nifty.getServer().getAddress();

	protected BukkitMinecraftServer() { }

	public final boolean isCurrentServer() {
		return serverAddress.getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress()) && serverAddress.getPort() == this.getAddress().getPort();
	}

}