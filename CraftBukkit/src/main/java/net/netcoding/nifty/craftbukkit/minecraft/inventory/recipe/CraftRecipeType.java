package net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe;

import org.bukkit.inventory.Recipe;

public enum CraftRecipeType {

	FURNACE(CraftFurnaceRecipe.class, org.bukkit.inventory.FurnaceRecipe.class),
	MERCHANT(CraftMerchantRecipe.class, org.bukkit.inventory.MerchantRecipe.class),
	SHAPED(CraftShapedRecipe.class, org.bukkit.inventory.ShapedRecipe.class),
	SHAPELESS(CraftShapelessRecipe.class, org.bukkit.inventory.ShapelessRecipe.class),
	DEFAULT(CraftRecipe.class, org.bukkit.inventory.Recipe.class);

	private final Class<? extends CraftRecipe> clazz;
	private final Class<? extends org.bukkit.inventory.Recipe> bukkitClazz;

	CraftRecipeType(Class<? extends CraftRecipe> clazz, Class<? extends org.bukkit.inventory.Recipe> bukkitClazz) {
		this.clazz = clazz;
		this.bukkitClazz = bukkitClazz;
	}

	public Class<? extends CraftRecipe> getClazz() {
		return this.clazz;
	}

	public Class<? extends Recipe> getBukkitClazz() {
		return this.bukkitClazz;
	}

	public static CraftRecipeType getByBukkitClass(Class<? extends org.bukkit.inventory.Recipe> bukkitClazz) {
		for (CraftRecipeType type : values()) {
			if (type.getBukkitClazz().equals(bukkitClazz))
				return type;
		}

		for (CraftRecipeType type : values()) {
			if (type.getBukkitClazz().isAssignableFrom(bukkitClazz))
				return type;
		}

		return DEFAULT;
	}

}