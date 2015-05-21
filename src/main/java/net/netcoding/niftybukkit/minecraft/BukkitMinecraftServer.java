package net.netcoding.niftybukkit.minecraft;

import java.net.InetSocketAddress;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftycore.minecraft.MinecraftServer;
import net.netcoding.niftycore.mojang.MojangProfile;

public abstract class BukkitMinecraftServer extends MinecraftServer<MojangProfile> {

	private static final InetSocketAddress serverAddress = new InetSocketAddress(NiftyBukkit.getPlugin().getServer().getIp(), NiftyBukkit.getPlugin().getServer().getPort());

	protected BukkitMinecraftServer() { }

	public final boolean isCurrentServer() {
		return serverAddress.getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress()) && serverAddress.getPort() == this.getAddress().getPort();
	}

}