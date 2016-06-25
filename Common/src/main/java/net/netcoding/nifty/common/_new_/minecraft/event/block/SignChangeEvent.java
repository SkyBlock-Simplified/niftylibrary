package net.netcoding.nifty.common._new_.minecraft.event.block;

import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.event.player.PlayerEvent;

public interface SignChangeEvent extends BlockEvent, Cancellable, PlayerEvent {

	default String getLine(int index) throws IndexOutOfBoundsException {
		return this.getLines()[index];
	}

	String[] getLines();

	void setLine(int index, String line) throws IndexOutOfBoundsException;

}