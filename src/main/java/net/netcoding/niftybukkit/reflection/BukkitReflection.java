package net.netcoding.niftybukkit.reflection;

import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.StringUtil;

public class BukkitReflection extends Reflection {

	public BukkitReflection(String className, MinecraftPackage minecraftPackage) {
		this(className, "", minecraftPackage);
	}

	public BukkitReflection(String className, String subPackage, MinecraftPackage minecraftPackage) {
		super(className, subPackage, minecraftPackage.toString());
	}

	public static Reflection getComatibleReflection(String className, String classEnum) {
		return new Reflection(StringUtil.format("{0}{1}", (MinecraftPackage.IS_PRE_1_8_3 ? "" : StringUtil.format("{0}$", className)), classEnum), MinecraftPackage.MINECRAFT_SERVER);
	}

}