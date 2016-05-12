package net.netcoding.niftybukkit.reflection;

import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.StringUtil;

public class BukkitReflection extends Reflection {

	public static final Reflection NMS_ENTITY_PLAYER = BukkitReflection.getCompatibleForgeReflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER, "entity.player");

	public BukkitReflection(Class<?> clazz) {
		this(clazz.getSimpleName(), clazz.getPackage().getName());
	}

	public BukkitReflection(String className, String packagePath) {
		this(className, "", packagePath);
	}

	public BukkitReflection(String className, String subPackage, String packagePath) {
		super(className, subPackage, packagePath);
	}

	/**
	 * Gets a class that was moved post 1.8.3.
	 *
	 * @param className Class name that exists both pre and post 1.8.3.
	 * @param classEnum Class name that moved post 1.8.3.
	 * @return Fixed class path to support pre and post 1.8.3.
	 */
	public static Reflection getCompatibleReflection(String className, String classEnum) {
		return new BukkitReflection(StringUtil.format("{0}{1}", (MinecraftPackage.IS_PRE_1_8_3 ? "" : StringUtil.format("{0}$", className)), classEnum), MinecraftPackage.MINECRAFT_SERVER);
	}

	public static Reflection getCompatibleForgeReflection(String className, String packagePath, String... subPackages) {
		if (MinecraftPackage.IS_FORGE) {
			String forgeNms = StringUtil.implode(".", StringUtil.split("\\.", MinecraftPackage.MINECRAFT_SERVER), 0, 2);
			String forgePackagePath = (MinecraftPackage.IS_FORGE && MinecraftPackage.MINECRAFT_SERVER.equals(packagePath) ? forgeNms : packagePath);

			for (String subPackage : subPackages) {
				Reflection reflection = new BukkitReflection(className, subPackage, forgePackagePath);

				try {
					reflection.getClazz();
					return reflection;
				} catch (Exception ignore) { }
			}
		}

		return new BukkitReflection(className, packagePath);
	}

}