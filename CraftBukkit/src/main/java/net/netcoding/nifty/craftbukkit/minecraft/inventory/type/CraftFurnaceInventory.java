package net.netcoding.nifty.craftbukkit.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.block.state.Furnace;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.type.FurnaceInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public final class CraftFurnaceInventory extends CraftInventory implements FurnaceInventory {

	public CraftFurnaceInventory(org.bukkit.inventory.FurnaceInventory furnaceInventory) {
		super(furnaceInventory);
	}

	@Override
	public ItemStack getFuel() {
		return new CraftItemStack(this.getHandle().getFuel());
	}

	@Override
	public org.bukkit.inventory.FurnaceInventory getHandle() {
		return (org.bukkit.inventory.FurnaceInventory)super.getHandle();
	}

	@Override
	public Furnace getHolder() {
		return (Furnace)super.getHolder();
	}

	@Override
	public ItemStack getResult() {
		return new CraftItemStack(this.getHandle().getResult());
	}

	@Override
	public ItemStack getSmelting() {
		return new CraftItemStack(this.getHandle().getSmelting());
	}

	@Override
	public void setFuel(ItemStack item) {
		this.getHandle().setFuel(CraftConverter.toBukkitItem(item));
	}

	@Override
	public void setResult(ItemStack item) {
		this.getHandle().setResult(CraftConverter.toBukkitItem(item));
	}

	@Override
	public void setSmelting(ItemStack item) {
		this.getHandle().setSmelting(CraftConverter.toBukkitItem(item));
	}

}