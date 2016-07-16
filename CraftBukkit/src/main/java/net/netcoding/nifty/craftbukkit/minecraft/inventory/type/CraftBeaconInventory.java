package net.netcoding.nifty.craftbukkit.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.type.BeaconInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public final class CraftBeaconInventory extends CraftInventory implements BeaconInventory {

	public CraftBeaconInventory(org.bukkit.inventory.BeaconInventory beaconInventory) {
		super(beaconInventory);
	}

	@Override
	public org.bukkit.inventory.BeaconInventory getHandle() {
		return (org.bukkit.inventory.BeaconInventory)super.getHandle();
	}

	@Override
	public ItemStack getItem() {
		return new CraftItemStack(this.getHandle().getItem());
	}

	@Override
	public void setItem(ItemStack item) {
		this.getHandle().setItem(CraftConverter.toBukkitItem(item));
	}

}