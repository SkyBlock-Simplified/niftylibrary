package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.FireworkEffect;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.FireworkEffectMeta;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public final class CraftFireworkEffectMeta extends CraftItemMeta implements FireworkEffectMeta {

	public CraftFireworkEffectMeta(org.bukkit.inventory.meta.FireworkEffectMeta fireworkEffectMeta) {
		super(fireworkEffectMeta);
	}

	@Override
	public FireworkEffectMeta clone() {
		return new CraftFireworkEffectMeta(this.getHandle().clone());
	}

	@Override
	public FireworkEffect getEffect() {
		return CraftConverter.fromBukkitEffect(this.getHandle().getEffect());
	}

	@Override
	public org.bukkit.inventory.meta.FireworkEffectMeta getHandle() {
		return (org.bukkit.inventory.meta.FireworkEffectMeta)super.getHandle();
	}

	@Override
	public void setEffect(FireworkEffect effect) {
		this.getHandle().setEffect(CraftConverter.toBukkitEffect(effect));
	}

}