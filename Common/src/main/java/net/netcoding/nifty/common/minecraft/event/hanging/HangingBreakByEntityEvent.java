package net.netcoding.nifty.common.minecraft.event.hanging;

import net.netcoding.nifty.common.minecraft.entity.Entity;

public interface HangingBreakByEntityEvent extends HangingBreakEvent {

	Entity getRemover();

	@Override
	default RemoveCause getCause() {
		return RemoveCause.ENTITY;
	}

}