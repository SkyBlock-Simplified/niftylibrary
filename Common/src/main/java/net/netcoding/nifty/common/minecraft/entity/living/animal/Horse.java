package net.netcoding.nifty.common.minecraft.entity.living.animal;

import net.netcoding.nifty.common.minecraft.entity.Tameable;
import net.netcoding.nifty.common.minecraft.entity.vehicle.Vehicle;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.inventory.type.HorseInventory;

/**
 * Represents a Horse.
 */
public interface Horse extends Animal, InventoryHolder, Tameable, Vehicle {

	/**
	 * Gets the horse's color.
	 * <p>
	 * Colors only apply to horses, not to donkeys, mules, skeleton horses or undead horses.
	 *
	 * @return A {@link Color} representing the horse's group.
	 */
	Color getColor();

	/**
	 * Gets the domestication level of this horse.
	 * <p>
	 * A higher domestication level indicates that the horse is closer to
	 * becoming tame. As the domestication level gets closer to the max
	 * domestication level, the chance of the horse becoming tame increases.
	 *
	 * @return The domestication level.
	 */
	int getDomestication();

	/**
	 * Gets the jump strength of this horse.
	 * <p>
	 * Jump strength defines how high the horse can jump. A higher jump strength
	 * increases how high a jump will go.
	 *
	 * @return The horse's jump strength.
	 */
	double getJumpStrength();

	@Override
	HorseInventory getInventory();

	/**
	 * Gets the maximum domestication level of this horse.
	 * <p>
	 * The higher this level is, the longer it will likely take for the horse to be tamed.
	 *
	 * @return The max domestication level.
	 */
	int getMaxDomestication();

	/**
	 * Gets the horse's style. (Styles determine what kind of markings or patterns a horse has.)
	 * <p>
	 * Styles only apply to horses, not to donkeys, mules, skeleton horses or undead horses.
	 *
	 * @return A {@link Style} representing the horse's style.
	 */
	Style getStyle();

	/**
	 * Gets the horse's variant.
	 * <p>
	 * A horse's variant defines its physical appearance and capabilities.
	 * Whether a horse is a regular horse, donkey, mule, or other kind of
	 * horse is determined using the variant.
	 *
	 * @return A {@link Variant} representing the horse's variant.
	 */
	Variant getVariant();

	/**
	 * Gets whether the horse has a chest equipped.
	 *
	 * @return True if the horse has chest storage.
	 */
	boolean isCarryingChest();

	/**
	 * Sets whether the horse has a chest equipped.
	 * Removing a chest will also clear the chest's inventory.
	 *
	 * @param chest true if the horse should have a chest
	 */
	void setCarryingChest(boolean chest);

	/**
	 * Sets the domestication level of this horse.
	 * <p>
	 * Setting the domestication level to a high value will increase the horse's chances of becoming tame.
	 * <p>
	 * Domestication level must be greater than zero and no greater than
	 * the max domestication level of the horse, determined with {@link #getMaxDomestication()}
	 *
	 * @param level The domestication level.
	 */
	void setDomestication(int level);

	/**
	 * Sets the horse's color.
	 * <p>
	 * Attempting to set a color for any donkey, mule, skeleton horse or undead horse will not result in a change.
	 *
	 * @param color A {@link Color} for this horse.
	 */
	void setColor(Color color);

	/**
	 * Sets the jump strength of this horse.
	 * <p>
	 * A higher jump strength increases how high a jump will go.
	 * Setting a jump strength to 0 will result in no jump.
	 * You cannot set a jump strength to a value below 0 or above 2.
	 *
	 * @param strength The jump strength for this horse.
	 */
	void setJumpStrength(double strength);

	/**
	 * Sets the maximum domestication level of this horse.
	 * <p>
	 * Setting a higher max domestication will increase the amount of
	 * domesticating (feeding, riding, etc.) necessary in order to tame it,
	 * while setting a lower max value will have the opposite effect.
	 * <p>
	 * Maximum domestication must be greater than zero.
	 *
	 * @param level The max domestication level.
	 */
	void setMaxDomestication(int level);

	/**
	 * Sets the style of this horse.
	 * Styles determine what kind of markings or patterns a horse has.
	 * <p>
	 * Attempting to set a style for any donkey, mule, skeleton horse or undead horse will not result in a change.
	 *
	 * @param style A {@link Style} for this horse.
	 */
	void setStyle(Style style);

	/**
	 * Sets the horse's variant.
	 * <p>
	 * A horse's variant defines its physical appearance and capabilities.
	 * Whether a horse is a regular horse, donkey, mule, or other kind of
	 * horse can be set using the variant.
	 * <p>
	 * Setting a horse's variant does not change its attributes such as
	 * its owner and its tamed status, but changing a mule or donkey
	 * with a chest to another variant which does not support a chest
	 * will remove the chest and its contents.
	 *
	 * @param variant A {@link Variant} for this horse.
	 */
	void setVariant(Variant variant);

	/**
	 * Represents the base color that the horse has.
	 */
	enum Color {

		/**
		 * Snow white
		 */
		WHITE,
		/**
		 * Very light brown
		 */
		CREAMY,
		/**
		 * Chestnut
		 */
		CHESTNUT,
		/**
		 * Light brown
		 */
		BROWN,
		/**
		 * Pitch black
		 */
		BLACK,
		/**
		 * Gray
		 */
		GRAY,
		/**
		 * Dark brown
		 */
		DARK_BROWN

	}

	/**
	 * Represents the style, or markings, that the horse has.
	 */
	enum Style {

		/**
		 * No markings
		 */
		NONE,
		/**
		 * White socks or stripes
		 */
		WHITE,
		/**
		 * Milky splotches
		 */
		WHITEFIELD,
		/**
		 * Round white dots
		 */
		WHITE_DOTS,
		/**
		 * Small black dots
		 */
		BLACK_DOTS

	}

	/**
	 * Represents the different types of horses that may exist.
	 */
	enum Variant {

		/**
		 * A normal horse
		 */
		HORSE,
		/**
		 * A donkey
		 */
		DONKEY,
		/**
		 * A mule
		 */
		MULE,
		/**
		 * An undead horse
		 */
		UNDEAD_HORSE,
		/**
		 * A skeleton horse
		 */
		SKELETON_HORSE

	}

}