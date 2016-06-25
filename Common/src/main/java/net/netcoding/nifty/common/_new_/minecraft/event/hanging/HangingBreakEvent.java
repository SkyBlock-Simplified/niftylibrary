package net.netcoding.nifty.common._new_.minecraft.event.hanging;

import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;

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