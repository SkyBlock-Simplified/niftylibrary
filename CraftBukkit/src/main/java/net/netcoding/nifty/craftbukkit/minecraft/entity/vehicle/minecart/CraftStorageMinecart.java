package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.StorageMinecart;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftStorageMinecart extends CraftMinecart implements StorageMinecart {

	private final Inventory inventory;

	public CraftStorageMinecart(org.bukkit.entity.minecart.StorageMinecart storageMinecart) {
		super(storageMinecart);
		this.inventory = CraftInventory.convertBukkitInventory(storageMinecart.getInventory());
	}

	@Override
	public org.bukkit.entity.minecart.StorageMinecart getHandle() {
		return (org.bukkit.entity.minecart.StorageMinecart)super.getHandle();
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

}