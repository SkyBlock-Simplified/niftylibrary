package net.netcoding.nifty.craftbukkit.minecraft.potion;

import net.netcoding.nifty.common.minecraft.potion.PotionBrewer;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.potion.PotionType;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;
import org.bukkit.potion.Potion;

import java.util.Collection;

public final class CraftPotionBrewer implements PotionBrewer {

	private static final CraftPotionBrewer INSTANCE = new CraftPotionBrewer();
	private final org.bukkit.potion.PotionBrewer brewer = Potion.getBrewer();

	private CraftPotionBrewer() { }

	@Override
	public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier) {
		return CraftConverter.fromBukkitEffect(this.getHandle().createEffect(org.bukkit.potion.PotionEffectType.getByName(potion.getName()), duration, amplifier));
	}

	@Override
	public Collection<PotionEffect> getEffects(PotionType type, boolean upgraded, boolean extended) {
		return this.getHandle().getEffects(org.bukkit.potion.PotionType.valueOf(type.name()), upgraded, extended).stream().map(CraftConverter::fromBukkitEffect).collect(Concurrent.toList());
	}

	public org.bukkit.potion.PotionBrewer getHandle() {
		return this.brewer;
	}

	public static CraftPotionBrewer getinstance() {
		return INSTANCE;
	}

}