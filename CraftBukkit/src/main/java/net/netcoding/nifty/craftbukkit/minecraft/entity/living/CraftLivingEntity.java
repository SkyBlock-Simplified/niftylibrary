package net.netcoding.nifty.craftbukkit.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.common.minecraft.inventory.EntityEquipment;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import net.netcoding.nifty.core.util.misc.Vector;
import net.netcoding.nifty.craftbukkit.minecraft.block.CraftBlock;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntityType;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.human.CraftPlayer;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftEntityEquipment;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class CraftLivingEntity extends CraftEntity implements LivingEntity {

	protected CraftLivingEntity(org.bukkit.entity.LivingEntity entity) {
		super(entity);
	}

	@Override
	public boolean addPotionEffect(PotionEffect effect, boolean force) {
		return this.getHandle().addPotionEffect(CraftConverter.toBukkitEffect(effect));
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return this.getHandle().getActivePotionEffects().stream().map(CraftConverter::fromBukkitEffect).collect(Concurrent.toSet());
	}

	@Override
	public void damage(double amount, Entity source) {
		this.getHandle().damage(amount, ((CraftEntity)source).getHandle());
	}

	@Override
	public boolean getCanPickupItems() {
		return this.getHandle().getCanPickupItems();
	}

	@Override
	public EntityEquipment getEquipment() {
		return new CraftEntityEquipment(this.getHandle().getEquipment());
	}

	@Override
	public double getEyeHeight(boolean ignoreSneaking) {
		return this.getHandle().getEyeHeight(ignoreSneaking);
	}

	@Override
	public Location getEyeLocation() {
		return new CraftLocation(this.getHandle().getEyeLocation());
	}

	@Override
	public org.bukkit.entity.LivingEntity getHandle() {
		return (org.bukkit.entity.LivingEntity)super.getHandle();
	}

	@Override
	public double getHealth() {
		return this.getHandle().getHealth();
	}

	@Override
	public Player getKiller() {
		return new CraftPlayer(this.getHandle().getKiller());
	}

	@Override
	public double getLastDamage() {
		return this.getHandle().getLastDamage();
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> materials, int distance) {
		ConcurrentSet<org.bukkit.Material> bukkitMaterials = materials.stream().map(material -> org.bukkit.Material.valueOf(material.name())).collect(Concurrent.toSet());
		return this.getHandle().getLastTwoTargetBlocks(bukkitMaterials, distance).stream().map(CraftBlock::new).collect(Concurrent.toList());
	}

	@Override
	public Entity getLeashHolder() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getLeashHolder());
	}

	@Override
	public List<Block> getLineOfSight(Set<Material> materials, int distance) {
		ConcurrentSet<org.bukkit.Material> bukkitMaterials = materials.stream().map(material -> org.bukkit.Material.valueOf(material.name())).collect(Concurrent.toSet());
		return this.getHandle().getLineOfSight(bukkitMaterials, distance).stream().map(CraftBlock::new).collect(Concurrent.toList());
	}

	@Override
	public double getMaxHealth() {
		return this.getHandle().getMaxHealth();
	}

	@Override
	public int getMaximumAir() {
		return this.getHandle().getMaximumAir();
	}

	@Override
	public int getMaximumNoDamageTicks() {
		return this.getHandle().getMaximumNoDamageTicks();
	}

	@Override
	public int getNoDamageTicks() {
		return this.getHandle().getNoDamageTicks();
	}

	@Override
	public int getRemainingAir() {
		return this.getHandle().getRemainingAir();
	}

	@Override
	public boolean getRemoveWhenFarAway() {
		return this.getHandle().getRemoveWhenFarAway();
	}

	@Override
	public Block getTargetBlock(Set<Material> materials, int distance) {
		ConcurrentSet<org.bukkit.Material> bukkitMaterials = materials.stream().map(material -> org.bukkit.Material.valueOf(material.name())).collect(Concurrent.toSet());
		return new CraftBlock(this.getHandle().getTargetBlock(bukkitMaterials, distance));
	}

	@Override
	public boolean hasAI() {
		return this.getHandle().hasAI();
	}

	@Override
	public boolean hasLineOfSight(Entity entity) {
		return this.getHandle().hasLineOfSight(((CraftEntity)entity).getHandle());
	}

	@Override
	public boolean isCollidable() {
		return this.getHandle().isCollidable();
	}

	@Override
	public boolean isGliding() {
		return this.getHandle().isGliding();
	}

	@Override
	public boolean isLeashed() {
		return this.getHandle().isLeashed();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
		Class<? extends org.bukkit.entity.Projectile> projectileClass = (Class<org.bukkit.entity.Projectile>)CraftEntityType.getByClass(projectile).getBukkitClass();
		org.bukkit.entity.Projectile bukkitProjectile = this.getHandle().launchProjectile(projectileClass, CraftConverter.toBukkitVector(velocity));
		return CraftEntity.convertBukkitEntity(bukkitProjectile, projectile);
	}

	@Override
	public void removePotionEffect(PotionEffectType effectType) {
		this.getHandle().removePotionEffect(org.bukkit.potion.PotionEffectType.getByName(effectType.getName()));
	}

	@Override
	public void resetMaxHealth() {
		this.getHandle().resetMaxHealth();
	}

	@Override
	public void setAI(boolean value) {
		this.getHandle().setAI(value);
	}

	@Override
	public void setCanPickupItems(boolean value) {
		this.getHandle().setCanPickupItems(value);
	}

	@Override
	public void setCollidable(boolean value) {
		this.getHandle().setCollidable(value);
	}

	@Override
	public void setGliding(boolean value) {
		this.getHandle().setGliding(value);
	}

	@Override
	public void setHealth(double health) {
		this.getHandle().setHealth(health);
	}

	@Override
	public void setLastDamage(double value) {
		this.getHandle().setLastDamage(value);
	}

	@Override
	public boolean setLeashHolder(Entity entity) {
		return this.getHandle().setLeashHolder(((CraftEntity)entity).getHandle());
	}

	@Override
	public void setMaxHealth(double maxHealth) {
		this.getHandle().setMaxHealth(maxHealth);
	}

	@Override
	public void setMaximumAir(int value) {
		this.getHandle().setMaximumAir(value);
	}

	@Override
	public void setMaximumNoDamageTicks(int ticks) {
		this.getHandle().setMaximumNoDamageTicks(ticks);
	}

	@Override
	public void setNoDamageTicks(int ticks) {
		this.getHandle().setNoDamageTicks(ticks);
	}

	@Override
	public void setRemainingAir(int value) {
		this.getHandle().setRemainingAir(value);
	}

	@Override
	public void setRemoveWhenFarAway(boolean value) {
		this.getHandle().setRemoveWhenFarAway(value);
	}

}