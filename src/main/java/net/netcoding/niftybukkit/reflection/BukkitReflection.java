package net.netcoding.niftybukkit.reflection;

import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.StringUtil;

public class BukkitReflection extends Reflection {

	public BukkitReflection(Class<?> clazz) {
		this(clazz.getSimpleName(), clazz.getPackage().getName());
	}

	public BukkitReflection(String className, String packagePath) {
		this(className, "", packagePath);
	}

	public BukkitReflection(String className, String subPackage, String packagePath) {
		super(className, subPackage, packagePath);
	}

	public static Reflection getCompatibleReflection(String className, String classEnum) {
		return new Reflection(StringUtil.format("{0}{1}", (MinecraftPackage.IS_PRE_1_8_3 ? "" : StringUtil.format("{0}$", className)), classEnum), MinecraftPackage.MINECRAFT_SERVER);
	}

}