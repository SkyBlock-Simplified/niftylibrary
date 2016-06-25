package net.netcoding.nifty.common._new_.minecraft.entity.living.complex;

import net.netcoding.nifty.common._new_.minecraft.entity.Damageable;

/**
 * Represents an ender dragon part.
 */
public interface EnderDragonPart extends ComplexEntityPart, Damageable {

	@Override
	EnderDragon getParent();

}