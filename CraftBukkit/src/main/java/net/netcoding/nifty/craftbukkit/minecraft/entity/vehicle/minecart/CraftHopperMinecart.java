package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.HopperMinecart;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftHopperMinecart extends CraftMinecart implements HopperMinecart {

	private final Inventory inventory;

	public CraftHopperMinecart(org.bukkit.entity.minecart.HopperMinecart hopperMinecart) {
		super(hopperMinecart);
		this.inventory = CraftInventory.convertBukkitInventory(hopperMinecart.getInventory());
	}

	@Override
	public org.bukkit.entity.minecart.HopperMinecart getHandle() {
		return (org.bukkit.entity.minecart.HopperMinecart)super.getHandle();
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public boolean isEnabled() {
		return this.getHandle().isEnabled();
	}

	@Override
	public void setEnabled(boolean value) {
		this.getHandle().setEnabled(value);
	}

}