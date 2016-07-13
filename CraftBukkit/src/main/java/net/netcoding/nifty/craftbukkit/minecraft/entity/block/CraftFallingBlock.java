package net.netcoding.nifty.craftbukkit.minecraft.entity.block;

import net.netcoding.nifty.common.minecraft.entity.block.FallingBlock;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public final class CraftFallingBlock extends CraftEntity implements FallingBlock {

	public CraftFallingBlock(org.bukkit.entity.FallingBlock fallingBlock) {
		super(fallingBlock);
	}

	@Override
	public boolean canHurtEntities() {
		return this.getHandle().canHurtEntities();
	}

	@Override
	public byte getBlockData() {
		return this.getHandle().getBlockData();
	}

	@Override
	public org.bukkit.entity.FallingBlock getHandle() {
		return (org.bukkit.entity.FallingBlock)super.getHandle();
	}

	@Override
	public Material getMaterial() {
		return Material.valueOf(this.getHandle().getMaterial().name());
	}

	@Override
	public void setDropItem(boolean dropItem) {
		this.getHandle().setDropItem(dropItem);
	}

	@Override
	public void setHurtEntities(boolean hurtEntities) {
		this.getHandle().setHurtEntities(hurtEntities);
	}

	@Override
	public boolean willDropItem() {
		return false; // TODO
	}

}