package net.netcoding.nifty.common._new_.minecraft.entity.projectile.source;

import net.netcoding.nifty.common._new_.minecraft.block.Block;

public interface BlockProjectileSource extends ProjectileSource {

	/**
	 * Gets the block this projectile source belongs to.
	 *
	 * @return Block for the projectile source.
	 */
	Block getBlock();

}