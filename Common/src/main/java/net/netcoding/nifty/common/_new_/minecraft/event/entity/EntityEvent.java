package net.netcoding.nifty.common._new_.minecraft.event.entity;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;
import net.netcoding.nifty.common._new_.minecraft.entity.EntityType;
import net.netcoding.nifty.common._new_.minecraft.event.Event;

public interface EntityEvent extends Event {

	Entity getEntity();

	EntityType getEntityType();

}