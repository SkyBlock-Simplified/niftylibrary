package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.CommandBlock;

public final class CraftCommandBlock extends CraftBlockState implements CommandBlock {

	public CraftCommandBlock(org.bukkit.block.CommandBlock commandBlock) {
		super(commandBlock);
	}

	@Override
	public org.bukkit.block.CommandBlock getHandle() {
		return (org.bukkit.block.CommandBlock)super.getHandle();
	}

	@Override
	public String getCommand() {
		return this.getHandle().getCommand();
	}

	@Override
	public void setCommand(String command) {
		this.getHandle().setCommand(command);
	}

	@Override
	public String getName() {
		return this.getHandle().getName();
	}

	@Override
	public void setName(String name) {
		this.getHandle().setName(name);
	}

}