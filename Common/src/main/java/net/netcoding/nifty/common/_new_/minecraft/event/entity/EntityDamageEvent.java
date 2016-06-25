package net.netcoding.nifty.common._new_.minecraft.event.entity;

import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;

public interface EntityDamageEvent extends Cancellable, EntityEvent {

	DamageCause getCause();

	default double getDamage() {
		return this.getDamage(DamageModifier.BASE);
	}

	double getDamage(DamageModifier type);

	double getFinalDamage();

	double getOriginalDamage(DamageModifier type);

	boolean isApplicable(DamageModifier type);

	void setDamage(double damage);

	void setDamage(DamageModifier type, double damage);

	enum DamageCause {

		CONTACT,
		ENTITY_ATTACK,
		PROJECTILE,
		SUFFOCATION,
		FALL,
		FIRE,
		FIRE_TICK,
		MELTING,
		LAVA,
		DROWNING,
		BLOCK_EXPLOSION,
		ENTITY_EXPLOSION,
		VOID,
		LIGHTNING,
		SUICIDE,
		STARVATION,
		POISON,
		MAGIC,
		WITHER,
		FALLING_BLOCK,
		THORNS,
		DRAGON_BREATH,
		CUSTOM,
		FLY_INTO_WALL

	}

	enum DamageModifier {

		BASE,
		HARD_HAT,
		BLOCKING,
		ARMOR,
		RESISTANCE,
		MAGIC,
		ABSORPTION

	}

}