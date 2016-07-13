package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.MapMeta;

public final class CraftMapMeta extends CraftItemMeta implements MapMeta {

	public CraftMapMeta(org.bukkit.inventory.meta.MapMeta mapMeta) {
		super(mapMeta);
	}

	@Override
	public MapMeta clone() {
		return new CraftMapMeta(this.getHandle().clone());
	}

	@Override
	public org.bukkit.inventory.meta.MapMeta getHandle() {
		return (org.bukkit.inventory.meta.MapMeta)super.getHandle();
	}

	@Override
	public boolean isScaling() {
		return this.getHandle().isScaling();
	}

	@Override
	public void setScaling(boolean value) {
		this.getHandle().setScaling(value);
	}

}