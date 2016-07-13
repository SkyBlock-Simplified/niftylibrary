package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.BrewingStand;
import net.netcoding.nifty.common.minecraft.inventory.types.BrewerInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftBrewingStand extends CraftBlockState implements BrewingStand {

	public CraftBrewingStand(org.bukkit.block.BrewingStand brewingStand) {
		super(brewingStand);
	}

	@Override
	public int getBrewingTime() {
		return this.getHandle().getBrewingTime();
	}

	@Override
	public int getFuelLevel() {
		return this.getHandle().getFuelLevel();
	}

	@Override
	public org.bukkit.block.BrewingStand getHandle() {
		return (org.bukkit.block.BrewingStand)super.getHandle();
	}

	@Override
	public BrewerInventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory(), BrewerInventory.class);
	}

	@Override
	public void setBrewingTime(int brewTime) {
		this.getHandle().setBrewingTime(brewTime);
	}

	@Override
	public void setFuelLevel(int level) {
		this.getHandle().setFuelLevel(level);
	}

}