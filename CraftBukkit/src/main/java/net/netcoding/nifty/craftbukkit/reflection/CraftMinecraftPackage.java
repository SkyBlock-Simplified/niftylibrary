package net.netcoding.nifty.craftbukkit.reflection;

import net.netcoding.nifty.common.reflection.MinecraftPackage;

import org.bukkit.Bukkit;

public final class CraftMinecraftPackage extends MinecraftPackage {

	public static final String CRAFTBUKKIT = Bukkit.getServer().getClass().getPackage().getName();

}