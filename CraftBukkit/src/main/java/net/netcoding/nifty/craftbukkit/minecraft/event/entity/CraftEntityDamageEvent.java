package net.netcoding.nifty.craftbukkit.minecraft.event.entity;

import net.netcoding.nifty.common.minecraft.event.entity.EntityDamageEvent;

public class CraftEntityDamageEvent extends CraftEntityEvent implements EntityDamageEvent {

	public CraftEntityDamageEvent(org.bukkit.event.entity.EntityDamageEvent entityDamageEvent) {
		super(entityDamageEvent);
	}

	@Override
	public org.bukkit.event.entity.EntityDamageEvent getHandle() {
		return (org.bukkit.event.entity.EntityDamageEvent)super.getHandle();
	}

	@Override
	public Damage.Cause getCause() {
		return Damage.Cause.valueOf(this.getHandle().getCause().name());
	}

	@Override
	public double getDamage(Damage.Modifier type) {
		return this.getHandle().getDamage(org.bukkit.event.entity.EntityDamageEvent.DamageModifier.valueOf(type.name()));
	}

	@Override
	public double getFinalDamage() {
		return this.getHandle().getFinalDamage();
	}

	@Override
	public double getOriginalDamage(Damage.Modifier type) {
		return this.getHandle().getOriginalDamage(org.bukkit.event.entity.EntityDamageEvent.DamageModifier.valueOf(type.name()));
	}

	@Override
	public boolean isApplicable(Damage.Modifier type) {
		return this.getHandle().isApplicable(org.bukkit.event.entity.EntityDamageEvent.DamageModifier.valueOf(type.name()));
	}

	@Override
	public void setDamage(double damage) {
		this.getHandle().setDamage(damage);
	}

	@Override
	public void setDamage(Damage.Modifier type, double damage) {
		this.getHandle().setDamage(org.bukkit.event.entity.EntityDamageEvent.DamageModifier.valueOf(type.name()), damage);
	}

	@Override
	public boolean isCancelled() {
		return this.getHandle().isCancelled();
	}

	@Override
	public void setCancelled(boolean value) {
		this.getHandle().setCancelled(value);
	}

}