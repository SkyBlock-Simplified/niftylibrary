package net.netcoding.nifty.common.minecraft.block;

import com.google.common.collect.ImmutableMap;
import net.netcoding.nifty.core.api.DyeColor;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.misc.Serializable;

import java.util.List;
import java.util.Map;

public interface Banner {

	/**
	 * Returns the base color for this banner.
	 *
	 * @return The base color.
	 */
	DyeColor getBaseColor();

	/**
	 * Sets the base color for this banner.
	 *
	 * @param color The base color.
	 */
	void setBaseColor(DyeColor color);

	/**
	 * Returns a list of patterns on this banner.
	 *
	 * @return The patterns.
	 */
	List<Pattern> getPatterns();

	/**
	 * Sets the patterns used on this banner.
	 *
	 * @param patterns The new list of patterns.
	 */
	void setPatterns(List<Pattern> patterns);

	/**
	 * Adds a new pattern on top of the existing patterns.
	 *
	 * @param pattern The new pattern to add
	 */
	void addPattern(Pattern pattern);

	/**
	 * Returns the pattern at the specified index.
	 *
	 * @param index The index.
	 * @return The pattern.
	 */
	default Pattern getPattern(int index) {
		return this.getPatterns().get(index);
	}

	/**
	 * Removes the pattern at the specified index.
	 *
	 * @param i The index.
	 * @return The removed pattern.
	 */
	Pattern removePattern(int index);

	/**
	 * Sets the pattern at the specified index.
	 *
	 * @param i The index.
	 * @param pattern The new pattern.
	 */
	void setPattern(int i, Pattern pattern);

	class Pattern implements Serializable {

		private final DyeColor color;
		private final PatternType pattern;

		public Pattern(DyeColor color, PatternType pattern) {
			this.color = color;
			this.pattern = pattern;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			else if (obj == this)
				return true;
			else if (!Pattern.class.isAssignableFrom(obj.getClass()))
				return false;
			else {
				Pattern other = (Pattern)obj;
				return this.getColor() == other.getColor() && this.getPattern() == other.getPattern();
			}
		}

		public static Pattern deserialize(Map<String, String> map) {
			return new Pattern(DyeColor.valueOf(map.get("color")), PatternType.getByIdentifier(map.get("pattern")));
		}

		/**
		 * Returns the color of the pattern
		 *
		 * @return the color of the pattern
		 */
		public DyeColor getColor() {
			return this.color;
		}

		/**
		 * Returns the type of pattern
		 *
		 * @return the pattern type
		 */
		public PatternType getPattern() {
			return this.pattern;
		}

		@Override
		public int hashCode() {
			int hash = 3;
			hash = 97 * hash + (this.getColor() != null ? this.getColor().hashCode() : 0);
			hash = 97 * hash + (this.getPattern() != null ? this.getPattern().hashCode() : 0);
			return hash;
		}

		@Override
		public Map<String, Object> serialize() {
			return ImmutableMap.of(
					"color", color.toString(),
					"pattern", pattern.getIdentifier()
			);
		}

	}

	enum PatternType {

		BASE("b"),
		SQUARE_BOTTOM_LEFT("bl"),
		SQUARE_BOTTOM_RIGHT("br"),
		SQUARE_TOP_LEFT("tl"),
		SQUARE_TOP_RIGHT("tr"),
		STRIPE_BOTTOM("bs"),
		STRIPE_TOP("ts"),
		STRIPE_LEFT("ls"),
		STRIPE_RIGHT("rs"),
		STRIPE_CENTER("cs"),
		STRIPE_MIDDLE("ms"),
		STRIPE_DOWNRIGHT("drs"),
		STRIPE_DOWNLEFT("dls"),
		STRIPE_SMALL("ss"),
		CROSS("cr"),
		STRAIGHT_CROSS("sc"),
		TRIANGLE_BOTTOM("bt"),
		TRIANGLE_TOP("tt"),
		TRIANGLES_BOTTOM("bts"),
		TRIANGLES_TOP("tts"),
		DIAGONAL_LEFT("ld"),
		DIAGONAL_RIGHT("rd"),
		DIAGONAL_LEFT_MIRROR("lud"),
		DIAGONAL_RIGHT_MIRROR("rud"),
		CIRCLE_MIDDLE("mc"),
		RHOMBUS_MIDDLE("mr"),
		HALF_VERTICAL("vh"),
		HALF_HORIZONTAL("hh"),
		HALF_VERTICAL_MIRROR("vhr"),
		HALF_HORIZONTAL_MIRROR("hhb"),
		BORDER("bo"),
		CURLY_BORDER("cbo"),
		CREEPER("cre"),
		GRADIENT("gra"),
		GRADIENT_UP("gru"),
		BRICKS("bri"),
		SKULL("sku"),
		FLOWER("flo"),
		MOJANG("moj");

		private static final ConcurrentMap<String, PatternType> BY_STRING = Concurrent.newMap();
		private final String identifier;

		static {
			for (PatternType pattern : values())
				BY_STRING.put(pattern.getIdentifier(), pattern);
		}

		PatternType(String key) {
			this.identifier = key;
		}

		/**
		 * Returns the identifier used to represent
		 * this pattern type
		 *
		 * @return The pattern's identifier.
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * Returns the pattern type which matches the passed
		 * identifier or null if no matches are found
		 *
		 * @param identifier the identifier
		 * @return the matched pattern type or null
		 */
		public static PatternType getByIdentifier(String identifier) {
			return BY_STRING.get(identifier);
		}

	}

}