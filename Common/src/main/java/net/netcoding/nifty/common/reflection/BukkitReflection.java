package net.netcoding.nifty.common.reflection;

import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;

public final class BukkitReflection extends Reflection {

	public static final Reflection NMS_INVENTORY = getCompatibleForgeReflection((MinecraftProtocol.isForge() ? "InventoryPlayer" : "IInventory"), MinecraftPackage.MINECRAFT_SERVER, "entity.player");
	public static final Reflection NMS_ENTITY_PLAYER = getCompatibleForgeReflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER, "entity.player");

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
	public static BukkitReflection getCompatibleReflection(String className, String classEnum) {
		return new BukkitReflection(StringUtil.format("{0}{1}", (MinecraftProtocol.isPre1_8_3() ? "" : StringUtil.format("{0}$", className)), classEnum), MinecraftPackage.MINECRAFT_SERVER);
	}

	/**
	 * Gets a class that will attempt to be compatible with Forge.
	 *
	 * @param className Class name to lookup.
	 * @param packagePath Package path to lookup.
	 * @param subPackages Sub-package for optional readability.
	 * @return Reflection class to manipulate classes.
	 */
	public static BukkitReflection getCompatibleForgeReflection(String className, String packagePath, String... subPackages) {
		if (MinecraftProtocol.isForge()) {
			String forgeNms = StringUtil.implode(".", StringUtil.split("\\.", MinecraftPackage.MINECRAFT_SERVER), 0, 2);
			String forgePackagePath = (MinecraftPackage.MINECRAFT_SERVER.equals(packagePath) ? forgeNms : packagePath);

			for (String subPackage : subPackages) {
				BukkitReflection reflection = new BukkitReflection(className, subPackage, forgePackagePath);

				try {
					reflection.getClazz();
					return reflection;
				} catch (Exception ignore) { }
			}
		}

		return new BukkitReflection(className, packagePath);
	}

}