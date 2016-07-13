package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.Item;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

public final class CraftItem extends CraftEntity implements Item {

	public CraftItem(org.bukkit.entity.Item item) {
		super(item);
	}

	@Override
	public org.bukkit.entity.Item getHandle() {
		return (org.bukkit.entity.Item)super.getHandle();
	}

	@Override
	public ItemStack getItemStack() {
		return new CraftItemStack(this.getHandle().getItemStack());
	}

	@Override
	public int getPickupDelay() {
		return this.getHandle().getPickupDelay();
	}

	@Override
	public void setItemStack(ItemStack item) {
		this.getHandle().setItemStack(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setPickupDelay(int delay) {
		this.getHandle().setPickupDelay(delay);
	}

}