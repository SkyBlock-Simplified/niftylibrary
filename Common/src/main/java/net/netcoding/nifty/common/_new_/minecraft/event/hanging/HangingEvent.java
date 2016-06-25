package net.netcoding.nifty.common._new_.minecraft.event.hanging;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;
import net.netcoding.nifty.common._new_.minecraft.event.Event;

public interface HangingEvent extends Event {

	Entity getHanging();

}