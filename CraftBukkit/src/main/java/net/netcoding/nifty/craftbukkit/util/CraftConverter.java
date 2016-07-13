package net.netcoding.nifty.craftbukkit.util;

import net.netcoding.nifty.common.minecraft.FireworkEffect;
import net.netcoding.nifty.common.minecraft.block.state.Banner;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.ProjectileSource;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.minecraft.potion.PotionData;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.potion.PotionType;
import net.netcoding.nifty.core.api.color.Color;
import net.netcoding.nifty.core.api.color.DyeColor;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.misc.Vector;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.CraftLivingEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.source.CraftBlockProjectileSource;

public final class CraftConverter {

	public static PotionData fromBukkitData(org.bukkit.potion.PotionData bukkitData) {
		return new PotionData(PotionType.valueOf(bukkitData.getType().name()), bukkitData.isExtended(), bukkitData.isUpgraded());
	}

	public static FireworkEffect fromBukkitEffect(org.bukkit.FireworkEffect bukkitEffect) {
		return FireworkEffect.builder()
				.with(FireworkEffect.Type.valueOf(bukkitEffect.getType().name()))
				.flicker(bukkitEffect.hasFlicker())
				.trail(bukkitEffect.hasTrail())
				.withColor(bukkitEffect.getColors().stream().map(color -> Color.fromRGB(color.asRGB())).collect(Concurrent.toList()))
				.withFade(bukkitEffect.getFadeColors().stream().map(color -> Color.fromRGB(color.asRGB())).collect(Concurrent.toList()))
				.build();
	}

	public static PotionEffect fromBukkitEffect(org.bukkit.potion.PotionEffect bukkitEffect) {
		return new PotionEffect(
				PotionEffectType.getByName(bukkitEffect.getType().getName()),
				bukkitEffect.getDuration(), bukkitEffect.getAmplifier(), bukkitEffect.isAmbient(),
				bukkitEffect.hasParticles(), Color.fromRGB(bukkitEffect.getColor().asRGB())
		);
	}

	public static Enchantment fromBukkitEnchant(org.bukkit.enchantments.Enchantment bukkitEnchant) {
		return Enchantment.getByName(bukkitEnchant.getName());
	}

	public static Vector fromBukkitEuler(org.bukkit.util.EulerAngle bukkitEuler) {
		return new Vector(bukkitEuler.getX(), bukkitEuler.getY(), bukkitEuler.getZ());
	}

	public static Banner.Pattern fromBukkitPattern(org.bukkit.block.banner.Pattern bukkitPattern) {
		return new Banner.Pattern(DyeColor.valueOf(bukkitPattern.getColor().name()), Banner.PatternType.getByIdentifier(bukkitPattern.getPattern().getIdentifier()));
	}

	public static ProjectileSource fromBukkitSource(org.bukkit.projectiles.ProjectileSource bukkitSource) {
		return (bukkitSource instanceof org.bukkit.entity.LivingEntity) ?
				CraftEntity.convertBukkitEntity((org.bukkit.entity.LivingEntity)bukkitSource, LivingEntity.class) :
				new CraftBlockProjectileSource((org.bukkit.projectiles.BlockProjectileSource)bukkitSource);
	}

	public static Vector fromBukkitVector(org.bukkit.util.Vector bukkitVector) {
		return new Vector(bukkitVector.getX(), bukkitVector.getY(), bukkitVector.getZ());
	}

	public static org.bukkit.potion.PotionData toBukkitData(PotionData data) {
		return new org.bukkit.potion.PotionData(org.bukkit.potion.PotionType.valueOf(data.getType().name()), data.isExtended(), data.isUpgraded());
	}

	public static org.bukkit.FireworkEffect toBukkitEffect(FireworkEffect effect) {
		return org.bukkit.FireworkEffect.builder()
				.with(org.bukkit.FireworkEffect.Type.valueOf(effect.getType().name()))
				.flicker(effect.hasFlicker())
				.trail(effect.hasTrail())
				.withColor(effect.getColors().stream().map(color -> org.bukkit.Color.fromRGB(color.asRGB())).collect(Concurrent.toList()))
				.withFade(effect.getFadeColors().stream().map(color -> org.bukkit.Color.fromRGB(color.asRGB())).collect(Concurrent.toList()))
				.build();
	}

	public static org.bukkit.potion.PotionEffect toBukkitEffect(PotionEffect effect) {
		return new org.bukkit.potion.PotionEffect(
				org.bukkit.potion.PotionEffectType.getByName(effect.getType().getName()),
				effect.getDuration(), effect.getAmplifier(), effect.isAmbient(),
				effect.hasParticles(), org.bukkit.Color.fromRGB(effect.getColor().asRGB())
		);
	}

	public static org.bukkit.enchantments.Enchantment toBukkitEnchant(Enchantment enchant) {
		return org.bukkit.enchantments.Enchantment.getByName(enchant.getName());
	}

	public static org.bukkit.util.EulerAngle toBukkitEuler(Vector vector) {
		return new org.bukkit.util.EulerAngle(vector.getX(), vector.getY(), vector.getZ());
	}

	public static org.bukkit.block.banner.Pattern toBukkitPattern(Banner.Pattern pattern) {
		return new org.bukkit.block.banner.Pattern(org.bukkit.DyeColor.valueOf(pattern.getColor().name()), org.bukkit.block.banner.PatternType.getByIdentifier(pattern.getPattern().getIdentifier()));
	}

	public static org.bukkit.projectiles.ProjectileSource toBukkitSource(ProjectileSource source) {
		return (source instanceof LivingEntity) ? ((CraftLivingEntity)source).getHandle() : ((CraftBlockProjectileSource)source).getHandle();
	}

	public static org.bukkit.util.Vector toBukkitVector(Vector vector) {
		return new org.bukkit.util.Vector(vector.getX(), vector.getY(), vector.getZ());
	}

}