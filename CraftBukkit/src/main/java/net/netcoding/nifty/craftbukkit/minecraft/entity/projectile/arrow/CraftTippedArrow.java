package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.arrow;

import net.netcoding.nifty.common.minecraft.entity.projectile.arrow.TippedArrow;
import net.netcoding.nifty.common.minecraft.potion.PotionData;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.List;

public final class CraftTippedArrow extends CraftArrow implements TippedArrow {

	public CraftTippedArrow(org.bukkit.entity.TippedArrow tippedArrow) {
		super(tippedArrow);
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
	public List<PotionEffect> getCustomEffects() {
		return this.getHandle().getCustomEffects().stream().map(CraftConverter::fromBukkitEffect).collect(Concurrent.toList());
	}

	@Override
	public org.bukkit.entity.TippedArrow getHandle() {
		return (org.bukkit.entity.TippedArrow)super.getHandle();
	}

	@Override
	public boolean removeCustomEffect(PotionEffectType type) {
		return this.getHandle().removeCustomEffect(org.bukkit.potion.PotionEffectType.getByName(type.getName()));
	}

	@Override
	public void setBasePotionData(PotionData data) {
		this.getHandle().setBasePotionData(CraftConverter.toBukkitData(data));
	}

}