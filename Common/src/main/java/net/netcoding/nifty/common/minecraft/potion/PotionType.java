package net.netcoding.nifty.common.minecraft.potion;

/**
 * This enum reflects and matches each potion state that can be obtained from
 * the Creative mode inventory
 */
public enum PotionType {

	UNCRAFTABLE(null, false, false),
	WATER(null, false, false),
	MUNDANE(null, false, false),
	THICK(null, false, false),
	AWKWARD(null, false, false),
	NIGHT_VISION(PotionEffectType.NIGHT_VISION, false, true),
	INVISIBILITY(PotionEffectType.INVISIBILITY, false, true),
	JUMP(PotionEffectType.JUMP, true, true),
	FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE, false, true),
	SPEED(PotionEffectType.SPEED, true, true),
	SLOWNESS(PotionEffectType.SLOW, false, true),
	WATER_BREATHING(PotionEffectType.WATER_BREATHING, false, true),
	INSTANT_HEAL(PotionEffectType.HEAL, true, false),
	INSTANT_DAMAGE(PotionEffectType.HARM, true, false),
	POISON(PotionEffectType.POISON, true, true),
	REGEN(PotionEffectType.REGENERATION, true, true),
	STRENGTH(PotionEffectType.INCREASE_DAMAGE, true, true),
	WEAKNESS(PotionEffectType.WEAKNESS, false, true),
	LUCK(PotionEffectType.LUCK, false, false);

	private final PotionEffectType effect;
	private final boolean upgradeable;
	private final boolean extendable;

	PotionType(PotionEffectType effect, boolean upgradeable, boolean extendable) {
		this.effect = effect;
		this.upgradeable = upgradeable;
		this.extendable = extendable;
	}

	public PotionEffectType getEffectType() {
		return this.effect;
	}

	public boolean isInstant() {
		return this.getEffectType() != null && this.getEffectType().isInstant();
	}

	/**
	 * Checks if the potion type has an upgraded state.
	 * This refers to whether or not the potion type can be Tier 2,
	 * such as Potion of Fire Resistance II.
	 *
	 * @return true if the potion type can be upgraded;
	 */
	public boolean isUpgradeable() {
		return this.upgradeable;
	}

	/**
	 * Checks if the potion type has an extended state.
	 * This refers to the extended duration potions
	 *
	 * @return true if the potion type can be extended
	 */
	public boolean isExtendable() {
		return this.extendable;
	}

	public int getMaxLevel() {
		return this.isUpgradeable() ? 2 : 1;
	}

	/**
	 * @deprecated Misleading
	 */
	@Deprecated
	public static PotionType getByEffect(PotionEffectType effectType) {
		if (effectType == null)
			return WATER;

		for (PotionType type : PotionType.values()) {
			if (effectType.equals(type.getEffectType()))
				return type;
		}

		return WATER;
	}

}