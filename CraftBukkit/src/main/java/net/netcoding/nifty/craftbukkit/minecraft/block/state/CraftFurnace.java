package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Furnace;
import net.netcoding.nifty.common.minecraft.inventory.types.FurnaceInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftFurnace extends CraftBlockState implements Furnace {

	public CraftFurnace(org.bukkit.block.Furnace furnace) {
		super(furnace);
	}

	@Override
	public org.bukkit.block.Furnace getHandle() {
		return (org.bukkit.block.Furnace)super.getHandle();
	}

	@Override
	public short getBurnTime() {
		return this.getHandle().getBurnTime();
	}

	@Override
	public short getCookTime() {
		return this.getHandle().getCookTime();
	}

	@Override
	public FurnaceInventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory(), FurnaceInventory.class);
	}

	@Override
	public void setBurnTime(short value) {
		this.getHandle().setBurnTime(value);
	}

	@Override
	public void setCookTime(short value) {
		this.getHandle().setCookTime(value);
	}

}