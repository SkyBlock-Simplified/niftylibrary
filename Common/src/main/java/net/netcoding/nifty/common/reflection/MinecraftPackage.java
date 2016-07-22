package net.netcoding.nifty.common.reflection;

import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;

/**
 * Holds packages essential to minecraft reflection.
 */
public abstract class MinecraftPackage {

	/**
	 * The minecraft NMS package {@literal net.minecraft.*}.
	 */
	public static final String MINECRAFT_SERVER;

	static {
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