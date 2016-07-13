package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.FireworkEffect;
import net.netcoding.nifty.core.util.ListUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface FireworkMeta extends ItemMeta {

	/**
	 * Add several effects to this firework.
	 *
	 * @param effects The firework effects to add
	 */
	default void addEffects(FireworkEffect... effects) {
		this.addEffects(Arrays.asList(effects));
	}

	/**
	 * Add several firework effects to this firework.
	 *
	 * @param effects A collections containing the desired firework effects.
	 */
	void addEffects(Collection<? extends FireworkEffect> effects);

	/**
	 * Remove all effects from this firework.
	 */
	void clearEffects();

	@Override
	FireworkMeta clone();

	/**
	 * Get the effects in this firework.
	 *
	 * @return An list of the firework effects.
	 */
	List<FireworkEffect> getEffects();

	/**
	 * Gets the approximate height the firework will fly.
	 *
	 * @return Approximate flight height of the firework.
	 */
	int getPower();

	/**
	 * Get whether this firework has any effects.
	 *
	 * @return True if it has effects
	 */
	default boolean hasEffects() {
		return ListUtil.notEmpty(this.getEffects());
	}

	/**
	 * Remove an effect from this firework.
	 *
	 * @param index The index of the effect to remove.
	 */
	void removeEffect(int index);

	/**
	 * Sets the approximate power of the firework. Each level of power is half
	 * a second of flight time.
	 *
	 * @param power The power of the firework, from 0-128.
	 */
	void setPower(int power);

}