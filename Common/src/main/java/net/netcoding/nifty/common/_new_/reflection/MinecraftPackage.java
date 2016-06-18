package net.netcoding.nifty.common._new_.reflection;

import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.StringUtil;

public abstract class MinecraftPackage {

	public static final String MINECRAFT_SERVER;

	static {
		// TODO: Remove CRAFTBUKKIT, Determine MINECRAFT_VERSION, Adjust MINECRAFT_SERVER if Forge
		String minecraftServer;

		if (MinecraftProtocol.isForge())
			minecraftServer = "net.minecraft";
		else {
			String bukkitPackage = new Reflection("Bukkit", "org.bukkit").invokeMethod("getServer", null).getClass().getPackage().getName();
			String minecraftVersion = bukkitPackage.split("\\.")[3];
			minecraftServer = StringUtil.format("net.minecraft.server.{0}", minecraftVersion);
		}

		MINECRAFT_SERVER = minecraftServer;
	}

}