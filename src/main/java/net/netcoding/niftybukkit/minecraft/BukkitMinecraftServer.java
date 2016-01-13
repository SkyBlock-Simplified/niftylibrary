package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftycore.minecraft.MinecraftServer;
import net.netcoding.niftycore.mojang.MojangProfile;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;

public abstract class BukkitMinecraftServer extends MinecraftServer<MojangProfile> {

	private static final InetSocketAddress serverAddress = new InetSocketAddress(Bukkit.getIp(), Bukkit.getPort());

	protected BukkitMinecraftServer() { }

	public final boolean isCurrentServer() {
		return serverAddress.getAddress().getHostAddress().equals(this.getAddress().getAddress().getHostAddress()) && serverAddress.getPort() == this.getAddress().getPort();
	}

}