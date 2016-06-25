package net.netcoding.nifty.common.minecraft.entity.living.complex;

import net.netcoding.nifty.common.minecraft.entity.Damageable;

/**
 * Represents an ender dragon part.
 */
public interface EnderDragonPart extends ComplexEntityPart, Damageable {

	@Override
	EnderDragon getParent();

}