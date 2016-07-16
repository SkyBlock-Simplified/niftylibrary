package net.netcoding.nifty.craftbukkit.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.block.state.DoubleChest;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.type.DoubleChestInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftDoubleChestInventory extends CraftInventory implements DoubleChestInventory {

	private final Inventory leftSide;
	private final Inventory rightSide;

	public CraftDoubleChestInventory(org.bukkit.inventory.DoubleChestInventory doubleChestInventory) {
		super(doubleChestInventory);
		this.leftSide = CraftInventory.convertBukkitInventory(doubleChestInventory.getLeftSide());
		this.rightSide = CraftInventory.convertBukkitInventory(doubleChestInventory.getRightSide());
	}

	@Override
	public org.bukkit.inventory.DoubleChestInventory getHandle() {
		return (org.bukkit.inventory.DoubleChestInventory)super.getHandle();
	}

	@Override
	public DoubleChest getHolder() {
		return (DoubleChest)super.getHolder();
	}

	@Override
	public Inventory getLeftSide() {
		return this.leftSide;
	}

	@Override
	public Inventory getRightSide() {
		return this.rightSide;
	}

}