package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.FireworkEffect;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.FireworkMeta;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.Collection;
import java.util.List;

public final class CraftFireworkMeta extends CraftItemMeta implements FireworkMeta {

	public CraftFireworkMeta(org.bukkit.inventory.meta.FireworkMeta fireworkMeta) {
		super(fireworkMeta);
	}

	@Override
	public void addEffects(Collection<? extends FireworkEffect> effects) {
		this.getHandle().addEffects(effects.stream().map(CraftConverter::toBukkitEffect).toArray(org.bukkit.FireworkEffect[]::new));
	}

	@Override
	public void clearEffects() {
		this.getHandle().clearEffects();
	}

	@Override
	public FireworkMeta clone() {
		return new CraftFireworkMeta(this.getHandle().clone());
	}

	@Override
	public List<FireworkEffect> getEffects() {
		return this.getHandle().getEffects().stream().map(CraftConverter::fromBukkitEffect).collect(Concurrent.toList());
	}

	@Override
	public org.bukkit.inventory.meta.FireworkMeta getHandle() {
		return (org.bukkit.inventory.meta.FireworkMeta)super.getHandle();
	}

	@Override
	public int getPower() {
		return this.getHandle().getPower();
	}

	@Override
	public void removeEffect(int index) {
		this.getHandle().removeEffect(index);
	}

	@Override
	public void setPower(int power) {
		this.getHandle().setPower(power);
	}

}