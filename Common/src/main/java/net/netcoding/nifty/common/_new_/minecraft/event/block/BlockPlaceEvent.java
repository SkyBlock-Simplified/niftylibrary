package net.netcoding.nifty.common._new_.minecraft.event.block;

import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.block.BlockState;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.event.player.PlayerEvent;
import net.netcoding.nifty.common._new_.minecraft.inventory.EquipmentSlot;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public interface BlockPlaceEvent extends BlockEvent, Cancellable, PlayerEvent {

	boolean canBuild();

	Block getBlockAgainst();

	BlockState getBlockReplacedState();

	EquipmentSlot getHand();

	ItemStack getItemInHand();

	void setBuild(boolean value);

}