package net.netcoding.nifty.common.minecraft.event.hanging;

import net.netcoding.nifty.common.minecraft.event.Cancellable;

public interface HangingBreakEvent extends HangingEvent, Cancellable {

	RemoveCause getCause();

	enum RemoveCause {

		ENTITY,
		EXPLOSION,
		OBSTRUCTION,
		PHYSICS,
		DEFAULT

	}

}