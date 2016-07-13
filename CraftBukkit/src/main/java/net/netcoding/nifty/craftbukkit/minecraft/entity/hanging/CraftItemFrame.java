package net.netcoding.nifty.craftbukkit.minecraft.entity.hanging;

import net.netcoding.nifty.common.minecraft.Rotation;
import net.netcoding.nifty.common.minecraft.entity.hanging.ItemFrame;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

public final class CraftItemFrame extends CraftHanging implements ItemFrame {

	public CraftItemFrame(org.bukkit.entity.ItemFrame itemFrame) {
		super(itemFrame);
	}

	@Override
	public org.bukkit.entity.ItemFrame getHandle() {
		return (org.bukkit.entity.ItemFrame)super.getHandle();
	}

	@Override
	public ItemStack getItem() {
		return new CraftItemStack(this.getHandle().getItem());
	}

	@Override
	public Rotation getRotation() {
		return Rotation.valueOf(this.getHandle().getRotation().name());
	}

	@Override
	public void setItem(ItemStack item) {
		this.getHandle().setItem(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setRotation(Rotation rotation) {
		this.getHandle().setRotation(org.bukkit.Rotation.valueOf(rotation.name()));
	}

}