package net.netcoding.nifty.common.minecraft;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.core.api.Color;
import net.netcoding.nifty.core.api.builder.BuilderCore;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import net.netcoding.nifty.core.util.misc.Serializable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a single firework effect.
 */
public final class FireworkEffect implements Serializable {

	/**
	 * Construct a firework effect.
	 *
	 * @return A utility object for building a firework effect
	 */
	public static Builder builder() {
		return Nifty.getBuilderManager().createBuilder(FireworkEffect.class);
	}

	private final boolean flicker;
	private final boolean trail;
	private final ConcurrentSet<Color> colors = Concurrent.newSet();
	private final ConcurrentSet<Color> fadeColors = Concurrent.newSet();
	private final Type type;

	public FireworkEffect(boolean flicker, boolean trail, Set<Color> colors, Set<Color> fadeColors, Type type) {
		if (ListUtil.isEmpty(colors))
			throw new IllegalStateException("Cannot make FireworkEffect without any color");

		this.flicker = flicker;
		this.trail = trail;
		this.colors.addAll(colors);
		this.fadeColors.addAll(colors);
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public static FireworkEffect deserialize(Map<String, Object> map) {
		String typeName = (String)map.get("type");
		Type type = Type.valueOf(typeName);

		if (type == null)
			throw new IllegalArgumentException(StringUtil.format("Invalid type with name ''{0}''!", typeName));

		Builder builder = builder().flicker((boolean)map.get("flicker")).trail((boolean)map.get("trail"));
		List<Integer> colors = (List<Integer>)map.get("colors");
		List<Integer> fadeColors = (List<Integer>)map.get("fade-colors");

		for (Integer color : colors)
			builder.withColor(Color.fromRGB(color));

		for (Integer fadeColor : fadeColors)
			builder.withFade(Color.fromRGB(fadeColor));

		return builder.with(type).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj == null)
			return false;
		else if (!FireworkEffect.class.isAssignableFrom(obj.getClass()))
			return false;
		else {
			FireworkEffect other = (FireworkEffect)obj;
			return this.flicker == other.flicker && this.trail == other.trail && this.type == other.type && this.colors.equals(other.colors) && this.fadeColors.equals(other.fadeColors);
		}
	}

	/**
	 * Get whether the firework effect flickers.
	 *
	 * @return True if it flickers.
	 */
	public boolean hasFlicker() {
		return this.flicker;
	}

	/**
	 * Get whether the firework effect has a trail.
	 *
	 * @return True if it has a trail.
	 */
	public boolean hasTrail() {
		return this.trail;
	}

	/**
	 * Get the primary colors of the firework effect.
	 *
	 * @return A list of the primary colors.
	 */
	public Set<Color> getColors() {
		return Collections.unmodifiableSet(this.colors);
	}

	/**
	 * Get the fade colors of the firework effect.
	 *
	 * @return A list of the fade colors.
	 */
	public Set<Color> getFadeColors() {
		return Collections.unmodifiableSet(this.fadeColors);
	}

	/**
	 * Get the type of the firework effect.
	 *
	 * @return The effect type.
	 */
	public Type getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		int PRIME = 31, TRUE = Boolean.TRUE.hashCode(), FALSE = Boolean.FALSE.hashCode();
		int hash = 1;
		hash = hash * PRIME + (flicker ? TRUE : FALSE);
		hash = hash * PRIME + (trail ? TRUE : FALSE);
		hash = hash * PRIME + type.hashCode();
		hash = hash * PRIME + colors.hashCode();
		hash = hash * PRIME + fadeColors.hashCode();
		return hash;
	}

	@Override
	public Map<String, Object> serialize() {
		return ImmutableMap.of(
				"flicker", this.hasFlicker(),
				"trail", this.hasTrail(),
				"colors", this.getColors().stream().map(Color::asRGB).collect(Collectors.toList()),
				"fade-colors", this.getFadeColors().stream().map(Color::asRGB).collect(Collectors.toList()),
				"type", this.getType().name()
		);
	}

	/**
	 * This is a builder for FireworkEffects.
	 *
	 * @see FireworkEffect#builder()
	 */
	public static final class Builder implements BuilderCore<FireworkEffect> {

		private final ConcurrentSet<Color> colors = Concurrent.newSet();
		private final ConcurrentSet<Color> fadeColors = Concurrent.newSet();
		boolean flicker = false;
		boolean trail = false;
		Type type = Type.BALL;

		Builder() {}

		/**
		 * Set whether the firework effect should flicker.
		 *
		 * @param flicker True if it should flicker.
		 */
		public Builder flicker(boolean flicker) {
			this.flicker = flicker;
			return this;
		}

		/**
		 * Set whether the firework effect should have a trail.
		 *
		 * @param trail true if it should have a trail, false for no trail
		 * @return This object, for chaining
		 */
		public Builder trail(boolean trail) {
			this.trail = trail;
			return this;
		}


		/**
		 * Specify the type of the firework effect.
		 *
		 * @param type The effect type.
		 */
		public Builder with(Type type) {
			Preconditions.checkArgument(type != null, "Type cannot be NULL!");
			this.type = type;
			return this;
		}

		/**
		 * Add primary colors to the firework effect.
		 *
		 * @param colors The colors to add.
		 */
		public Builder withColor(Color... colors) {
			ListUtil.noNullElements(colors, "Colors cannot be/contain NULL!");
			Collections.addAll(this.colors, colors);
			return this;
		}

		/**
		 * Add several primary colors to the firework effect.
		 *
		 * @param colors A collection containing the desired colors.
		 */
		public Builder withColor(Collection<? extends Color> colors) {
			ListUtil.noNullElements(colors, "Colors cannot be/contain NULL!");
			this.colors.addAll(colors);
			return this;
		}

		/**
		 * Add fade colors to the firework effect.
		 *
		 * @param colors The colors to add.
		 */
		public Builder withFade(Color... colors) {
			ListUtil.noNullElements(colors, "Colors have be/contain NULL!");
			Collections.addAll(this.fadeColors, colors);
			return this;
		}

		/**
		 * Add several fade colors to the firework effect.
		 *
		 * @param colors A collection containing the desired colors.
		 */
		public Builder withFade(Collection<? extends Color> colors) {
			ListUtil.noNullElements(colors, "Colors have be/contain NULL!");
			this.fadeColors.addAll(colors);
			return this;
		}

		/**
		 * Add a flicker to the firework effect.
		 */
		public Builder withFlicker() {
			this.flicker = true;
			return this;
		}

		/**
		 * Add a trail to the firework effect.
		 */
		public Builder withTrail() {
			this.trail = true;
			return this;
		}

		/**
		 * Create a {@link FireworkEffect} from the current contents of this builder.
		 *
		 * @return The representative firework effect.
		 */
		@Override
		public FireworkEffect build() {
			if (ListUtil.isEmpty(this.colors))
				this.colors.add(Color.WHITE);

			return new FireworkEffect(this.flicker, this.trail, this.colors, this.fadeColors, this.type);
		}

	}


	/**
	 * The type or shape of the effect.
	 */
	public enum Type {

		/**
		 * A small ball effect.
		 */
		BALL,
		/**
		 * A large ball effect.
		 */
		BALL_LARGE,
		/**
		 * A star-shaped effect.
		 */
		STAR,
		/**
		 * A burst effect.
		 */
		BURST,
		/**
		 * A creeper-face effect.
		 */
		CREEPER

	}

}