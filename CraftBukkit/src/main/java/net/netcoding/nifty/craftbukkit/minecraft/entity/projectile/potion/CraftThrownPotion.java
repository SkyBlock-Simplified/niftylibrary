package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.potion;

import net.netcoding.nifty.common.minecraft.entity.projectile.potion.ThrownPotion;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.CraftProjectile;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.Collection;

public class CraftThrownPotion extends CraftProjectile implements ThrownPotion {

	public CraftThrownPotion(org.bukkit.entity.ThrownPotion thrownPotion) {
		super(thrownPotion);
	}

	@Override
	public Collection<PotionEffect> getEffects() {
		return this.getHandle().getEffects().stream().map(CraftConverter::fromBukkitEffect).collect(Concurrent.toList());
	}

	@Override
	public org.bukkit.entity.ThrownPotion getHandle() {
		return (org.bukkit.entity.ThrownPotion)super.getHandle();
	}

	@Override
	public ItemStack getItem() {
		return new CraftItemStack(this.getHandle().getItem());
	}

	@Override
	public void setItem(ItemStack item) {
		this.getHandle().setItem(((CraftItemStack)item).getHandle());
	}

}