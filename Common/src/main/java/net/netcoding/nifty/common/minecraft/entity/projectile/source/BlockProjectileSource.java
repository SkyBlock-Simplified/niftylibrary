package net.netcoding.nifty.common.minecraft.entity.projectile.source;

import net.netcoding.nifty.common.minecraft.block.Block;

public interface BlockProjectileSource extends ProjectileSource {

	/**
	 * Gets the block this projectile source belongs to.
	 *
	 * @return Block for the projectile source.
	 */
	Block getBlock();

}