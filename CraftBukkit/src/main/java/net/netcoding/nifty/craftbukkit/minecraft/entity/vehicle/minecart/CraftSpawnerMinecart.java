package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.SpawnerMinecart;

public final class CraftSpawnerMinecart extends CraftMinecart implements SpawnerMinecart {

	public CraftSpawnerMinecart(org.bukkit.entity.minecart.SpawnerMinecart spawnerMinecart) {
		super(spawnerMinecart);
	}

	@Override
	public org.bukkit.entity.minecart.SpawnerMinecart getHandle() {
		return (org.bukkit.entity.minecart.SpawnerMinecart)super.getHandle();
	}

}