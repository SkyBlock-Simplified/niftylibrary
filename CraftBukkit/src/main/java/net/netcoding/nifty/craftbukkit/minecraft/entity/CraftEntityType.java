package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.EntityType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.craftbukkit.minecraft.entity.block.CraftEnderCrystal;
import net.netcoding.nifty.craftbukkit.minecraft.entity.block.CraftFallingBlock;
import net.netcoding.nifty.craftbukkit.minecraft.entity.explosive.CraftPrimedTNT;
import net.netcoding.nifty.craftbukkit.minecraft.entity.hanging.CraftItemFrame;
import net.netcoding.nifty.craftbukkit.minecraft.entity.hanging.CraftLeashHitch;
import net.netcoding.nifty.craftbukkit.minecraft.entity.hanging.CraftPainting;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.CraftArmorStand;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.CraftVillager;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.complex.CraftComplexEntityPart;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.complex.CraftEnderDragon;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.human.CraftPlayer;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.*;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.arrow.CraftArrow;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.arrow.CraftSpectralArrow;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.arrow.CraftTippedArrow;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive.CraftDragonFireball;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive.CraftLargeFireball;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive.CraftSmallFireball;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive.CraftWitherSkull;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.potion.CraftLingeringPotion;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.potion.CraftSplashPotion;
import net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.CraftBoat;
import net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart.*;
import net.netcoding.nifty.craftbukkit.minecraft.entity.weather.CraftLightningStrike;
import net.netcoding.nifty.craftbukkit.minecraft.entity.weather.CraftWeather;

// TODO
public enum CraftEntityType {

