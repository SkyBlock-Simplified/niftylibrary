package net.netcoding.nifty.common.minecraft.entity.projectile.potion;

import net.netcoding.nifty.common.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.material.Material;

import java.util.Collection;

/**
 * Represents a thrown Potion bottle.
 */
public interface ThrownPotion extends Projectile {

	/**
	 * Returns the effects that are applied by this potion.
	 *
	 * @return The potion effects
	 */
	Collection<PotionEffect> getEffects();

	/**
	 * Returns a copy of the ItemStack for this thrown potion.
	 * <p>
	 * Altering this copy will not alter the thrown potion directly. If you want
	 * to alter the thrown potion, you must use the {@link
	 * #setItem(ItemStack) setItemStack} method.
	 *
	 * @return A copy of the ItemStack for this thrown potion.
	 */
	ItemStack getItem();

	/**
	 * Set the ItemStack for this thrown potion.
	 * <p>
	 * The ItemStack must be of type {@link Material#SPLASH_POTION}
	 * or {@link Material#LINGERING_POTION}, otherwise an exception
	 * is thrown.
	 *
	 * @param item New ItemStack.
	 */
	void setItem(ItemStack item);

}