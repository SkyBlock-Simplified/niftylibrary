package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.Particle;
import net.netcoding.nifty.common.minecraft.entity.AreaEffectCloud;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.ProjectileSource;
import net.netcoding.nifty.common.minecraft.potion.PotionData;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.core.api.color.Color;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.List;

public final class CraftAreaEffectCloud extends CraftEntity implements AreaEffectCloud {

	public CraftAreaEffectCloud(org.bukkit.entity.AreaEffectCloud areaEffectCloud) {
		super(areaEffectCloud);
	}

	@Override
	public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
		return this.getHandle().addCustomEffect(CraftConverter.toBukkitEffect(effect), overwrite);
	}

	@Override
	public void clearCustomEffects() {
		this.getHandle().clearCustomEffects();
	}

	@Override
	public PotionData getBasePotionData() {
		return CraftConverter.fromBukkitData(this.getHandle().getBasePotionData());
	}

	@Override
	public Color getColor() {
		return Color.fromRGB(this.getHandle().getColor().asRGB());
	}

	@Override
	public List<PotionEffect> getCustomEffects() {
		return this.getHandle().getCustomEffects().stream().map(CraftConverter::fromBukkitEffect).collect(Concurrent.toList());
	}

	@Override
	public int getDuration() {
		return this.getHandle().getDuration();
	}

	@Override
	public int getDurationOnUse() {
		return this.getHandle().getDurationOnUse();
	}

	@Override
	public org.bukkit.entity.AreaEffectCloud getHandle() {
		return (org.bukkit.entity.AreaEffectCloud)super.getHandle();
	}

	@Override
	public Particle getParticle() {
		return Particle.valueOf(this.getHandle().getParticle().name());
	}

	@Override
	public float getRadius() {
		return this.getHandle().getRadius();
	}

	@Override
	public float getRadiusOnUse() {
		return this.getHandle().getRadiusOnUse();
	}

	@Override
	public float getRadiusPerTick() {
		return this.getHandle().getRadiusPerTick();
	}

	@Override
	public int getReapplicationDelay() {
		return this.getHandle().getReapplicationDelay();
	}

	@Override
	public ProjectileSource getSource() {
		return CraftConverter.fromBukkitSource(this.getHandle().getSource());
	}

	@Override
	public int getWaitTime() {
		return this.getHandle().getWaitTime();
	}

	@Override
	public boolean removeCustomEffect(PotionEffectType type) {
		return this.getHandle().removeCustomEffect(org.bukkit.potion.PotionEffectType.getByName(type.getName()));
	}

	@Override
	public void setBasePotionData(PotionData data) {
		this.getHandle().setBasePotionData(CraftConverter.toBukkitData(data));
	}

	@Override
	public void setColor(Color color) {
		this.getHandle().setColor(org.bukkit.Color.fromRGB(color.asRGB()));
	}

	@Override
	public void setDuration(int duration) {
		this.getHandle().setDuration(duration);
	}

	@Override
	public void setDurationOnUse(int duration) {
		this.getHandle().setDurationOnUse(duration);
	}

	@Override
	public void setParticle(Particle particle) {
		this.getHandle().setParticle(org.bukkit.Particle.valueOf(particle.name()));
	}

	@Override
	public void setRadius(float radius) {
		this.getHandle().setRadius(radius);
	}

	@Override
	public void setRadiusOnUse(float radius) {
		this.getHandle().setRadiusOnUse(radius);
	}

	@Override
	public void setRadiusPerTick(float radius) {
		this.getHandle().setRadiusPerTick(radius);
	}

	@Override
	public void setReapplicationDelay(int delay) {
		this.getHandle().setReapplicationDelay(delay);
	}

	@Override
	public void setSource(ProjectileSource source) {
		this.getHandle().setSource(CraftConverter.toBukkitSource(source));
	}

	@Override
	public void setWaitTime(int waitTime) {
		this.getHandle().setWaitTime(waitTime);
	}

}