package net.netcoding.nifty.common.minecraft.inventory.item.potion;

import com.google.common.base.Preconditions;

public final class PotionData {

	private final PotionType type;
	private final boolean extended;
	private final boolean upgraded;

	public PotionData(PotionType type) {
		this(type, false, false);
	}

	/**
	 * Instantiates a final PotionData object to contain information about a
	 * Potion
	 *
	 * @param type the type of the Potion
	 * @param extended whether the potion is extended PotionType#isExtendable()
	 * must be true
	 * @param upgraded whether the potion is upgraded PotionType#isUpgradable()
	 * must be true
	 */
	public PotionData(PotionType type, boolean extended, boolean upgraded) {
		Preconditions.checkArgument(type != null, "Potion type must not be NULL!");
		Preconditions.checkArgument(!upgraded || type.isUpgradeable(), "Potion Type is not upgradable!");
		Preconditions.checkArgument(!extended || type.isExtendable(), "Potion Type is not extendable!");
		Preconditions.checkArgument(!upgraded || !extended, "Potion cannot be both extended and upgraded!");
		this.type = type;
		this.extended = extended;
		this.upgraded = upgraded;
	}

	/**
	 * Gets the type of the potion, Type matches up with each kind of craftable
	 * potion
	 *
	 * @return the potion type
	 */
	public PotionType getType() {
		return this.type;
	}

	/**
	 * Checks if the potion is in an upgraded state. This refers to whether or
	 * not the potion is Tier 2, such as Potion of Fire Resistance II.
	 *
	 * @return true if the potion is upgraded;
	 */
	public boolean isUpgraded() {
		return this.upgraded;
	}

	/**
	 * Checks if the potion is in an extended state. This refers to the extended
	 * duration potions
	 *
	 * @return true if the potion is extended
	 */
	public boolean isExtended() {
		return this.extended;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.getType() != null ? this.getType().hashCode() : 0);
		hash = 23 * hash + (this.isExtended() ? 1 : 0);
		hash = 23 * hash + (this.isUpgraded() ? 1 : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj == null || PotionData.class.isAssignableFrom(obj.getClass()))
			return false;
		else {
			PotionData other = (PotionData) obj;
			return (this.isUpgraded() == other.isUpgraded()) && (this.isExtended() == other.isExtended()) && (this.getType() == other.getType());
		}
	}
}