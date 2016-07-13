package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Dropper;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftDropper extends CraftBlockState implements Dropper {

	public CraftDropper(org.bukkit.block.Dropper dropper) {
		super(dropper);
	}

	@Override
	public org.bukkit.block.Dropper getHandle() {
		return (org.bukkit.block.Dropper)super.getHandle();
	}

	@Override
	public void drop() {
		this.getHandle().drop();
	}

	@Override
	public Inventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory());
	}

}