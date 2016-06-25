package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.block.Action;
import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.event.EventResult;
import net.netcoding.nifty.common._new_.minecraft.inventory.EquipmentSlot;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.minecraft.material.Material;

public interface PlayerInteractEvent extends PlayerEvent, Cancellable {

	Action getAction();

	BlockFace getBlockFace();

	Block getClickedBlock();

	EquipmentSlot getHand();

	ItemStack getItem();

	Material getMaterial();

	boolean hasBlock();

	boolean hasItem();

	boolean isBlockInHand();

	default boolean isCancelled() {
		return this.useInteractedBlock() == EventResult.DENY;
	}

	default void setCancelled(boolean cancelled) {
		this.setUseInteractedBlock(cancelled ? EventResult.DENY : (this.useInteractedBlock() == EventResult.DENY ? EventResult.DEFAULT : this.useInteractedBlock()));
		this.setUseItemInHand(cancelled ? EventResult.DENY : (this.useItemInHand() == EventResult.DENY ? EventResult.DEFAULT : this.useItemInHand()));
	}

	void setUseInteractedBlock(EventResult result);

	void setUseItemInHand(EventResult result);

	EventResult useInteractedBlock();

	EventResult useItemInHand();

}