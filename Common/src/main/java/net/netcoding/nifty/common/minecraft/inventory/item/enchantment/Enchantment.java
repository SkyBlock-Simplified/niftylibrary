package net.netcoding.nifty.common.minecraft.inventory.item.enchantment;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class Enchantment {

	public static final Enchantment PROTECTION_ENVIRONMENTAL = new EnchantmentWrapper(0, "protection", "PROTECTION_ENVIRONMENTAL", EnchantmentTarget.ARMOR);
	public static final Enchantment PROTECTION_FIRE = new EnchantmentWrapper(1, "fire_protection", "PROTECTION_FIRE", EnchantmentTarget.ARMOR);
	public static final Enchantment PROTECTION_FALL = new EnchantmentWrapper(2, "feather_falling", "PROTECTION_FALL", EnchantmentTarget.ARMOR);
	public static final Enchantment PROTECTION_EXPLOSIONS = new EnchantmentWrapper(3, "blast_protection", "PROTECTION_EXPLOSIONS", EnchantmentTarget.ARMOR);
	public static final Enchantment PROTECTION_PROJECTILE = new EnchantmentWrapper(4, "projectile_protection", "PROTECTION_PROJECTILE", EnchantmentTarget.ARMOR);
	public static final Enchantment OXYGEN = new EnchantmentWrapper(5, "respiration", "OXYGEN", EnchantmentTarget.ARMOR);
	public static final Enchantment WATER_WORKER = new EnchantmentWrapper(6, "aqua_affinity", "WATER_WORKER", EnchantmentTarget.ARMOR);
	public static final Enchantment THORNS = new EnchantmentWrapper(7, "thorns", "THORNS", EnchantmentTarget.ARMOR);
	public static final Enchantment DEPTH_STRIDER = new EnchantmentWrapper(8, "depth_strider", "DEPTH_STRIDER", EnchantmentTarget.ARMOR);
	public static final Enchantment FROST_WALKER = new EnchantmentWrapper(9, "frost_walker", "FROST_WALKER", EnchantmentTarget.ARMOR_FEET);
	public static final Enchantment DAMAGE_ALL = new EnchantmentWrapper(16, "sharpness", "DAMAGE_ALL", EnchantmentTarget.WEAPON);
	public static final Enchantment DAMAGE_UNDEAD = new EnchantmentWrapper(17, "smite", "DAMAGE_UNDEAD", EnchantmentTarget.WEAPON);
	public static final Enchantment DAMAGE_ARTHROPODS = new EnchantmentWrapper(18, "bane_of_arthropods", "DAMAGE_ARTHROPODS", EnchantmentTarget.WEAPON);
	public static final Enchantment KNOCKBACK = new EnchantmentWrapper(19, "knockback", "KNOCKBACK", EnchantmentTarget.WEAPON);
	public static final Enchantment FIRE_ASPECT = new EnchantmentWrapper(20, "fire_aspect", "FIRE_ASPECT", EnchantmentTarget.WEAPON);
	public static final Enchantment LOOT_BONUS_MOBS = new EnchantmentWrapper(21, "looting", "LOOT_BONUS_MOBS", EnchantmentTarget.WEAPON);
	public static final Enchantment DIG_SPEED = new EnchantmentWrapper(32, "efficiency", "DIG_SPEED", EnchantmentTarget.WEAPON);
	public static final Enchantment SILK_TOUCH = new EnchantmentWrapper(33, "silk_touch", "SILK_TOUCH", EnchantmentTarget.WEAPON);
	public static final Enchantment DURABILITY = new EnchantmentWrapper(34, "unbreaking", "DURABILITY", EnchantmentTarget.WEAPON);
	public static final Enchantment LOOT_BONUS_BLOCKS = new EnchantmentWrapper(35, "fortune", "LOOT_BONUS_BLOCKS", EnchantmentTarget.WEAPON);
	public static final Enchantment ARROW_DAMAGE = new EnchantmentWrapper(48, "power", "ARROW_DAMAGE", EnchantmentTarget.WEAPON);
	public static final Enchantment ARROW_KNOCKBACK = new EnchantmentWrapper(49, "punch", "ARROW_KNOCKBACK", EnchantmentTarget.WEAPON);
	public static final Enchantment ARROW_FIRE = new EnchantmentWrapper(50, "flame", "ARROW_FIRE", EnchantmentTarget.WEAPON);
	public static final Enchantment ARROW_INFINITE = new EnchantmentWrapper(51, "infinity", "ARROW_INFINITE", EnchantmentTarget.WEAPON);
	public static final Enchantment LUCK = new EnchantmentWrapper(61, "luck_of_the_sea", "LUCK", EnchantmentTarget.WEAPON);
	public static final Enchantment LURE = new EnchantmentWrapper(62, "lure", "LURE", EnchantmentTarget.WEAPON);
	public static final Enchantment MENDING = new EnchantmentWrapper(70, "mending", "MENDING", EnchantmentTarget.ALL);
	static final ConcurrentMap<Integer, Enchantment> BY_ID = new ConcurrentMap<>();
	static final ConcurrentMap<String, Enchantment> BY_NAME = new ConcurrentMap<>();
	static final ConcurrentMap<Integer, Enchantment> ORIGINAL = new ConcurrentMap<>();
	private static boolean acceptingNew;
	private final int id;

	protected Enchantment(int id) {
		this.id = id;
	}

	public abstract boolean canEnchant(ItemStack item);

	public abstract boolean conflictsWith(Enchantment enchantment);

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (!(obj instanceof Enchantment))
			return false;
		else {
			Enchantment other = (Enchantment)obj;
			return this.getId() == other.getId();
		}
	}

	public static Enchantment getById(int id) {
		return BY_ID.get(id);
	}

	public static Enchantment getByName(String name) {
		return BY_NAME.get(name);
	}

	public final int getId() {
		return this.id;
	}

	public String getKey() {
		return ORIGINAL.get(this.getId()).getKey();
	}

	public EnchantmentTarget getTarget() {
		return ORIGINAL.get(this.getId()).getTarget();
	}

	public abstract int getMaxLevel();

	public String getName() {
		return ORIGINAL.get(this.getId()).getName();
	}

	public final int getStartLevel() {
		return 1;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}

	public static boolean isAcceptingRegistrations() {
		return acceptingNew;
	}

	public static void registerEnchantment(Enchantment enchantment) {
		if (!BY_ID.containsKey(enchantment.getId()) && !BY_NAME.containsKey(enchantment.getName())) {
			if (!isAcceptingRegistrations())
				throw new IllegalStateException("No longer accepting new enchantments! (Only NiftyBukkit implementation)");

			BY_ID.put(enchantment.getId(), enchantment);
			BY_NAME.put(enchantment.getName(), enchantment);
		} else
			throw new IllegalArgumentException(StringUtil.format("Enchantment ''{0}'' already registered!", enchantment.getName()));
	}

	public static void stopAcceptingRegistrations() {
		acceptingNew = false;
	}

	@Override
	public String toString() {
		return "Enchantment[" + this.getId() + ", " + this.getName() + "]";
	}

	public static Enchantment[] values() {
		return ListUtil.toArray(BY_ID.values(), Enchantment.class);
	}

}