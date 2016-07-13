package net.netcoding.nifty.common.minecraft.command;

import net.netcoding.nifty.common.minecraft.block.Block;

public interface BlockCommandSource extends CommandSource {

	Block getBlock();

}