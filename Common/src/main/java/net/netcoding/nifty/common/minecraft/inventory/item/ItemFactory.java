package net.netcoding.nifty.common.minecraft.inventory.item;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.api.color.Color;

public interface ItemFactory {

	default ItemMeta asMetaFor(ItemMeta meta, ItemStack item) {
		return this.asMetaFor(meta, item.getType());
	}

	ItemMeta asMetaFor(ItemMeta meta, Material material);

	boolean equals(ItemMeta meta1, ItemMeta meta2);

	static Color getDefaultLeatherColor() {
		return Color.fromRGB(10511680);
	}

	ItemMeta getItemMeta(Material material);

	default boolean isApplicable(ItemMeta meta, ItemStack item) {
		return this.isApplicable(meta, item.getType());
	}

	boolean isApplicable(ItemMeta meta, Material material);

}