package net.netcoding.nifty.common.minecraft.command.source;

import net.netcoding.nifty.common.minecraft.block.Block;

public interface BlockCommandSource extends CommandSource {

	Block getBlock();

}