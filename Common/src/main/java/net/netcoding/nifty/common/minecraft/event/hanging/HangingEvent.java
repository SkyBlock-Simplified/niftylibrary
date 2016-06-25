package net.netcoding.nifty.common.minecraft.event.hanging;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.event.Event;

public interface HangingEvent extends Event {

	Entity getHanging();

}