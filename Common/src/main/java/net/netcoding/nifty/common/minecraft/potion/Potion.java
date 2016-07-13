package net.netcoding.nifty.common.minecraft.potion;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.PotionMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.api.builder.BuilderCore;

import java.util.Collection;

/**
 * Potion Adapter for pre-1.9 data values
 * see @PotionMeta for 1.9+
 */
public final class Potion {

	private static final int EXTENDED_BIT = 0x40;
	private static final int POTION_BIT = 0xF;
	private static final int SPLASH_BIT = 0x4000;
	private static final int TIER_BIT = 0x20;
	private static final int TIER_SHIFT = 5;
	private boolean extended = false;
	private boolean splash = false;
	private int level = 1;
	private PotionType type;

	/**
	 * Construct a new potion of the given type. Unless the type is {@link
	 * PotionType#WATER}, it will be level one, without extended duration.
	 * Don't use this constructor to create a no-effect potion other than
	 * water bottle.
	 *
	 * @param type The potion type
	 */
	public Potion(PotionType type) {
		Preconditions.checkArgument(type != null, "Type cannot be NULL!");
		this.type = type;
	}

	/**
	 * Create a new potion of the given type and level.
	 *
	 * @param type The type of potion.
	 * @param level The potion's level.
	 */
	public Potion(PotionType type, int level) {
		this(type);
		Preconditions.checkArgument(level > 0 && level < 3, "Level must be 1 or 2!");
		this.level = level;
	}

	/**
	 * Create a new potion of the given type and level.
	 *
	 * @param type The type of potion.
	 * @param level The potion's level.
	 * @param splash Whether it is a splash potion.
	 */
	public Potion(PotionType type, int level, boolean splash) {
		this(type, level);
		this.splash = splash;
	}

	/**
	 * Create a new potion of the given type and level.
	 *
	 * @param type The type of potion.
	 * @param level The potion's level.
	 * @param splash Whether it is a splash potion.
	 * @param extended Whether it has an extended duration.
	 */
	public Potion(PotionType type, int level, boolean splash, boolean extended) {
		this(type, level, splash);
		this.extended = extended;
	}

	/**
	 * Applies the effects of this potion to the given {@link ItemStack}. The
	 * ItemStack must be a potion.
	 *
	 * @param item The itemstack to apply to
	 */
	public void apply(ItemStack item) {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");
		Preconditions.checkArgument(item.hasItemMeta(), "Item has no ItemMeta!");
		Preconditions.checkArgument(item.getItemMeta() instanceof PotionMeta, "Item is not a potion!");
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setBasePotionData(new PotionData(this.getType(), this.hasExtendedDuration(), this.getLevel() == 2));
		item.setItemMeta(meta);
	}

	/**
	 * Applies the effects that would be applied by this potion to the given
	 * {@link LivingEntity}.
	 *
	 * @see LivingEntity#addPotionEffects(Collection)
	 * @param entity The entity to apply the effects to
	 */
	public void apply(LivingEntity entity) {
		Preconditions.checkArgument(entity != null, "Entity cannot be NULL!");
		entity.addPotionEffects(getEffects());
	}

	public static Builder builder() {
		return Nifty.getBuilderManager().createBuilder(Potion.class);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (!(obj instanceof Potion))
			return false;
		else {
			Potion other = (Potion)obj;
			return this.hasExtendedDuration() == other.hasExtendedDuration() && this.isSplash() == other.isSplash() && this.getLevel() == other.getLevel() && this.getType() == other.getType();
		}
	}

	/**
	 * Returns a collection of {@link PotionEffect}s that this {@link Potion}
	 * would confer upon a {@link LivingEntity}.
	 *
	 * @return The effects that this potion applies
	 */
	public Collection<PotionEffect> getEffects() {
		return getBrewer().getEffects(this.getType(), this.getLevel() == 2, this.hasExtendedDuration());
	}

	/**
	 * Returns the level of this potion.
	 *
	 * @return The level of this potion
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Returns the {@link PotionType} of this potion.
	 *
	 * @return The type of this potion
	 */
	public PotionType getType() {
		return this.type;
	}

	/**
	 * Returns whether this potion has an extended duration.
	 *
	 * @return Whether this potion has extended duration
	 */
	public boolean hasExtendedDuration() {
		return this.extended;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + this.getLevel();
		result = prime * result + (this.hasExtendedDuration() ? 1231 : 1237);
		result = prime * result + (this.isSplash() ? 1231 : 1237);
		result = prime * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
		return result;
	}

	/**
	 * Returns whether this potion is a splash potion.
	 *
	 * @return Whether this is a splash potion
	 */
	public boolean isSplash() {
		return this.splash;
	}

