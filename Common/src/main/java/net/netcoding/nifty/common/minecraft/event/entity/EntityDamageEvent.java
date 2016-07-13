package net.netcoding.nifty.common.minecraft.event.entity;

import net.netcoding.nifty.common.minecraft.event.Cancellable;

public interface EntityDamageEvent extends Cancellable, EntityEvent {

	Damage.Cause getCause();

	default double getDamage() {
		return this.getDamage(Damage.Modifier.BASE);
	}

	double getDamage(Damage.Modifier type);

	double getFinalDamage();

	double getOriginalDamage(Damage.Modifier type);

	boolean isApplicable(Damage.Modifier type);

	void setDamage(double damage);

	void setDamage(Damage.Modifier type, double damage);

	class Damage {

		public enum Cause {

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

		public enum Modifier {

			BASE,
			HARD_HAT,
			BLOCKING,
			ARMOR,
			RESISTANCE,
			MAGIC,
			ABSORPTION

		}

	}

}