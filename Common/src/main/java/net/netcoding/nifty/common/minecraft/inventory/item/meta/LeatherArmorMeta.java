package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemFactory;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.api.Color;

/**
 * Represents leather armor ({@link Material#LEATHER_BOOTS}, {@link
 * Material#LEATHER_CHESTPLATE}, {@link Material#LEATHER_HELMET}, or {@link
 * Material#LEATHER_LEGGINGS}) that can be colored.
 */
public interface LeatherArmorMeta extends ItemMeta {

	/**
	 * Gets the color of the armor. If it has not been set otherwise, it will
	 * be {@link ItemFactory#getDefaultLeatherColor()}.
	 *
	 * @return the color of the armor, never null
	 */
	Color getColor();

	/**
	 * Sets the color of the armor.
	 *
	 * @param color the color to set. Setting it to null is equivalent to
	 *     setting it to {@link ItemFactory#getDefaultLeatherColor()}.
	 */
	void setColor(Color color);

	LeatherArmorMeta clone();
}