package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.PotionMeta;
import net.netcoding.nifty.common.minecraft.potion.PotionData;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.List;

public final class CraftPotionMeta extends CraftItemMeta implements PotionMeta {

	public CraftPotionMeta(org.bukkit.inventory.meta.PotionMeta potionMeta) {
		super(potionMeta);
	}

	@Override
	public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
		return this.getHandle().addCustomEffect(CraftConverter.toBukkitEffect(effect), overwrite);
	}

	@Override
	public boolean clearCustomEffects() {
		return this.getHandle().clearCustomEffects();
	}

	@Override
	public PotionMeta clone() {
		return new CraftPotionMeta(this.getHandle().clone());
	}

	@Override
	public PotionData getBasePotionData() {
		return CraftConverter.fromBukkitData(this.getHandle().getBasePotionData());
	}

	@Override
	public List<PotionEffect> getCustomEffects() {
		return this.getHandle().getCustomEffects().stream().map(CraftConverter::fromBukkitEffect).collect(Concurrent.toList());
	}

	@Override
	public org.bukkit.inventory.meta.PotionMeta getHandle() {
		return (org.bukkit.inventory.meta.PotionMeta)super.getHandle();
	}

	@Override
	public void setBasePotionData(PotionData data) {
		this.getHandle().setBasePotionData(CraftConverter.toBukkitData(data));
	}

	@Override
	public boolean removeCustomEffect(PotionEffectType type) {
		return this.getHandle().removeCustomEffect(org.bukkit.potion.PotionEffectType.getByName(type.getName()));
	}

	@Override
	public boolean setMainEffect(PotionEffectType type) {
		return this.getHandle().setMainEffect(org.bukkit.potion.PotionEffectType.getByName(type.getName()));
	}

}