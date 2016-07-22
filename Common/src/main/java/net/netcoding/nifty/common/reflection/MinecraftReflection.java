package net.netcoding.nifty.common.reflection;

import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;

/**
 * Allows for assisted access to hidden minecraft fields, methods and classes.
 */
public final class MinecraftReflection extends Reflection {

	public static final Reflection INVENTORY = getCompatibleForgeReflection((MinecraftProtocol.isForge() ? "InventoryPlayer" : "IInventory"), MinecraftPackage.MINECRAFT_SERVER, "entity.player");
	public static final Reflection ENTITY = getCompatibleForgeReflection("Entity", MinecraftPackage.MINECRAFT_SERVER, "entity");
	public static final Reflection ENTITY_PLAYER = getCompatibleForgeReflection("EntityPlayer", MinecraftPackage.MINECRAFT_SERVER, "entity.player");
	public static final Reflection PLAYER_CONNECTION = getCompatibleForgeReflection("PlayerConnection", MinecraftPackage.MINECRAFT_SERVER);
	public static final Reflection NETWORK_MANAGER = new Reflection("NetworkManager", MinecraftPackage.MINECRAFT_SERVER);
	public static final Reflection CUSTOM_PAYLOD = new Reflection("PacketPlayOutCustomPayload", MinecraftPackage.MINECRAFT_SERVER);
	public static final Reflection DATA_SERIALIZER = new Reflection("PacketDataSerializer", MinecraftPackage.MINECRAFT_SERVER);

	/**
	 * Creates a new reflection instance of {@literal clazz}.
	 *
	 * @param clazz The class to reflect.
	 */
	public MinecraftReflection(Class<?> clazz) {
		this(clazz.getSimpleName(), clazz.getPackage().getName());
	}

	/**
	 * Creates a new reflection instance of {@literal packagePath}.{@literal className}.
	 *
	 * @param className The class name to reflect.
	 * @param packagePath The package the {@literal className} belongs to.
	 */
	public MinecraftReflection(String className, String packagePath) {
		this(className, "", packagePath);
	}

	/**
	 * Creates a new reflection instance of {@literal packagePath}.{@literal subPackage}.{@literal className}.
	 *
	 * @param className The class name to reflect.
	 * @param subPackage The sub package the {@literal className} belongs to.
	 * @param packagePath The package the {@literal className} belongs to.
	 */
	public MinecraftReflection(String className, String subPackage, String packagePath) {
		super(className, subPackage, packagePath);
	}

	/**
	 * Gets a class that was moved post 1.8.3.
	 *
	 * @param className Class name that exists both pre and post 1.8.3.
	 * @param classEnum Class name that moved post 1.8.3.
	 * @return Fixed class path to support pre and post 1.8.3.
	 */
	public static MinecraftReflection getCompatibleReflection(String className, String classEnum) {
		return new MinecraftReflection(StringUtil.format("{0}{1}", (MinecraftProtocol.isPre1_8_3() ? "" : StringUtil.format("{0}$", className)), classEnum), MinecraftPackage.MINECRAFT_SERVER);
	}

	/**
	 * Gets a class that will attempt to be compatible with Forge.
	 *
	 * @param className Class name to lookup.
	 * @param packagePath Package path to lookup.
	 * @param subPackages Sub-package for optional readability.
	 * @return Reflection class to manipulate classes.
	 */
	public static MinecraftReflection getCompatibleForgeReflection(String className, String packagePath, String... subPackages) {
		if (MinecraftProtocol.isForge()) {
			String forgeNms = StringUtil.implode(".", StringUtil.split("\\.", MinecraftPackage.MINECRAFT_SERVER), 0, 2);
			String forgePackagePath = (MinecraftPackage.MINECRAFT_SERVER.equals(packagePath) ? forgeNms : packagePath);

			for (String subPackage : subPackages) {
				MinecraftReflection reflection = new MinecraftReflection(className, subPackage, forgePackagePath);

				try {
					reflection.getClazz();
					return reflection;
				} catch (Exception ignore) { }
			}
		}

		return new MinecraftReflection(className, packagePath);
	}

}