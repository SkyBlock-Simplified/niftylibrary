package net.netcoding.nifty.common.minecraft.event.entity;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.EntityType;
import net.netcoding.nifty.common.minecraft.event.Event;

public interface EntityEvent extends Event {

	Entity getEntity();

	EntityType getEntityType();

}