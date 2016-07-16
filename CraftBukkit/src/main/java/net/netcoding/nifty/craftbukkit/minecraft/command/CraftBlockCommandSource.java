package net.netcoding.nifty.craftbukkit.minecraft.command;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.command.BlockCommandSource;
import net.netcoding.nifty.craftbukkit.minecraft.block.CraftBlock;

public final class CraftBlockCommandSource extends CraftCommandSource implements BlockCommandSource {

	public CraftBlockCommandSource(org.bukkit.command.BlockCommandSender sender) {
		super(sender);
	}

	@Override
	public org.bukkit.command.BlockCommandSender getHandle() {
		return (org.bukkit.command.BlockCommandSender)super.getHandle();
	}

	@Override
	public Block getBlock() {
		return new CraftBlock(this.getHandle().getBlock());
	}

}