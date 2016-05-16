package net.netcoding.niftybukkit.reflection;

import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.Bukkit;

public class MinecraftPackage {

	public static final String CRAFTBUKKIT = Bukkit.getServer().getClass().getPackage().getName();
	private static final String MINECRAFT_VERSION = CRAFTBUKKIT.split("\\.")[3];
	public static final String MINECRAFT_SERVER = StringUtil.format("net.minecraft.server.{0}", MINECRAFT_VERSION);
	static final String MINECRAFT_VERSION_NUMBER = MINECRAFT_VERSION.replaceAll("^v", "").replaceAll("R", "").replace("_", ".");

}