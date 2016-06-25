package net.netcoding.nifty.common._new_.minecraft.event.hanging;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;

public interface HangingBreakByEntityEvent extends HangingBreakEvent {

	Entity getRemover();

	@Override
	default RemoveCause getCause() {
		return RemoveCause.ENTITY;
	}

}