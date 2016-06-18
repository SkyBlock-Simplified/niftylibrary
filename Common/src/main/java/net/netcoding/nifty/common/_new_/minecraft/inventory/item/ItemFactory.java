package net.netcoding.nifty.common._new_.minecraft.inventory.item;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common._new_.minecraft.material.Material;
import org.bukkit.Color;

public interface ItemFactory {

	ItemMeta getItemMeta(Material material);

	boolean isApplicable(ItemMeta itemMeta, ItemStack itemStack);

	boolean isApplicable(ItemMeta itemMeta, Material material);

	boolean equals(ItemMeta itemMeta, ItemMeta itemMeta2);

	ItemMeta asMetaFor(ItemMeta itemMeta, ItemStack itemStack);

	ItemMeta asMetaFor(ItemMeta itemMeta, Material material);

	default Color getDefaultLeatherColor() {
		return Color.fromRGB(10511680);
	}

}