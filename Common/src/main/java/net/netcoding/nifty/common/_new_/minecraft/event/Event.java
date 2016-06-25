package net.netcoding.nifty.common._new_.minecraft.event;

import net.netcoding.nifty.common.Nifty;

public interface Event {

	default String getEventName() {
		return this.getClass().getSimpleName();
	}

	default boolean isAsynchronous() {
		return !Thread.currentThread().equals(Nifty.getServiceManager().getProvider(Thread.class));
	}

}