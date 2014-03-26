package net.netcoding.niftybukkit.reflection;

import org.bukkit.Bukkit;

public enum MinecraftPackage {
	MINECRAFT_SERVER {
		@Override
		public String toString() {
			return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23, 30);
		}
	},
	CRAFTBUKKIT {
		@Override
		public String toString() {
			return Bukkit.getServer().getClass().getPackage().getName();
		}
	};
}