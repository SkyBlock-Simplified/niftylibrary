package net.netcoding.nifty.craftbukkit.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.entity.living.Creature;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public abstract class CraftCreature extends CraftLivingEntity implements Creature {

	public CraftCreature(org.bukkit.entity.Creature creature) {
		super(creature);
	}

	@Override
	public org.bukkit.entity.Creature getHandle() {
		return (org.bukkit.entity.Creature)super.getHandle();
	}

	@Override
	public LivingEntity getTarget() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getTarget(), LivingEntity.class);
	}

	@Override
	public void setTarget(LivingEntity target) {
		this.getHandle().setTarget(((CraftLivingEntity)target).getHandle());
	}

}