	/**
	 * An item resting on the ground.
	 * <p>
	 * Spawn with {@link World#dropItem(Location, ItemStack)} or {@link
	 * World#dropItemNaturally(Location, ItemStack)}
	 */
	DROPPED_ITEM(EntityType.DROPPED_ITEM, CraftItem.class, "org.bukkit.entity.Item"),
	/**
	 * An experience orb.
	 */
	EXPERIENCE_ORB(EntityType.EXPERIENCE_ORB, CraftExperienceOrb.class,  "org.bukkit.entity.ExperienceOrb"),
	/**
	 * A leash attached to a fencepost.
	 */
	LEASH_HITCH(EntityType.LEASH_HITCH, CraftLeashHitch.class, "org.bukkit.entity.LeashHitch"),
	/**
	 * A painting on a wall.
	 */
	PAINTING(EntityType.PAINTING, CraftPainting.class, "org.bukkit.entity.Painting"),
	/**
	 * An arrow projectile; may get stuck in the ground.
	 */
	ARROW(EntityType.ARROW, CraftArrow.class, "org.bukkit.entity.Arrow"),
	/**
	 * A flying snowball.
	 */
	SNOWBALL(EntityType.SNOWBALL, CraftSnowball.class, "org.bukkit.entity.Snowball"),
	/**
	 * A flying large fireball, as thrown by a Ghast for example.
	 */
	FIREBALL(EntityType.FIREBALL, CraftLargeFireball.class, "org.bukkit.entity.Fireball"),
	/**
	 * A flying small fireball, such as thrown by a Blaze or player.
	 */
	SMALL_FIREBALL(EntityType.SMALL_FIREBALL, CraftSmallFireball.class, "org.bukkit.entity.SmallFireball"),
	/**
	 * A flying ender pearl.
	 */
	ENDER_PEARL(EntityType.ENDER_PEARL, CraftEnderPearl.class, "org.bukkit.entity.EnderPearl"),
	/**
	 * An ender eye signal.
	 */
	ENDER_SIGNAL(EntityType.ENDER_SIGNAL, CraftEnderSignal.class, "org.bukkit.entity.EnderSignal"),
	/**
	 * A flying experience bottle.
	 */
	THROWN_EXP_BOTTLE(EntityType.THROWN_EXP_BOTTLE, CraftThrownExpBottle.class, "org.bukkit.entity.ThrownExpBottle"),
	/**
	 * An item frame on a wall.
	 */
	ITEM_FRAME(EntityType.ITEM_FRAME, CraftItemFrame.class, "org.bukkit.entity.ItemFrame"),
	/**
	 * A flying wither skull projectile.
	 */
	WITHER_SKULL(EntityType.WITHER_SKULL, CraftWitherSkull.class, "org.bukkit.entity.WitherSkull"),
	/**
	 * Primed TNT that is about to explode.
	 */
	PRIMED_TNT(EntityType.PRIMED_TNT, CraftPrimedTNT.class, "org.bukkit.entity.TNTPrimed"),
	/**
	 * A block that is going to or is about to fall.
	 */
	FALLING_BLOCK(EntityType.FALLING_BLOCK, CraftFallingBlock.class, "org.bukkit.entity.FallingBlock"),
	/**
	 * Internal representation of a Firework once it has been launched.
	 */
	FIREWORK(EntityType.FIREWORK, CraftFirework.class, "org.bukkit.entity.Firework"),
	/**
	 * Like {@link #ARROW} but tipped with a specific potion which is applied on contact.
	 */
	TIPPED_ARROW(EntityType.TIPPED_ARROW, CraftTippedArrow.class, "org.bukkit.entity.TippedArrow"),
	/**
	 * Like {@link #TIPPED_ARROW} but causes the {@link PotionEffectType#GLOWING} effect on all team members.
	 */
	SPECTRAL_ARROW(EntityType.SPECTRAL_ARROW, CraftSpectralArrow.class, "org.bukkit.entity.SpectralArrow"),
	/**
	 * Bullet fired by {@link #SHULKER}.
	 */
	SHULKER_BULLET(EntityType.SHULKER_BULLET, CraftShulkerBullet.class, "org.bukkit.entity.ShulkerBullet"),
	/**
	 * Like {@link #FIREBALL} but with added effects.
	 */
	DRAGON_FIREBALL(EntityType.DRAGON_FIREBALL, CraftDragonFireball.class, "org.bukkit.entity.DragonFireball"),
	/**
	 * Mechanical entity with an inventory for placing weapons / armor into.
	 */
	ARMOR_STAND(EntityType.ARMOR_STAND, CraftArmorStand.class, "org.bukkit.entity.ArmorStand"),
	MINECART_COMMAND(EntityType.MINECART_COMMAND, CraftCommandMinecart.class, "org.bukkit.entity.minecart.CommandMinecart"),
	BOAT(EntityType.BOAT, CraftBoat.class, "org.bukkit.entity.Boat"),
	MINECART(EntityType.MINECART, CraftMinecart.class, "org.bukkit.entity.Minecart"),
	MINECART_CHEST(EntityType.MINECART_CHEST, CraftStorageMinecart.class, "org.bukkit.entity.minecart.StorageMinecart"),
	MINECART_FURNACE(EntityType.MINECART_FURNACE, CraftPoweredMinecart.class, "org.bukkit.entity.minecart.PoweredMinecart"),
	MINECART_TNT(EntityType.MINECART_TNT, CraftExplosiveMinecart.class, "org.bukkit.entity.minecart.ExplosiveMinecart"),
	MINECART_HOPPER(EntityType.MINECART_HOPPER, CraftHopperMinecart.class, "org.bukkit.entity.minecart.HopperMinecart"),
	MINECART_MOB_SPAWNER(EntityType.MINECART_MOB_SPAWNER, CraftSpawnerMinecart.class, "org.bukkit.entity.minecart.SpawnerMinecart"),
	CREEPER(EntityType.CREEPER, null, "org.bukkit.entity.Creeper"),
	SKELETON(EntityType.SKELETON, null, "org.bukkit.entity.Skeleton"),
	SPIDER(EntityType.SPIDER, null, "org.bukkit.entity.Spider"),
	GIANT(EntityType.GIANT, null, "org.bukkit.entity.Giant"),
	ZOMBIE(EntityType.ZOMBIE, null, "org.bukkit.entity.Zombie"),
	SLIME(EntityType.SLIME, null, "org.bukkit.entity.Slime"),
	GHAST(EntityType.GHAST, null, "org.bukkit.entity.Ghast"),
	PIG_ZOMBIE(EntityType.PIG_ZOMBIE, null, "org.bukkit.entity.PigZombie"),
	ENDERMAN(EntityType.ENDERMAN, null, "org.bukkit.entity.Enderman"),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, null, "org.bukkit.entity.CaveSpider"),
	SILVERFISH(EntityType.SILVERFISH, null, "org.bukkit.entity.Silverfish"),
	BLAZE(EntityType.BLAZE, null, "org.bukkit.entity.Blaze"),
	MAGMA_CUBE(EntityType.MAGMA_CUBE, null, "org.bukkit.entity.MagmaCube"),
	ENDER_DRAGON(EntityType.ENDER_DRAGON, CraftEnderDragon.class, "org.bukkit.entity.EnderDragon"),
	WITHER(EntityType.WITHER, null, "org.bukkit.entity.Wither"),
	BAT(EntityType.BAT, null, "org.bukkit.entity.Bat"),
	WITCH(EntityType.WITCH, null, "org.bukkit.entity.Witch"),
	ENDERMITE(EntityType.ENDERMITE, null, "org.bukkit.entity.Endermite"),
	GUARDIAN(EntityType.GUARDIAN, null, "org.bukkit.entity.Guardian"),
	SHULKER(EntityType.SHULKER, null, "org.bukkit.entity.Shulker"),
	PIG(EntityType.PIG, null, "org.bukkit.entity.Pig"),
	SHEEP(EntityType.SHEEP, null, "org.bukkit.entity.Sheep"),
	COW(EntityType.COW, null, "org.bukkit.entity.Cow"),
	CHICKEN(EntityType.CHICKEN, null, "org.bukkit.entity.Chicken"),
	SQUID(EntityType.SQUID, null, "org.bukkit.entity.Squid"),
	WOLF(EntityType.WOLF, null, "org.bukkit.entity.Wolf"),
	MUSHROOM_COW(EntityType.MUSHROOM_COW, null, "org.bukkit.entity.MushroomCow"),
	SNOWMAN(EntityType.SNOWMAN, null, "org.bukkit.entity.Snowman"),
	SNOW_GOLEM(EntityType.SNOW_GOLEM, null, "org.bukkit.entity.Snowman"),
	OCELOT(EntityType.OCELOT, null, "org.bukkit.entity.Ocelot"),
	IRON_GOLEM(EntityType.IRON_GOLEM, null, "org.bukkit.entity.IronGolem"),
	HORSE(EntityType.HORSE, null, "org.bukkit.entity.Horse"),
	RABBIT(EntityType.RABBIT, null, "org.bukkit.entity.Rabbit"),
	POLAR_BEAR(EntityType.POLAR_BEAR, null, "org.bukkit.entity.PolarBear"),
	VILLAGER(EntityType.VILLAGER, CraftVillager.class, "org.bukkit.entity.Villager"),
	ENDER_CRYSTAL(EntityType.ENDER_CRYSTAL, CraftEnderCrystal.class, "org.bukkit.entity.EnderCrystal"),
	/**
	 * A flying splash potion
	 */
	SPLASH_POTION(EntityType.SPLASH_POTION, CraftSplashPotion.class, "org.bukkit.entity.SplashPotion"),
	/**
	 * A flying lingering potion
	 */
	LINGERING_POTION(EntityType.LINGERING_POTION, CraftLingeringPotion.class, "org.bukkit.entity.LingeringPotion"),
	AREA_EFFECT_CLOUD(EntityType.AREA_EFFECT_CLOUD, CraftAreaEffectCloud.class, "org.bukkit.entity.AreaEffectCloud"),
	/**
	 * A flying chicken egg.
	 */
	EGG(EntityType.EGG, CraftEgg.class, "org.bukkit.entity.Egg"),
	/**
	 * A fishing line and bobber.
	 */
	FISHING_HOOK(EntityType.FISHING_HOOK, CraftFishHook.class, "org.bukkit.entity.FishHook"),
	/**
	 * A bolt of lightning.
	 * <p>
	 * Spawn with {@link World#strikeLightning(Location)}.
	 */
	LIGHTNING(EntityType.LIGHTNING, CraftLightningStrike.class, "org.bukkit.entity.LightningStrike"),
	WEATHER(EntityType.WEATHER, CraftWeather.class, "org.bukkit.entity.Weather"),
	PLAYER(EntityType.PLAYER, CraftPlayer.class, "org.bukkit.entity.Player"),
	COMPLEX_PART(EntityType.COMPLEX_PART, CraftComplexEntityPart.class, "org.bukkit.entity.ComplexEntityPart"),
	/**
	 * An unknown entity without an Entity Class
	 */
	UNKNOWN(EntityType.UNKNOWN, null, null);

	private final EntityType type;
	private final Class<? extends CraftEntity> clazz;
	private final Class<? extends org.bukkit.entity.Entity> bukkitClazz;

	@SuppressWarnings("unchecked")
	CraftEntityType(EntityType type, Class<? extends CraftEntity> clazz, String bukkitEntityPath) {
		this.type = type;
		this.clazz = clazz;
		Class<? extends org.bukkit.entity.Entity> bukkitClazz = null;

		if (StringUtil.notEmpty(bukkitEntityPath)) {
			try {
				bukkitClazz = (Class<? extends org.bukkit.entity.Entity>)Class.forName(bukkitEntityPath);
				new Reflection(type.getClass()).setValue(Boolean.class, type, true);
			} catch (ClassNotFoundException ignore) { }
		}

		this.bukkitClazz = bukkitClazz;
	}

	public Class<? extends org.bukkit.entity.Entity> getBukkitClass() {
		return this.bukkitClazz;
	}

	public static CraftEntityType getByBukkitClass(Class<? extends org.bukkit.entity.Entity> bukkitClazz) {
		for (CraftEntityType type : values()) {
			if (!type.getType().isSupported())
				continue;

			if (type.getBukkitClass().isAssignableFrom(bukkitClazz))
				return type;
		}

		return CraftEntityType.UNKNOWN;
	}

	public static CraftEntityType getByClass(Class<? extends Entity> entity) {
		for (CraftEntityType type : values()) {
			if (type.getEntityClass().isAssignableFrom(entity))
				return type;
		}

		return null;
	}

	public Class<? extends CraftEntity> getEntityClass() {
		return this.clazz;
	}

	public EntityType getType() {
		return this.type;
	}

}