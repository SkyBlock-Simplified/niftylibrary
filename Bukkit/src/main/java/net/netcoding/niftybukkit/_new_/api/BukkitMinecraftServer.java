package net.netcoding.niftybukkit._new_.api;

import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.api.MinecraftServer;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;

public abstract class BukkitMinecraftServer<T extends BukkitMojangProfile> extends MinecraftServer<T> {

	private static final InetSocketAddress serverAddress = new InetSocketAddress(Bukkit.getIp(), Bukkit.getPort());

	protected BukkitMinecraftServer() { }

	public final boolean isCurrentServer() {
		return serverAddress.getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress()) && serverAddress.getPort() == this.getAddress().getPort();
	}

}