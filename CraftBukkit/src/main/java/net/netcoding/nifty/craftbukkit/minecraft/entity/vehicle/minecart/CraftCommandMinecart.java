package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.CommandMinecart;

public final class CraftCommandMinecart extends CraftMinecart implements CommandMinecart {

	public CraftCommandMinecart(org.bukkit.entity.minecart.CommandMinecart commandMinecart) {
		super(commandMinecart);
	}

	@Override
	public String getCommand() {
		return this.getHandle().getCommand();
	}

	@Override
	public org.bukkit.entity.minecart.CommandMinecart getHandle() {
		return (org.bukkit.entity.minecart.CommandMinecart)super.getHandle();
	}

	@Override
	public void setCommand(String command) {
		this.getHandle().setCommand(command);
	}

	@Override
	public void setName(String name) {
		this.getHandle().setName(name);
	}

}