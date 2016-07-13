package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.Firework;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.FireworkMeta;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta.CraftFireworkMeta;

public final class CraftFirework extends CraftEntity implements Firework {

	public CraftFirework(org.bukkit.entity.Firework firework) {
		super(firework);
	}

	@Override
	public void detonate() {
		this.getHandle().detonate();
	}

	@Override
	public FireworkMeta getFireworkMeta() {
		return new CraftFireworkMeta(this.getHandle().getFireworkMeta());
	}

	@Override
	public org.bukkit.entity.Firework getHandle() {
		return (org.bukkit.entity.Firework)super.getHandle();
	}

	@Override
	public void setFireworkMeta(FireworkMeta meta) {
		this.getHandle().setFireworkMeta(((CraftFireworkMeta)meta).getHandle());
	}

}