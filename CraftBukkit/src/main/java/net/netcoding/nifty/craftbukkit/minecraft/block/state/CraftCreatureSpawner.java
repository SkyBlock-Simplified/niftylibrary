package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.CreatureSpawner;
import net.netcoding.nifty.common.minecraft.entity.EntityType;

public final class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {

	public CraftCreatureSpawner(org.bukkit.block.CreatureSpawner creatureSpawner) {
		super(creatureSpawner);
	}

	@Override
	public int getDelay() {
		return this.getHandle().getDelay();
	}

	@Override
	public org.bukkit.block.CreatureSpawner getHandle() {
		return (org.bukkit.block.CreatureSpawner)super.getHandle();
	}

	@Override
	public EntityType getSpawnedType() {
		return EntityType.valueOf(this.getHandle().getSpawnedType().name());
	}

	@Override
	public void setSpawnedType(EntityType creatureType) {
		this.getHandle().setSpawnedType(org.bukkit.entity.EntityType.valueOf(creatureType.name()));
	}

	@Override
	public void setDelay(int delay) {
		this.getHandle().setDelay(delay);
	}

}