package net.netcoding.nifty.common.minecraft.inventory.item;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.api.color.Color;

public interface ItemFactory {

	ItemMeta getItemMeta(Material material);

	boolean isApplicable(ItemMeta meta, ItemStack item);

	boolean isApplicable(ItemMeta meta, Material material);

	boolean equals(ItemMeta meta1, ItemMeta meta2);

	ItemMeta asMetaFor(ItemMeta meta, ItemStack item);

	ItemMeta asMetaFor(ItemMeta meta, Material material);

	static Color getDefaultLeatherColor() {
		return Color.fromRGB(10511680);
	}

}