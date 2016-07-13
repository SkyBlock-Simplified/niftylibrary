package net.netcoding.nifty.craftbukkit.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.inventory.types.AnvilInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftAnvilInventory extends CraftInventory implements AnvilInventory {

	public CraftAnvilInventory(org.bukkit.inventory.AnvilInventory anvilInventory) {
		super(anvilInventory);
	}

	@Override
	public org.bukkit.inventory.AnvilInventory getHandle() {
		return (org.bukkit.inventory.AnvilInventory)super.getHandle();
	}

}