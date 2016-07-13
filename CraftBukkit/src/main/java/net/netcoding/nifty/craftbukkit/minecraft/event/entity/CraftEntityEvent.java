package net.netcoding.nifty.craftbukkit.minecraft.event.entity;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.event.entity.EntityEvent;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public class CraftEntityEvent implements EntityEvent {

	private final org.bukkit.event.entity.EntityEvent entityEvent;

	public CraftEntityEvent(org.bukkit.event.entity.EntityEvent entityEvent) {
		this.entityEvent = entityEvent;
	}

	@Override
	public Entity getEntity() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getEntity());
	}

	public org.bukkit.event.entity.EntityEvent getHandle() {
		return this.entityEvent;
	}

}