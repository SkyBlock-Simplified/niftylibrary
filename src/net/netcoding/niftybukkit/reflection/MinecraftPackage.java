package net.netcoding.niftybukkit.reflection;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;

public enum MinecraftPackage {

	MINECRAFT_SERVER {
		@Override
		public String toString() {
			return StringUtil.format("net.minecraft.server.{0}", Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
		}
	},

	CRAFTBUKKIT {
		@Override
		public String toString() {
			return Bukkit.getServer().getClass().getPackage().getName();
		}
	};

}