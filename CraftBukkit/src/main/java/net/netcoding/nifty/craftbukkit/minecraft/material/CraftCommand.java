package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Command;

public final class CraftCommand extends CraftMaterialData implements Command {

	public CraftCommand(org.bukkit.material.Command command) {
		super(command);
	}

	@Override
	public Command clone() {
		return (Command)super.clone();
	}

}