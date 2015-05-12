package net.netcoding.niftybukkit.minecraft_new;

import java.net.InetSocketAddress;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftycore.minecraft.MinecraftServer;

public abstract class BukkitMinecraftServer extends MinecraftServer {

	private static final InetSocketAddress serverAddress = new InetSocketAddress(NiftyBukkit.getPlugin().getServer().getIp(), NiftyBukkit.getPlugin().getServer().getPort());

	protected BukkitMinecraftServer() { }

	public final boolean isCurrentServer() {
		return serverAddress.getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress()) && serverAddress.getPort() == this.getAddress().getPort();
	}

}