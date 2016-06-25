package net.netcoding.nifty.common.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.block.EnderCrystal;
import net.netcoding.nifty.common.minecraft.entity.block.FallingBlock;
import net.netcoding.nifty.common.minecraft.entity.explosive.PrimedTNT;
import net.netcoding.nifty.common.minecraft.entity.hanging.ItemFrame;
import net.netcoding.nifty.common.minecraft.entity.hanging.LeashHitch;
import net.netcoding.nifty.common.minecraft.entity.hanging.Painting;
import net.netcoding.nifty.common.minecraft.entity.living.Ageable;
import net.netcoding.nifty.common.minecraft.entity.living.ArmorStand;
import net.netcoding.nifty.common.minecraft.entity.living.Bat;
import net.netcoding.nifty.common.minecraft.entity.living.Hostile;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.minecraft.entity.living.Villager;
import net.netcoding.nifty.common.minecraft.entity.living.animal.*;
import net.netcoding.nifty.common.minecraft.entity.living.complex.ComplexEntityPart;
import net.netcoding.nifty.common.minecraft.entity.living.complex.EnderDragon;
import net.netcoding.nifty.common.minecraft.entity.living.golem.IronGolem;
import net.netcoding.nifty.common.minecraft.entity.living.golem.Shulker;
import net.netcoding.nifty.common.minecraft.entity.living.golem.SnowGolem;
import net.netcoding.nifty.common.minecraft.entity.living.monster.*;
import net.netcoding.nifty.common.minecraft.entity.projectile.*;
import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.DragonFireball;
import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.LargeFireball;
import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.SmallFireball;
import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.WitherSkull;
import net.netcoding.nifty.common.minecraft.entity.vehicle.Boat;
import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.CommandMinecart;
import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.ExplosiveMinecart;
import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.HopperMinecart;
import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.PoweredMinecart;
import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.RideableMinecart;
import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.SpawnerMinecart;
import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.StorageMinecart;
import net.netcoding.nifty.common.minecraft.entity.weather.LightningStrike;
import net.netcoding.nifty.common.minecraft.entity.weather.Weather;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum EntityType {

	// These strings MUST match the strings in nms.EntityTypes and are case sensitive.
	/**
	 * An item resting on the ground.
	 * <p>
	 * Spawn with {@link World#dropItem(Location, ItemStack)} or {@link
	 * World#dropItemNaturally(Location, ItemStack)}
	 */
	DROPPED_ITEM("Item", Item.class, 1, false),
	/**
	 * An experience orb.
	 */
	EXPERIENCE_ORB("XPOrb", ExperienceOrb.class, 2),
	/**
	 * A leash attached to a fencepost.
	 */
	LEASH_HITCH("LeashKnot", LeashHitch.class, 8),
	/**
	 * A painting on a wall.
	 */
	PAINTING("Painting", Painting.class, 9),
	/**
	 * An arrow projectile; may get stuck in the ground.
	 */
	ARROW("Arrow", Arrow.class, 10),
	/**
	 * A flying snowball.
	 */
	SNOWBALL("Snowball", Snowball.class, 11),
	/**
	 * A flying large fireball, as thrown by a Ghast for example.
	 */
	FIREBALL("Fireball", LargeFireball.class, 12),
	/**
	 * A flying small fireball, such as thrown by a Blaze or player.
	 */
	SMALL_FIREBALL("SmallFireball", SmallFireball.class, 13),
	/**
	 * A flying ender pearl.
	 */
	ENDER_PEARL("ThrownEnderpearl", EnderPearl.class, 14),
	/**
	 * An ender eye signal.
	 */
	ENDER_SIGNAL("EyeOfEnderSignal", EnderSignal.class, 15),
	/**
	 * A flying experience bottle.
	 */
	THROWN_EXP_BOTTLE("ThrownExpBottle", ThrownExpBottle.class, 17),
	/**
	 * An item frame on a wall.
	 */
	ITEM_FRAME("ItemFrame", ItemFrame.class, 18),
	/**
	 * A flying wither skull projectile.
	 */
	WITHER_SKULL("WitherSkull", WitherSkull.class, 19),
	/**
	 * Primed TNT that is about to explode.
	 */
	PRIMED_TNT("PrimedTnt", PrimedTNT.class, 20),
	/**
	 * A block that is going to or is about to fall.
	 */
	FALLING_BLOCK("FallingSand", FallingBlock.class, 21, false),
	/**
	 * Internal representation of a Firework once it has been launched.
	 */
	FIREWORK("FireworksRocketEntity", Firework.class, 22, false),
	/**
	 * Like {@link #ARROW} but tipped with a specific potion which is applied on contact.
	 */
	TIPPED_ARROW("TippedArrow", TippedArrow.class, 23),
	/**
	 * Like {@link #TIPPED_ARROW} but causes the {@link PotionEffectType#GLOWING} effect on all team members.
	 */
	SPECTRAL_ARROW("SpectralArrow", SpectralArrow.class, 24),
	/**
	 * Bullet fired by {@link #SHULKER}.
	 */
	SHULKER_BULLET("ShulkerBullet", ShulkerBullet.class, 25),
	/**
	 * Like {@link #FIREBALL} but with added effects.
	 */
	DRAGON_FIREBALL("DragonFireball", DragonFireball.class, 26),
	/**
	 * Mechanical entity with an inventory for placing weapons / armor into.
	 */
	ARMOR_STAND("ArmorStand", ArmorStand.class, 30),
	MINECART_COMMAND("MinecartCommandBlock", CommandMinecart.class, 40),
	BOAT("Boat", Boat.class, 41),
	MINECART("MinecartRideable", RideableMinecart.class, 42),
	MINECART_CHEST("MinecartChest", StorageMinecart.class, 43),
	MINECART_FURNACE("MinecartFurnace", PoweredMinecart.class, 44),
	MINECART_TNT("MinecartTNT", ExplosiveMinecart.class, 45),
	MINECART_HOPPER("MinecartHopper", HopperMinecart.class, 46),
	MINECART_MOB_SPAWNER("MinecartMobSpawner", SpawnerMinecart.class, 47),
	CREEPER("Creeper", Creeper.class, 50),
	SKELETON("Skeleton", Skeleton.class, 51),
	SPIDER("Spider", Spider.class, 52),
	GIANT("Giant", Giant.class, 53),
	ZOMBIE("Zombie", Zombie.class, 54),
	SLIME("Slime", Slime.class, 55),
	GHAST("Ghast", Ghast.class, 56),
	PIG_ZOMBIE("PigZombie", PigZombie.class, 57),
	ENDERMAN("Enderman", Enderman.class, 58),
	CAVE_SPIDER("CaveSpider", CaveSpider.class, 59),
	SILVERFISH("Silverfish", Silverfish.class, 60),
	BLAZE("Blaze", Blaze.class, 61),
	MAGMA_CUBE("LavaSlime", MagmaCube.class, 62),
	ENDER_DRAGON("EnderDragon", EnderDragon.class, 63),
	WITHER("WitherBoss", Wither.class, 64),
	BAT("Bat", Bat.class, 65),
	WITCH("Witch", Witch.class, 66),
	ENDERMITE("Endermite", Endermite.class, 67),
	GUARDIAN("Guardian", Guardian.class, 68),
	SHULKER("Shulker", Shulker.class, 69),
	PIG("Pig", Pig.class, 90),
	SHEEP("Sheep", Sheep.class, 91),
	COW("Cow", Cow.class, 92),
	CHICKEN("Chicken", Chicken.class, 93),
	SQUID("Squid", Squid.class, 94),
	WOLF("Wolf", Wolf.class, 95),
	MUSHROOM_COW("MushroomCow", MushroomCow.class, 96),
	SNOWMAN("SnowMan", SnowGolem.class, 97),
	SNOW_GOLEM("SnowMan", SnowGolem.class, 97),
	OCELOT("Ozelot", Ocelot.class, 98),
	IRON_GOLEM("VillagerGolem", IronGolem.class, 99),
	HORSE("EntityHorse", Horse.class, 100),
	RABBIT("Rabbit", Rabbit.class, 101),
	POLAR_BEAR("PolarBear", PolarBear.class, 102),
	VILLAGER("Villager", Villager.class, 120),
	ENDER_CRYSTAL("EnderCrystal", EnderCrystal.class, 200),
	/**
	 * A flying splash potion
	 */
	SPLASH_POTION(null, SplashPotion.class, -1, false),
	/**
	 * A flying lingering potion
	 */
	LINGERING_POTION(null, LingeringPotion.class, -1, false),
	AREA_EFFECT_CLOUD(null, AreaEffectCloud.class, -1),
	/**
	 * A flying chicken egg.
	 */
	EGG(null, Egg.class, -1, false),
	/**
	 * A fishing line and bobber.
	 */
	FISHING_HOOK(null, Fish.class, -1, false),
	/**
	 * A bolt of lightning.
	 * <p>
	 * Spawn with {@link World#strikeLightning(Location)}.
	 */
	LIGHTNING(null, LightningStrike.class, -1, false),
	WEATHER(null, Weather.class, -1, false),
	PLAYER(null, Player.class, -1, false),
	COMPLEX_PART(null, ComplexEntityPart.class, -1, false),
	/**
	 * An unknown entity without an Entity Class
	 */
	UNKNOWN(null, null, -1, false);

	private static final ConcurrentMap<String, EntityType> BY_NAME = Concurrent.newMap();
	private static final ConcurrentMap<Short, EntityType> BY_ID = Concurrent.newMap();

	private final String name;
	private final Class<? extends Entity> clazz;
	private final short typeId;
	private final Behavior behavior;
	private final boolean independent;
	private final boolean living;

	static {
		for (EntityType type : values()) {
			if (type.getName() != null)
				BY_NAME.put(type.getName().toLowerCase(), type);

			if (type.getTypeId() > 0)
				BY_ID.put(type.getTypeId(), type);
		}
	}

	EntityType(String name, Class<? extends Entity> clazz, int typeId) {
		this(name, clazz, typeId, true);
	}

	EntityType(String name, Class<? extends Entity> clazz, int typeId, boolean independent) {
		this.name = name;
		this.clazz = clazz;
		this.typeId = (short) typeId;
		this.independent = independent;

		if (clazz != null) {
			this.living = LivingEntity.class.isAssignableFrom(clazz);

			if (Ageable.class.isAssignableFrom(clazz))
				this.behavior = Behavior.FRIENDLY;
			else if (Hostile.class.isAssignableFrom(clazz))
				this.behavior = Behavior.HOSTILE;
			else
				this.behavior = Behavior.NEUTRAL;
		} else {
			this.living = false;
			this.behavior = Behavior.NEUTRAL;
		}
	}

	/**
	 * Gets an entity by name.
	 *
	 * @param name The entity type's name.
	 * @return The matching entity type or null.
	 */
	public static EntityType getByName(String name) {
		return StringUtil.isEmpty(name) ? null : BY_NAME.get(name.toLowerCase());
	}

	/**
	 * Gets an entity by its id.
	 *
	 * @param id The raw type id.
	 * @return The matching entity type or null.
	 */
	public static EntityType getById(int id) {
		return (id > Short.MAX_VALUE ? null : BY_ID.get((short)id));
	}

	public Class<? extends Entity> getEntityClass() {
		return this.clazz;
	}

	/**
	 * Gets the entitys name.
	 *
	 * @return the entity type's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the type id.
	 *
	 * @return The raw type id.
	 */
	public short getTypeId() {
		return typeId;
	}

	/**
	 * Some entities cannot be spawned using {@link
	 * World#spawnEntity(Location, EntityType)} or {@link
	 * World#spawn(Location, Class)}, usually because they require additional
	 * information in order to spawn.
	 *
	 * @return False if the entity type cannot be spawned
	 */
	public boolean isSpawnable() {
		return independent;
	}

	public boolean isAlive() {
		return living;
	}

	public enum Behavior {

		FRIENDLY("friendly"),
		NEUTRAL("neutral"),
		HOSTILE("hostile");

		private final String type;

		Behavior(String type) {
			this.type = type;
		}

		public String getType() {
			return this.type;
		}

	}

}