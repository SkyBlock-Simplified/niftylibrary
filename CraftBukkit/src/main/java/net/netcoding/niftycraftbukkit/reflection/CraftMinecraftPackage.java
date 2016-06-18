package net.netcoding.niftycraftbukkit.reflection;

import net.netcoding.niftybukkit._new_.reflection.MinecraftPackage;
import org.bukkit.Bukkit;

public final class CraftMinecraftPackage extends MinecraftPackage {

	public static final String CRAFTBUKKIT = Bukkit.getServer().getClass().getPackage().getName();

}