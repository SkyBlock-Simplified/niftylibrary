package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.block.Banner;
import net.netcoding.nifty.core.api.color.DyeColor;

import java.util.List;

public interface BannerMeta extends ItemMeta {

	/**
	 * Adds a new pattern on top of the existing patterns.
	 *
	 * @param pattern The new pattern to add.
	 */
	void addPattern(Banner.Pattern pattern);

	@Override
	BannerMeta clone();

	/**
	 * Returns the base color for this banner.
	 *
	 * @return The base color.
	 */
	DyeColor getBaseColor();

	/**
	 * Returns the pattern at the specified index.
	 *
	 * @param index The index.
	 * @return The pattern.
	 */
	default Banner.Pattern getPattern(int index) {
		return this.getPatterns().get(index);
	}

	/**
	 * Returns a list of patterns on this banner.
	 *
	 * @return The patterns.
	 */
	List<Banner.Pattern> getPatterns();

	/**
	 * Removes the pattern at the specified index.
	 *
	 * @param index The index.
	 * @return The removed pattern.
	 */
	Banner.Pattern removePattern(int index);

	/**
	 * Sets the base color for this banner.
	 *
	 * @param color The base color.
	 */
	void setBaseColor(DyeColor color);

	/**
	 * Sets the pattern at the specified index.
	 *
	 * @param index The index.
	 * @param pattern The new pattern.
	 */
	void setPattern(int index, Banner.Pattern pattern);

	/**
	 * Sets the patterns used on this banner
	 *
	 * @param patterns The new list of patterns
	 */
	void setPatterns(List<Banner.Pattern> patterns);

}