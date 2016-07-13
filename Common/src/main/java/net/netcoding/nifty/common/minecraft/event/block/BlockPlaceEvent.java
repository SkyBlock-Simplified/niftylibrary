package net.netcoding.nifty.common.minecraft.event.block;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.block.state.BlockState;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.event.player.PlayerEvent;
import net.netcoding.nifty.common.minecraft.inventory.EquipmentSlot;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface BlockPlaceEvent extends BlockEvent, Cancellable, PlayerEvent {

	boolean canBuild();

	Block getBlockAgainst();

	BlockState getBlockReplacedState();

	EquipmentSlot getHand();

	ItemStack getItemInHand();

	void setBuild(boolean value);

}