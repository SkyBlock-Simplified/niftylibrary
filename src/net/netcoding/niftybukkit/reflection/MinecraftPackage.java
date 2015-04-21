package net.netcoding.niftybukkit.reflection;

import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.VersionUtil;

import org.bukkit.Bukkit;

public class MinecraftPackage {

	public static final String CRAFTBUKKIT = Bukkit.getServer().getClass().getPackage().getName();
	public static final String MINECRAFT_VERSION = CRAFTBUKKIT.split("\\.")[3];
	public static final String MINECRAFT_SERVER = StringUtil.format("net.minecraft.server.{0}", MINECRAFT_VERSION);
	private static final String MINECRAFT_VERSION_NUMBER = MINECRAFT_VERSION.replaceAll("^v", "").replaceAll("R", "").replace("_", ".");
	public static final boolean IS_PRE_1_8 = new VersionUtil(MINECRAFT_VERSION_NUMBER).compareTo(new VersionUtil("1.8.0")) < 0;
	public static final boolean IS_PRE_1_8_3 = new VersionUtil(MINECRAFT_VERSION_NUMBER).compareTo(new VersionUtil("1.8.2")) < 0;
	public static final boolean IS_SPIGOT;

	static {
		boolean spigot = false;

		try {
			Reflection spigotPlayerRef = new Reflection("Spigot", "Player", "org.bukkit.entity");
			spigotPlayerRef.getClazz();
			spigot = true;
		} catch (Exception ex) { }

		IS_SPIGOT = spigot;
	}

}