package net.netcoding.nifty.common._new_.minecraft.entity.living.complex;

import net.netcoding.nifty.common._new_.minecraft.entity.living.LivingEntity;

import java.util.Set;

/**
 * Represents a complex living entity that is made up of various smaller parts.
 */
public interface ComplexLivingEntity extends LivingEntity {

	/**
	 * Gets a list of parts that belong to this complex entity.
	 *
	 * @return List of parts.
	 */
	Set<ComplexEntityPart> getParts();

}