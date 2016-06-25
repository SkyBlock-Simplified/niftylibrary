package net.netcoding.nifty.common.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.entity.living.golem.IronGolem;

/**
 * Represents a minecart that can have certain {@link
 * Entity entities} as passengers. Normal passengers
 * include all {@link LivingEntity living entities} with
 * the exception of {@link IronGolem iron golems}.
 * Non-player entities that meet normal passenger criteria automatically
 * mount these minecarts when close enough.
 */
public interface RideableMinecart extends Minecart {

}