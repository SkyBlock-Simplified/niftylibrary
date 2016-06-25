package net.netcoding.nifty.common.minecraft.entity.living.complex;

import net.netcoding.nifty.common.minecraft.entity.Entity;

/**
 * Represents a single part of a {@link ComplexLivingEntity}
 */
public interface ComplexEntityPart extends Entity {

	/**
	 * Gets the parent {@link ComplexLivingEntity} of this part.
	 *
	 * @return Parent complex entity
	 */
	ComplexLivingEntity getParent();

}