	/**
	 * Set whether this potion has extended duration. This will cause the
	 * potion to have roughly 8/3 more duration than a regular potion.
	 *
	 * @param isExtended Whether the potion should have extended duration
	 */
	public void setExtendedDuration(boolean isExtended) {
		Preconditions.checkArgument(this.getType() == null || !this.getType().isInstant(), "Instant potions cannot be extended!");
		this.extended = isExtended;
	}

	/**
	 * Sets whether this potion is a splash potion. Splash potions can be
	 * thrown for a radius effect.
	 *
	 * @param isSplash Whether this is a splash potion
	 */
	public void setSplash(boolean isSplash) {
		this.splash = isSplash;
	}

	/**
	 * Sets the {@link PotionType} of this potion.
	 *
	 * @param type The new type of this potion
	 */
	public void setType(PotionType type) {
		this.type = type;
	}

	/**
	 * Sets the level of this potion.
	 *
	 * @param level The new level of this potion
	 */
	public void setLevel(int level) {
		Preconditions.checkArgument(this.getType() != null, "No-effect potions don't have a level!");
		Preconditions.checkArgument(level > 0 && level <= 2, "Level must be between 1 and 2 for this potion!");
		this.level = level;
	}

	/**
	 * Converts this potion to an {@link ItemStack} with the specified amount
	 * and a correct damage value.
	 *
	 * @param amount The amount of the ItemStack
	 * @return The created ItemStack
	 */
	public ItemStack toItemStack(int amount) {
		Material material = (this.isSplash() ? Material.SPLASH_POTION : Material.POTION);
		ItemStack itemStack = ItemStack.of(material, amount);
		PotionMeta meta = (PotionMeta)itemStack.getItemMeta();
		meta.setBasePotionData(new PotionData(this.getType(), this.hasExtendedDuration(), this.getLevel() == 2));
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	/**
	 *
	 * @param damage the damage value
	 * @return the produced potion
	 */
	public static Potion fromDamage(int damage) {
		Builder builder = builder();
		PotionType type;

		switch (damage & POTION_BIT) {
			case 1:
				builder.type(type = PotionType.REGEN);
				break;
			case 2:
				builder.type(type = PotionType.SPEED);
				break;
			case 3:
				builder.type(type = PotionType.FIRE_RESISTANCE);
				break;
			case 4:
				builder.type(type = PotionType.POISON);
				break;
			case 5:
				builder.type(type = PotionType.INSTANT_HEAL);
				break;
			case 6:
				builder.type(type = PotionType.NIGHT_VISION);
				break;
			case 8:
				builder.type(type = PotionType.WEAKNESS);
				break;
			case 9:
				builder.type(type = PotionType.STRENGTH);
				break;
			case 10:
				builder.type(type = PotionType.SLOWNESS);
				break;
			case 11:
				builder.type(type = PotionType.JUMP);
				break;
			case 12:
				builder.type(type = PotionType.INSTANT_DAMAGE);
				break;
			case 13:
				builder.type(type = PotionType.WATER_BREATHING);
				break;
			case 14:
				builder.type(type = PotionType.INVISIBILITY);
				break;
			case 0:
			default:
				builder.type(type = PotionType.WATER);
		}

		if (type != PotionType.WATER) {
			int level = ((damage & TIER_BIT) >> TIER_SHIFT) + 1;
			builder.level(level);
		}

		if ((damage & SPLASH_BIT) > 0)
			builder.splash();

		if ((damage & EXTENDED_BIT) > 0)
			builder.extend();

		return builder.build();
	}

	public static Potion fromItemStack(ItemStack item) {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");

		if (item.getType() != Material.POTION)
			throw new IllegalArgumentException("Item is not a potion!");

		return fromDamage(item.getDurability());
	}

	/**
	 * Returns an instance of {@link PotionBrewer}.
	 *
	 * @return An instance of PotionBrewer
	 */
	public static PotionBrewer getBrewer() {
		return Nifty.getServiceManager().getProvider(PotionBrewer.class);
	}

	public static final class Builder implements BuilderCore<Potion> {

		private final Potion potion = new Potion(PotionType.WATER);

		@Override
		public Potion build() {
			return this.potion;
		}

		public Builder extend() {
			return this.extend(true);
		}

		public Builder extend(boolean value) {
			this.potion.setExtendedDuration(value);
			return this;
		}

		public Builder level(int level) {
			this.potion.setLevel(level);
			return this;
		}

		public Builder splash() {
			return this.splash(true);
		}

		public Builder splash(boolean value) {
			this.potion.setSplash(value);
			return this;
		}

		public Builder type(PotionType type) {
			this.potion.setType(type);
			return this;
		}

	}

}