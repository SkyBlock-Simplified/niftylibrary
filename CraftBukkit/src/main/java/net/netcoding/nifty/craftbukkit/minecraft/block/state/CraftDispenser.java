package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Dispenser;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.BlockProjectileSource;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.source.CraftBlockProjectileSource;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftDispenser extends CraftBlockState implements Dispenser {

	public CraftDispenser(org.bukkit.block.Dispenser dispenser) {
		super(dispenser);
	}

	@Override
	public boolean dispense() {
		return this.getHandle().dispense();
	}

	@Override
	public org.bukkit.block.Dispenser getHandle() {
		return (org.bukkit.block.Dispenser)super.getHandle();
	}

	@Override
	public BlockProjectileSource getBlockProjectileSource() {
		return new CraftBlockProjectileSource(this.getHandle().getBlockProjectileSource());
	}

	@Override
	public Inventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory());
	}

}