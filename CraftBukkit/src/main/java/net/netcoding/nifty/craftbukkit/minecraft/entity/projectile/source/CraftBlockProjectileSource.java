package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.source;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.BlockProjectileSource;
import net.netcoding.nifty.core.util.misc.Vector;
import net.netcoding.nifty.craftbukkit.minecraft.block.CraftBlock;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntityType;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public final class CraftBlockProjectileSource implements BlockProjectileSource {

	private final org.bukkit.projectiles.BlockProjectileSource blockProjectileSource;
	private final Block block;

	public CraftBlockProjectileSource(org.bukkit.projectiles.BlockProjectileSource blockProjectileSource) {
		this.blockProjectileSource = blockProjectileSource;
		this.block = new CraftBlock(blockProjectileSource.getBlock());
	}

	public org.bukkit.projectiles.BlockProjectileSource getHandle() {
		return this.blockProjectileSource;
	}

	@Override
	public Block getBlock() {
		return this.block;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
		Class<? extends org.bukkit.entity.Projectile> projectileClass = (Class<org.bukkit.entity.Projectile>)CraftEntityType.getByClass(projectile).getBukkitClass();
		org.bukkit.entity.Projectile bukkitProjectile = this.getHandle().launchProjectile(projectileClass, CraftConverter.toBukkitVector(velocity));
		return CraftEntity.convertBukkitEntity(bukkitProjectile, projectile);
	}

}