package net.netcoding.nifty.common.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.core.util.misc.Vector;

public interface ArmorStand extends LivingEntity {

	/**
	 * Returns the armor stand's body's current pose.
	 *
	 * @return The current pose.
	 */
	Vector getBodyPose();

	/**
	 * Returns the item currently being worn by the armor stand on its feet.
	 *
	 * @return The worn item.
	 */
	ItemStack getBoots();

	/**
	 * Returns the item currently being worn by the armor stand on its chest.
	 *
	 * @return The worn item.
	 */
	ItemStack getChestplate();

	/**
	 * Returns the armor stand's head's current pose.
	 *
	 * @return The current pose.
	 */
	Vector getHeadPose();

	/**
	 * Returns the item currently being worn by the armor stand on its head.
	 *
	 * @return The worn item.
	 */
	ItemStack getHelmet();

	/**
	 * Returns the item the armor stand is currently holding.
	 *
	 * @return the held item.
	 */
	ItemStack getItemInHand();

	/**
	 * Returns the armor stand's left arm's current pose.
	 *
	 * @return The current pose.
	 */
	Vector getLeftArmPose();

	/**
	 * Returns the armor stand's left leg's current pose.
	 *
	 * @return The current pose.
	 */
	Vector getLeftLegPose();

	/**
	 * Returns the item currently being worn by the armor stand on its legs.
	 *
	 * @return The worn item.
	 */
	ItemStack getLeggings();

	/**
	 * Returns the armor stand's right arm's current pose.
	 *
	 * @return The current pose.
	 */
	Vector getRightArmPose();

	/**
	 * Returns the armor stand's right leg's current pose.
	 *
	 * @return The current pose.
	 */
	Vector getRightLegPose();

	/**
	 * Returns whether this armor stand has arms.
	 *
	 * @return Whether this has arms or not.
	 */
	boolean hasArms();

	/**
	 * Returns whether the armor stand has a base plate.
	 *
	 * @return Whether it has a base plate.
	 */
	boolean hasBasePlate();

	/**
	 * Returns whether this armor stand is a marker, meaning it has a very small collision box.
	 *
	 * @return Whether this is a marker.
	 */
	boolean isMarker();

	/**
	 * Returns whether this armor stand is scaled down.
	 *
	 * @return Whether this is scaled down.
	 */
	boolean isSmall();

	/**
	 * Returns whether the armor stand should be visible or not.
	 *
	 * @return Whether the stand is visible or not.
	 */
	boolean isVisible();

	/**
	 * Sets whether this armor stand has arms.
	 *
	 * @param arms Whether this has arms or not.
	 */
	void setArms(boolean arms);

	/**
	 * Sets the armor stand's body's current pose.
	 *
	 * @param pose The current pose.
	 */
	void setBodyPose(Vector pose);

	/**
	 * Sets whether the armor stand has a base plate.
	 *
	 * @param basePlate Whether is has a base plate.
	 */
	void setBasePlate(boolean basePlate);

	/**
	 * Sets the item currently being worn by the armor stand on its feet.
	 *
	 * @param item the item to wear.
	 */
	void setBoots(ItemStack item);

	/**
	 * Sets the item currently being worn by the armor stand on its chest.
	 *
	 * @param item the item to wear.
	 */
	void setChestplate(ItemStack item);

	/**
	 * Sets the armor stand's head's current pose.
	 *
	 * @param pose The current pose.
	 */
	void setHeadPose(Vector pose);

	/**
	 * Sets the item currently being worn by the armor stand on its head.
	 *
	 * @param item The item to wear.
	 */
	void setHelmet(ItemStack item);

	/**
	 * Sets the item the armor stand is currently holding.
	 *
	 * @param item The item to hold.
	 */
	void setItemInHand(ItemStack item);

	/**
	 * Sets the armor stand's left arm's current pose.
	 *
	 * @param pose The current pose.
	 */
	void setLeftArmPose(Vector pose);

	/**
	 * Sets the armor stand's left leg's current pose.
	 *
	 * @param pose The current pose.
	 */
	void setLeftLegPose(Vector pose);

	/**
	 * Sets the item currently being worn by the armor stand on its legs.
	 *
	 * @param item the item to wear.
	 */
	void setLeggings(ItemStack item);

	/**
	 * Sets whether this armor stand is scaled down.
	 *
	 * @param small Whether this is scaled down.
	 */
	void setSmall(boolean small);

	/**
	 * Sets whether this armor stand is a marker, meaning it has a very small collision box.
	 *
	 * @param marker Whether this is a marker.
	 */
	void setMarker(boolean marker);

	/**
	 * Sets the armor stand's right arm's current pose.
	 *
	 * @param pose The current pose.
	 */
	void setRightArmPose(Vector pose);

	/**
	 * Sets the armor stand's right leg's current pose.
	 *
	 * @param pose The current pose.
	 */
	void setRightLegPose(Vector pose);

	/**
	 * Sets whether the armor stand should be visible or not.
	 *
	 * @param visible Whether the stand is visible or not.
	 */
	void setVisible(boolean visible);

}